package com.xie.lifeassistant.controller.core.menutree;

import com.xie.lifeassistant.model.core.menutree.entity.CoreMenuTreeInfoEntity;
import com.xie.lifeassistant.model.core.menutree.service.CoreMenuTreeInfoService;
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
 * @comment 树形菜单列表
 * @author xie
 * @since 2020-09-28
 */
@RestController
@RequestMapping("/core/menutree")
@Api(tags={"菜单管理"})
public class CoreMenuTreeInfoController extends GenericController<CoreMenuTreeInfoEntity> {

    @Lazy
    @Autowired
    private CoreMenuTreeInfoService service;

    @RequestMapping("getList")
    @ApiOperation(value = "查询列表", httpMethod = "POST")
    public String getList(
            @ApiParam(name="needGuide",value="是否根据权限筛选",defaultValue="false") String needGuide,
            @ApiParam(name="isTop",value="是否显示根节点",defaultValue="true") String isTop,
            @ApiParam(name="isShow",value="是否显示隐藏菜单，默认null") String isShow){

        Boolean isShow2 = null;
        if(StringUtils.isBlank(needGuide)){
            needGuide = "false";
        }
        if(StringUtils.isBlank(isTop)){
            isTop = "true";
        }
        if(StringUtils.isNotBlank(isShow)){
            isShow2 = Boolean.parseBoolean(isShow);
        }

        List<Map<String,Object>> list = service.getMenuTree(Boolean.parseBoolean(needGuide),Boolean.parseBoolean(isTop),isShow2);
        return returnStringByList(list);
    }

    @RequestMapping("save")
    @ApiOperation(value = "保存", httpMethod = "POST")
    public String save(CoreMenuTreeInfoEntity entity){
        try {

            service.saveOrUpdate(entity);
            return returnSuccess();
        }catch (Exception e){
            e.printStackTrace();
            return returnFaild();
        }
    }

    @RequestMapping("delete")
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
