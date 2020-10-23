package com.xie.lifeassistant.model.core.treeinfo.service.impl;

import com.xie.lifeassistant.model.core.guidefile.service.CoreGuideFileService;
import com.xie.lifeassistant.model.core.memberinfo.MemberType;
import com.xie.lifeassistant.model.core.memberinfo.entity.CoreMemberInfoEntity;
import com.xie.lifeassistant.model.core.memberinfo.service.CoreMemberInfoService;
import com.xie.lifeassistant.model.core.treeinfo.TreeType;
import com.xie.lifeassistant.model.core.treeinfo.dao.CoreTreeInfoDao;
import com.xie.lifeassistant.model.core.treeinfo.entity.CoreTreeInfoEntity;
import com.xie.lifeassistant.model.core.treeinfo.service.CoreTreeInfoService;
import com.xie.lifeassistant.util.config.BaseSettingConfig;
import com.xie.lifeassistant.util.datamanage.GenericServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @comment 树形结构表
 * @author xie
 * @since 2020-09-28
 */
@Service
public class CoreTreeInfoServiceImpl extends GenericServiceImpl<CoreTreeInfoDao, CoreTreeInfoEntity> implements CoreTreeInfoService {

    //默认管理员分组
    public final static String ADMINI_GROUP_ID = "11111111-1111-1111-1111-111111111111";
    //默认管理员
    public final static String ADMINI_MEMBER_ID = "22222222-2222-2222-2222-222222222222";
    private static Boolean isInto = false;

    @Lazy
    @Autowired
    private CoreMemberInfoService memberService;
    @Lazy
    @Autowired
    private CoreGuideFileService guideFileService;
    @Lazy
    @Autowired
    private BaseSettingConfig baseSettingConfig;

    @Override
    public List<Map<String, Object>> getList(String treeType) {
        return getList(treeType,null,1);
    }

    @Override
    public List<Map<String, Object>> getList(String treeType, String parentId, int openLevel) {
        Map<String,Map<String,Object>> mapOther = new HashMap<>();
        if(String.valueOf(TreeType.MemberInfo.getCode()).equals(treeType)){
            //得到全部成员
            List<CoreMemberInfoEntity> memList= memberService.findAll();
            for(CoreMemberInfoEntity entity : memList){
                Map<String,Object> m = entityToMap(entity);
                if(String.valueOf(MemberType.Group.getCode()).equals(m.get("memberType").toString())){
                    m.put("icon","layui-icon-group");
                }if(String.valueOf(MemberType.Person.getCode()).equals(m.get("memberType").toString())){
                    m.put("icon","layui-icon-username");
                }
                mapOther.put(entity.getTreeId(),m);
            }
        }

        List<CoreTreeInfoEntity> list = getBaseMapper().getMainInfo(treeType,parentId);
        LinkedHashMap<String,List<Map<String, Object>>> map = new LinkedHashMap<>();
        List<Map<String, Object>> listRoot = new ArrayList<>();
        for(CoreTreeInfoEntity entity : list){
            Map<String, Object> m = entityToMap(entity);
            m.put("id", m.get("treeId"));
            m.put("title",m.get("treeName")+"  ("+m.get("treeLeft")+"-"+m.get("treeRight")+")");
            if(StringUtils.isBlank(entity.getParentId())){
                m.put("icon", "layui-icon-tree");
                listRoot.add(m);
                continue;
            }
            List<Map<String, Object>> li = map.get(entity.getParentId());
            if(li == null){
                li = new ArrayList<>();
            }
            li.add(m);
            map.put(entity.getParentId(), li);
        }
        int thisLevel = 0;

        getTrees(listRoot,map,mapOther,openLevel,thisLevel);
        return listRoot;
    }

