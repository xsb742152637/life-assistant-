package com.xie.lifeassistant.model.core.menutree.service.impl;

import com.xie.lifeassistant.model.core.guidefile.service.core.MenuEx;
import com.xie.lifeassistant.model.core.menutree.dao.CoreMenuTreeInfoDao;
import com.xie.lifeassistant.model.core.menutree.entity.CoreMenuTreeInfoEntity;
import com.xie.lifeassistant.model.core.menutree.service.CoreMenuTreeInfoService;
import com.xie.lifeassistant.util.context.Context;
import com.xie.lifeassistant.util.datamanage.GenericServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @comment 树形菜单列表
 * @author xie
 * @since 2020-09-28
 */
@Service
public class CoreMenuTreeInfoServiceImpl extends GenericServiceImpl<CoreMenuTreeInfoDao, CoreMenuTreeInfoEntity> implements CoreMenuTreeInfoService {

    @Lazy
    @Autowired
    private MenuEx menuEx;

    //@Cacheable(value = "cacheManager", key = "'CoreMenuTreeServiceImpl.getMenuTree'")
    @Override
    public List<Map<String,Object>> getMenuTree(Boolean needGuide, Boolean isTop, Boolean isShow) {
        //得到权限
        Set<String> set = new HashSet<>();
        if(needGuide){
            Map<String,Set<String>> mapAuth = menuEx.getGuides();
            Set<String> set1 = mapAuth.get(Context.getMember().getMemberId());
            for(String s : set1){
                set.add(s.split(":")[0]);
            }
        }

        //1. 得到全部显示的菜单树的列表
        List<Map<String,Object>> list = underlineToHump(getBaseMapper().getMenuTree(isShow));
        //2. 将列表转换为树形结构

        //2.1 将所有父级菜单放到此map中,方便后续菜单快速找到爹
        Map<String,Map<String,Object>> mapP = new HashMap<>();
        //2.2 最终返回的树形结构
        List<Map<String,Object>> listR = new ArrayList<>();
        for(Map<String,Object> map : list){
            if(!isTop && "root".equals(map.get("menuId").toString())) {
                continue;
            }
            if("root".equals(map.get("menuId").toString())) {
                //根节点默认展开
                map.put("readonly",true);
                map.put("spread",true);
            }
            map.put("id",map.get("menuId"));

            //该菜单是否有权限
            boolean isHaveGuide = !needGuide;
            if(needGuide && set != null && set.size() > 0){
                isHaveGuide = set.contains(map.get("menuId").toString());
            }
            map.put("isHaveGuide",isHaveGuide);

            String outlineLevel = map.get("outlineLevel").toString();
            String parentLevel = outlineLevel.contains(".") ? outlineLevel.substring(0,outlineLevel.lastIndexOf(".")) : "";

            Map<String,Object> mp = mapP.get(parentLevel);
            //找不到爹就自己当祖宗
            if(mp == null){
                listR.add(map);
            }else{
                List<Map<String,Object>> listC = mp.get("children") == null ? new ArrayList<Map<String, Object>>() : (List<Map<String,Object>>) mp.get("children");
                listC.add(map);
                mp.put("children",listC);
                if(listC.size() > 0){
                    mp.put("url","");//如果存在下级，则不允许有路径。作用于左侧菜单
                }
            }
            //自己很有可能有儿子,所有先到当爹的列表中等着儿子来找.
            mapP.put(outlineLevel,map);
        }

        if(listR.size() > 0){
            listR.get(0).put("state","open");
        }
        return listR;
    }

    @Override
    public CoreMenuTreeInfoEntity findByCode(String code){
        CoreMenuTreeInfoEntity entity = getBaseMapper().findByCode(code);
        if(entity != null){
            return entity;
        }else{
            System.out.println("菜单code："+code+"没有找到。");
            return null;
        }
    }

    /**
     * 	//根据上级，得到子级下一个排序数字
     * @param outlineLevel
     * @return
     */
    @Override
    public Integer getMenuLevelByParLevel(String outlineLevel) {
        Integer n = getBaseMapper().getMenuLevelByParLevel(outlineLevel);
        if(n == null){
            return 1;
        }
        return n+1;
    }

    /**
     *移动菜单
     * @param treeId
     * @param type(上移)、false(下移)
     */
    @Override
    @Transactional
    public void moveTree(String treeId, boolean type) {
        CoreMenuTreeInfoEntity entity = findById(treeId);
        if(entity == null) {
            return;
        }
        int menuLevel = entity.getMenuLevel();

        if(type){
            menuLevel--;
        }else{
            menuLevel++;
        }
        String outlineLevel = entity.getOutlineLevel();
        if(outlineLevel.split("\\.").length > 1){
            outlineLevel = outlineLevel.substring(0,outlineLevel.lastIndexOf(".")) + "." + menuLevel;
        }else{
            outlineLevel = String.valueOf(menuLevel);
        }

        CoreMenuTreeInfoEntity entity2 = getBaseMapper().findOneByOutlineLevel(outlineLevel);
        if(entity2 == null) {
            return;
        }

        getBaseMapper().moveChildren(entity2.getOutlineLevel()+".","temp"+".");
        getBaseMapper().moveChildren(entity.getOutlineLevel()+".",entity2.getOutlineLevel()+".");
        getBaseMapper().moveChildren("temp"+".",entity.getOutlineLevel()+".");

        entity2.setMenuLevel(entity.getMenuLevel());
        entity2.setOutlineLevel(entity.getOutlineLevel());

        entity.setMenuLevel(menuLevel);
        entity.setOutlineLevel(outlineLevel);

        List<CoreMenuTreeInfoEntity> list = new ArrayList<>();
        list.add(entity);
        list.add(entity2);
        save(list);
    }


    @Override
    @Transactional
    public int updateAfterDelete(String before, String after) {
        return getBaseMapper().updateAfterDelete(before,after);
    }

    @Override
    @Transactional
    public void delete(String primaryId) {
        CoreMenuTreeInfoEntity entity = findById(primaryId);
        String outlineLevel = entity.getOutlineLevel();
        String before = outlineLevel.substring(0,outlineLevel.lastIndexOf(".")+1);
        String after = outlineLevel.substring(outlineLevel.lastIndexOf(".")+1);
        deleteById(primaryId);
        updateAfterDelete(before,after);
    }

}
