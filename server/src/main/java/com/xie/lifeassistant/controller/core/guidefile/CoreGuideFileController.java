package com.xie.lifeassistant.controller.core.guidefile;

import com.xie.lifeassistant.model.core.guidefile.GuideType;
import com.xie.lifeassistant.model.core.guidefile.entity.CoreGuideFileEntity;
import com.xie.lifeassistant.model.core.guidefile.service.CoreGuideFileService;
import com.xie.lifeassistant.model.core.guidefile.service.core.MenuEx;
import com.xie.lifeassistant.model.core.guidefile.service.impl.CoreGuideFileServiceImpl;
import com.xie.lifeassistant.model.core.menutree.entity.CoreMenuTreeInfoEntity;
import com.xie.lifeassistant.model.core.menutree.service.CoreMenuTreeInfoService;
import com.xie.lifeassistant.util.context.Context;
import com.xie.lifeassistant.util.datamanage.GenericController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @comment 授权文件
 * @author xie
 * @since 2020-09-28
 */
@RestController
@RequestMapping("/core/guidefile")
@Api(tags={"权限管理"})
public class CoreGuideFileController extends GenericController<CoreGuideFileEntity> {

    @Lazy
    @Autowired
    private CoreGuideFileService service;
    @Lazy
    @Autowired
    private CoreMenuTreeInfoService menuTreeService;
    @Lazy
    @Autowired
    private MenuEx menuEx;

    @RequestMapping("getXmlTree")
    @ApiOperation(value = "查询权限信息", httpMethod = "POST")
    public void getXmlTree(HttpServletRequest request, HttpServletResponse response,
                           @ApiParam(name="projectId",value="项目编号",required=true) @RequestParam String projectId){
        response.setContentType("text/xml;charset=UTF-8");
        String xmlStr =  "";
        try {
            if(StringUtils.isNotBlank(projectId)){
                CoreGuideFileServiceImpl.IS_UPDATE = false;
                xmlStr = service.getXmlStr(projectId);
                PrintWriter print = response.getWriter();
                print.print(xmlStr);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @RequestMapping("save")
    @ApiOperation(value = "查询权限信息", httpMethod = "POST")
    public String save(
            @ApiParam(name="projectId",value="项目编号",required=true) @RequestParam String projectId,
            @ApiParam(name="str",value="权限信息",required=true) @RequestParam String str){
        if(StringUtils.isNotBlank(projectId) && StringUtils.isNotBlank(str)){
            if(CoreGuideFileServiceImpl.IS_UPDATE){
                return returnFaild("保存失败！成员或菜单信息已改变，当前页面将重新加载");
            }
            try{
                str = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"+str;
                service.save(projectId,str);

                System.out.println("修改权限" + (new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()))+"\nprojectId："+projectId+"\n"+str+"\n\n");
                return returnSuccess("保存成功！");
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return returnFaild("保存失败！");
    }

    //清空授权
    @RequestMapping("removeGuide")
    @ApiOperation(value = "清空权限", httpMethod = "POST")
    public String removeGuide(){
        try{
            service.deleteAll();
            System.out.println("清空权限");
            return returnSuccess("清空成功！");
        }catch (Exception e){
            e.printStackTrace();
        }
        return returnFaild("清空失败！");
    }

    //复制到人，同一项目部不同成员之间的复制，但会影响上下级菜单及成员
    @RequestMapping("copyMem")
    @ApiOperation(value = "成员复制", httpMethod = "POST")
    public String copyMem(@ApiParam(name="copy_memberIds",value="多个目标成员",required=true) @RequestParam String copy_memberIds,
                          @ApiParam(name="projectId",value="项目部",required=true) @RequestParam String projectId,
                          @ApiParam(name="memberId",value="源成员",required=true) @RequestParam String memberId)throws Exception{
        if(StringUtils.isBlank(copy_memberIds)){
            return returnFaild("请至少选择一个需要复制授权的成员！");
        }else{
            int r = menuEx.copyMem(copy_memberIds,projectId,memberId);
            return returnSuccess("成功复制" + r + "条权限！");
        }
    }

    @RequestMapping("canUpdate")
    @ApiOperation(value = "是否可编辑某个模块", httpMethod = "POST")
    public boolean canUpdate(@ApiParam(name="menuCode",value="菜单编码",required=true) @RequestParam String menuCode)throws Exception{
        boolean isHave = false;
        CoreMenuTreeInfoEntity entity = null;
        if(StringUtils.isBlank(menuCode)){
            entity = Context.getMenuTree();
        }else{
            entity = menuTreeService.findByCode(menuCode);
        }

        if(entity != null){
            isHave = SecurityUtils.getSubject().isPermitted(entity.getMenuId() + ":" + GuideType.Update.getCode());
        }
        return isHave;
    }

}
