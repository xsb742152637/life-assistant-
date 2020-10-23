package com.xie.lifeassistant.controller.core.treeinfo;

import com.xie.lifeassistant.model.core.treeinfo.entity.CoreTreeInfoEntity;
import com.xie.lifeassistant.model.core.treeinfo.service.CoreTreeInfoService;
import com.xie.lifeassistant.util.datamanage.GenericController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @comment 树形结构表
 * @author xie
 * @since 2020-09-28
 */
@RestController
@RequestMapping("/core/treeinfo")
@Api(tags={"各类树形结构管理"})
public class CoreTreeInfoController extends GenericController<CoreTreeInfoEntity> {

    @Lazy
    @Autowired
    private CoreTreeInfoService service;

    @RequestMapping("getList")
    @ApiOperation(value = "查询列表", httpMethod = "POST")
    public String getList(@ApiParam(name="searchKey",value="关键字") @RequestParam String searchKey,
                          @ApiParam(name="openLevel",value="默认展开层级",required=true,defaultValue="1") @RequestParam(defaultValue="1") Integer openLevel,
                          @ApiParam(name="treeType",value="树类型",required=true) @RequestParam String treeType,
                          @ApiParam(name="parentId",value="父级ID",required=true) @RequestParam String parentId){

        try {
            List<Map<String,Object>> list = service.getList(treeType,parentId, openLevel);
            return returnStringByList(list);
        }catch (Exception e){
            e.printStackTrace();
            return " ";
        }
    }

    @RequestMapping("save")
    @ApiOperation(value = "保存", httpMethod = "POST")
    public String save(CoreTreeInfoEntity entity){
        if(StringUtils.isBlank(entity.getTreeName())){
            return returnFaild("没有得到名称");
        }
        if(StringUtils.isBlank(entity.getParentId())){
            return returnFaild("没有找到上级编号");
        }

        try {
            service.save(entity);
            return returnSuccess(entity.getTreeId());
        }catch (Exception e){
            e.printStackTrace();
            return returnFaild();
        }
    }

    @RequestMapping("move")
    @ApiOperation(value = "移动", httpMethod = "POST")
    public String move(@ApiParam(name="type",value="上移(true)/下移(false)",required=true,defaultValue="true") @RequestParam Boolean type,
                       @ApiParam(name="primaryId",value="主键") @RequestParam String primaryId){
        try {
            service.move(primaryId,type);
            return returnSuccess();
        }catch (Exception e){
            e.printStackTrace();
            return returnFaild();
        }
    }

    @RequestMapping("deleteById")
    @ApiOperation(value = "删除", httpMethod = "POST")
    public String delete(@ApiParam(name="primaryId",value="主键",required=true)  @RequestParam String primaryId){
        try {
            service.deleteById(primaryId);
            return returnSuccess();
        }catch (Exception e){
            e.printStackTrace();
            return returnFaild();
        }
    }
}
