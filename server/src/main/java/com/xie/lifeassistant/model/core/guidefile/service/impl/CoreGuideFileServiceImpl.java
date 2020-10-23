package com.xie.lifeassistant.model.core.guidefile.service.impl;

import com.xie.lifeassistant.model.core.guidefile.dao.CoreGuideFileDao;
import com.xie.lifeassistant.model.core.guidefile.entity.CoreGuideFileEntity;
import com.xie.lifeassistant.model.core.guidefile.service.CoreGuideFileService;
import com.xie.lifeassistant.model.core.guidefile.service.core.Member;
import com.xie.lifeassistant.model.core.guidefile.service.core.Menu;
import com.xie.lifeassistant.model.core.guidefile.service.core.MenuEx;
import com.xie.lifeassistant.model.core.memberinfo.MemberType;
import com.xie.lifeassistant.model.core.menutree.service.CoreMenuTreeInfoService;
import com.xie.lifeassistant.model.core.treeinfo.TreeType;
import com.xie.lifeassistant.model.core.treeinfo.service.CoreTreeInfoService;
import com.xie.lifeassistant.util.datamanage.GenericServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @comment 授权文件
 * @author xie
 * @since 2020-09-28
 */
@Service
public class CoreGuideFileServiceImpl extends GenericServiceImpl<CoreGuideFileDao, CoreGuideFileEntity> implements CoreGuideFileService {
    public static Boolean IS_UPDATE = false;//如果成员或菜单有更新，则需要用户刷新新版授权页面
    protected Member memberEntity = new Member();

    @Lazy
    @Autowired
    private CoreTreeInfoService treeService;
    @Lazy
    @Autowired
    private CoreMenuTreeInfoService menuTreeService;
    @Lazy
    @Autowired
    private MenuEx menuEx;

    @Override
    public String getXmlStr(String projectId){
        String str = "";
        CoreGuideFileEntity en = getBaseMapper().selectById(projectId);
        if(en != null){
            str = en.getDocument().toString();
        }
        if(StringUtils.isBlank(str)){
            try{
                if(projectId.equals(Member.PROJECT_ID)){
                    str = createMemberXml("系统",null,null);
                }else if(projectId.equals(Menu.PROJECT_ID)){
                    str = createMenuXml();
                }else{
                    str = createProjectXml(projectId);
                }
            }catch (Exception e){
                System.out.println(e);
                e.printStackTrace();
            }
        }
        return str;
    }

    @Override
    @Transactional
    public CoreGuideFileEntity save(String projectId, String str) {// 保存
        CoreGuideFileEntity en = new CoreGuideFileEntity();
        en.setGuideId(projectId);
        en.setDocument(str);

        save(en);
        menuEx.removeCache();
        return en;
    }

    @Override
    @Transactional
    public CoreGuideFileEntity save(String projectId, Document d) {// 保存
        return save(projectId,d.asXML());
    }

    @Override
    public String createMemberXml(String sourceType,String memberId,String memberName){
        Document _document = DocumentHelper.createDocument();
        if(_document == null){
            return "";
        }

        //得到成员树
        List<Map<String,Object>> list = treeService.getList(String.valueOf(TreeType.MemberInfo.getCode()));
        addMemEle(_document,null,list);

        deleteById(Member.PROJECT_ID);
        save(Member.PROJECT_ID,_document);
        memberEntity.removeCache();

        String eventData = "sourceType：" + sourceType + "；memberId：" + memberId + "；memberName：" + memberName ;
        System.out.println(eventData);
        return _document.asXML().toString();
    }

    public String createMenuXml(){
        Document _document = DocumentHelper.createDocument();
        if(_document == null){
            return "";
        }

        List<Map<String,Object>> list = menuTreeService.getMenuTree(false,true,null);
        addMenuEle(_document,null,list);

        deleteById(Menu.PROJECT_ID);
        save(Menu.PROJECT_ID,_document);
        System.out.println("创建菜单树");
        return _document.asXML().toString();
    }

    public String createProjectXml(String projectId){
        String f = getXmlStr(Menu.PROJECT_ID);
        if(StringUtils.isBlank(f)){
            f = createMenuXml();
        }
        Menu menuEntity = new Menu(null,null,f);

        Document _document = DocumentHelper.createDocument();
        if(_document == null){
            return "";
        }

        //创建跟节点
        Element root = _document.addElement(Menu.MENU_TAG);
        root.setAttributes(menuEntity.getRoot().attributes());
        List<Element> list = menuEntity.getRoot().elements();
        for(Element el : list){
            Element e = root.addElement(Menu.MENU_TAG);
            e.setAttributes(el.attributes());
            addProEle(e,el.elements());
        }

        deleteById(projectId);
        save(projectId,_document);
        System.out.println("创建项目树");
        return _document.asXML().toString();
    }