    private void getTrees(List<Map<String, Object>> listPar,LinkedHashMap<String,List<Map<String, Object>>> map,Map<String,Map<String,Object>> mapOther,int openLevel,int thisLevel){
        thisLevel++;
        for(Map<String, Object> parMap : listPar){
            String treeId = parMap.get("treeId").toString();
            List<Map<String, Object>> ch = map.get(treeId);
            if(ch != null && ch.size() > 0){
                getTrees(ch,map,mapOther,openLevel,thisLevel);
            }
            if(openLevel== -1 || thisLevel <= openLevel){
                parMap.put("spread",true);
            }
            parMap.put("children", ch);
            if(mapOther.get(treeId) != null){
                Map<String,Object> m = mapOther.get(treeId);
                m.putAll(parMap);
                parMap.putAll(m);
            }
        }
    }

    public List<CoreTreeInfoEntity> findRoots() {
        return getBaseMapper().findRoots();
    }

    @Override
    @Transactional
    public void intoTrees(){
        if(isInto){
            return;
        }
        isInto = true;
        String name = baseSettingConfig.getName();
        System.out.println("初始化树，系统名称：" + name);
        List<CoreTreeInfoEntity> list = findRoots();
        List<String> rootIds = new ArrayList<>();
        for(CoreTreeInfoEntity entity : list){
            rootIds.add(entity.getTreeId());
        }

        List<CoreMemberInfoEntity> addMemList = new ArrayList<>();
        List<CoreTreeInfoEntity> addList = new ArrayList<>();
        for(TreeType en : TreeType.values()){
            if(!rootIds.contains(en.toString())){
                //为各类型树新增根节点
                CoreTreeInfoEntity entity = new CoreTreeInfoEntity();
                entity.setTreeId(en.toString());
                entity.setParentId(null);
                entity.setTreeName(name);
                entity.setTreeLeft(1);
                entity.setTreeRight(6);
                entity.setTreeType(en.getCode());
                entity.setCanUpdate(false);
                entity.setCanDelete(false);
                addList.add(entity);

                //为根成员新增信息
                if(TreeType.MemberInfo.toString().equals(en.toString())){
                    CoreMemberInfoEntity mem = new CoreMemberInfoEntity();
                    mem.setMemberId(en.toString());
                    mem.setMemberName(name);
                    mem.setMemberType(MemberType.Group.getCode());
                    mem.setMemberState(true);
                    mem.setTreeId(entity.getTreeId());
                    addMemList.add(mem);

                    String parentId = entity.getTreeId().toString();
                    entity = new CoreTreeInfoEntity();
                    entity.setTreeId(ADMINI_GROUP_ID);
                    entity.setParentId(parentId);
                    entity.setTreeName("管理员");
                    entity.setTreeLeft(2);
                    entity.setTreeRight(5);
                    entity.setTreeType(en.getCode());
                    entity.setCanUpdate(false);
                    entity.setCanDelete(false);
                    addList.add(entity);

                    mem = new CoreMemberInfoEntity();
                    mem.setMemberId(ADMINI_GROUP_ID);
                    mem.setMemberName("管理员");
                    mem.setMemberType(MemberType.Group.getCode());
                    mem.setMemberState(true);
                    mem.setTreeId(entity.getTreeId());
                    addMemList.add(mem);

                    parentId = entity.getTreeId().toString();
                    entity = new CoreTreeInfoEntity();
                    entity.setTreeId(ADMINI_MEMBER_ID);
                    entity.setParentId(parentId);
                    entity.setTreeName("admini");
                    entity.setTreeLeft(3);
                    entity.setTreeRight(4);
                    entity.setTreeType(en.getCode());
                    entity.setCanUpdate(true);
                    entity.setCanDelete(false);
                    addList.add(entity);

                    mem = new CoreMemberInfoEntity();
                    mem.setMemberId(ADMINI_MEMBER_ID);
                    mem.setMemberName("admini");
                    mem.setMemberType(MemberType.Person.getCode());
                    mem.setAccount("admini");
                    mem.setPassword("111");
                    mem.setMemberState(true);
                    mem.setTreeId(entity.getTreeId());
                    addMemList.add(mem);
                }
            }
        }
        if(addList.size() > 0){
            save(addList);
        }if(addMemList.size() > 0){
            memberService.save(addMemList);

            //授权xml同步成员
            guideFileService.createMemberXml("add",ADMINI_MEMBER_ID,"admini");
        }

    }