    private void addMemEle(Document doc,Element parEle,List<Map<String,Object>> list){
        for(Map<String,Object> map : list) {
            Element newEle = null;
            if(parEle == null){
                newEle = doc.addElement(Member.MEMBER_TAG);
            }else{
                newEle = parEle.addElement(Member.MEMBER_TAG);
            }
            if(map.get("memberId") == null || StringUtils.isBlank(map.get("memberId").toString())){
                System.out.println("没有找到成员：" + map.get("treeName").toString());
                continue;
            }
            newEle.addAttribute("id",map.get("memberId").toString() + (String.valueOf(MemberType.Person.getCode()).equals(map.get("memberType").toString()) ? (";" + map.get("parentId").toString()) : ""));
            newEle.addAttribute("n",map.get("memberName").toString());
            newEle.addAttribute("p",map.get("memberType").toString());//是person
            if(map.get("children") != null){
                addMemEle(doc,newEle,(List<Map<String,Object>>) map.get("children"));
            }
        }
    }

    private void addMenuEle(Document doc,Element parEle,List<Map<String,Object>> list){
        for(Map<String,Object> map : list) {
            Element newEle = null;
            if(parEle == null){
                newEle = doc.addElement(Member.MENU_TAG);
            }else{
                newEle = parEle.addElement(Member.MENU_TAG);
            }
            newEle.addAttribute("id",map.get("id").toString());
            newEle.addAttribute("n",map.get("title").toString());
            if(map.get("children") != null){
                addMenuEle(doc,newEle,(List<Map<String,Object>>) map.get("children"));
            }
        }
    }

    private void addProEle(Element p,List<Element> list){
        if(list == null || list.size() < 1){
            return;
        }
        for(Element el : list){
            Element e = p.addElement(Menu.MENU_TAG);
            e.setAttributes(el.attributes());
            addProEle(e,el.elements());
        }
    }

    @Override
    @Transactional
    public void addMenu(String parId,String menuId,String menuName){
        IS_UPDATE = true;
        List<CoreGuideFileEntity> list = findAll();
        List<CoreGuideFileEntity> listU = new ArrayList<>();
        for(CoreGuideFileEntity entity : list){
            if(Member.PROJECT_ID.equalsIgnoreCase(entity.getGuideId()) ){
                continue;
            }
            Menu menuEntity = new Menu(null,null,entity.getDocument());
            List<Element> listM = menuEntity.getMenuItems(menuId);
            if(listM.size() > 0) {
                for(Element e : listM){
                    e.addAttribute("n",menuName);
                    e.addAttribute("ut",Menu.sdf.format(new Date()));
                }
            }else {
                //d1794973-0b31-47a5-a24e-a9ea52c3dedc
                //d80e8493-c97e-4c51-83df-c4b143d66c5f
                listM = menuEntity.getMenuItems(parId);
                for (Element e : listM) {
                    Element m = e.addElement(Menu.MENU_TAG);
                    m.addAttribute("id", menuId);
                    m.addAttribute("n", menuName);
                    m.addAttribute("ut",Menu.sdf.format(new Date()));
                }
            }
            entity.setDocument(menuEntity.getContext().asXML());
            listU.add(entity);
        }
        save(listU);

        System.out.println("调整菜单" + "parId："+parId+"，menuId："+menuId+"，menuName："+menuName);
    }

    @Override
    @Transactional
    public void deleteMenu(String menuId){
        IS_UPDATE = true;
        String menuName = "";
        List<CoreGuideFileEntity> list = findAll();
        List<CoreGuideFileEntity> listU = new ArrayList<>();
        for(CoreGuideFileEntity entity : list){
            if(Member.PROJECT_ID.equalsIgnoreCase(entity.getGuideId()) || Menu.PROJECT_ID.equalsIgnoreCase(entity.getGuideId()) ){
                continue;
            }
            Menu menuEntity = new Menu(null,null,entity.getDocument());
            List<Element> listM = menuEntity.getMenuItems(menuId);
            if(listM.size() > 0) {
                for(Element e : listM){
                    if(StringUtils.isBlank(menuName)){
                        menuName = e.attributeValue("n");
                    }
                    Element ep = e.getParent();
                    if(ep == null){
                        continue;
                    }
                    //删除菜单
                    ep.remove(e);
                }
            }
            entity.setDocument(menuEntity.getContext().asXML());
            listU.add(entity);
        }
        save(listU);
        //删除菜单模板文件
        deleteById(Menu.PROJECT_ID);
        //新增菜单模板文件
        createMenuXml();
        System.out.println("删除菜单" + "menuId："+menuId+"，menuName："+menuName);
    }

}