    //重写了默认的save方法
    @Override
    @Transactional
    public boolean save(CoreTreeInfoEntity entity){
        if(StringUtils.isBlank(entity.getTreeId())){
            CoreTreeInfoEntity parEntity = findById(entity.getParentId());

            entity.setTreeId(UUID.randomUUID().toString());
            entity.setParentId(parEntity.getTreeId());
            entity.setTreeLeft(parEntity.getTreeRight());
            entity.setTreeRight(entity.getTreeLeft() + 1);

            saveOrUpdate(entity);
            //改前面的
            getBaseMapper().insert_updateBefore(parEntity.getTreeType(),parEntity.getTreeLeft(),parEntity.getTreeRight());
            //改后面的
            getBaseMapper().insert_updateAfter(parEntity.getTreeType(),entity.getTreeLeft());
        }else{
            CoreTreeInfoEntity entitytemp = findById(entity.getTreeId());
            entitytemp.setTreeName(entity.getTreeName());
            saveOrUpdate(entitytemp);
        }
        return true;
    }

    @Override
    @Transactional
    public void move(String primaryId, Boolean moveOn) {
        CoreTreeInfoEntity entity = findById(primaryId);
        CoreTreeInfoEntity neighborEntity = getBaseMapper().findNeighborEntity(entity,moveOn);
        if(neighborEntity == null || entity == null){
            return;
        }
        List<CoreTreeInfoEntity> moveEntitySons = getBaseMapper().findSons(primaryId);
        List<CoreTreeInfoEntity> neighborEntitySons = getBaseMapper().findSons(neighborEntity.getTreeId());
        moveEntitySons.add(entity);
        neighborEntitySons.add(neighborEntity);
        if(moveOn){
            for (CoreTreeInfoEntity e:moveEntitySons){
                e.setTreeLeft(e.getTreeLeft() - 2 * neighborEntitySons.size());
                e.setTreeRight(e.getTreeRight() - 2 * neighborEntitySons.size());
            }
            for (CoreTreeInfoEntity e:neighborEntitySons){
                e.setTreeLeft(e.getTreeLeft() + 2 * moveEntitySons.size());
                e.setTreeRight(e.getTreeRight() + 2 * moveEntitySons.size());
            }
        }else {
            for (CoreTreeInfoEntity e:moveEntitySons){
                e.setTreeLeft(e.getTreeLeft() + 2 * neighborEntitySons.size());
                e.setTreeRight(e.getTreeRight() + 2 * neighborEntitySons.size());
            }
            for (CoreTreeInfoEntity e:neighborEntitySons){
                e.setTreeLeft(e.getTreeLeft() - 2 * moveEntitySons.size());
                e.setTreeRight(e.getTreeRight() - 2 * moveEntitySons.size());
            }
        }

        List<CoreTreeInfoEntity> list = new ArrayList<>();
        list.addAll(neighborEntitySons);
        list.addAll(moveEntitySons);

        save(list);
    }

    @Override
    @Transactional
    public void deleteById(String primaryId) {
        System.out.println("我的删除方法");
        CoreTreeInfoEntity entitySelf = findById(primaryId);
        List<CoreTreeInfoEntity> list = getBaseMapper().findSons(primaryId);
        CoreTreeInfoEntity entityLastSon;
        if(list.size()>0){
            entityLastSon = list.get(0);
        }   else {
            entityLastSon = entitySelf;
        }
        Integer sonCount = list.size()+1;

        //删除自己及子孙节点
        getBaseMapper().delete(primaryId);
        //改前面的左右值
        getBaseMapper().delete_updateBefor(entitySelf.getTreeType(),entitySelf.getTreeLeft(),entitySelf.getTreeRight(),sonCount);
        //改后面的左右值
        getBaseMapper().delete_updateAfter(entityLastSon.getTreeType(),entityLastSon.getTreeLeft(),sonCount);
    }

}
