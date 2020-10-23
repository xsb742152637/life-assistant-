package com.xie.lifeassistant.controller.core.menuurl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xie.lifeassistant.model.core.menuurl.entity.CoreMenuUrlInfoEntity;
import com.xie.lifeassistant.model.core.menuurl.service.CoreMenuUrlInfoService;
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

import java.sql.Timestamp;
import java.util.UUID;

/**
 * @comment 菜单路径列表
 * @author xie
 * @since 2020-05-03
 */
@RestController
@RequestMapping("/core/menuurl")
@Api(tags={"菜单路径管理"})
public class CoreMenuUrlInfoController extends GenericController<CoreMenuUrlInfoEntity> {

    @Lazy
    @Autowired
    private CoreMenuUrlInfoService service;

    @RequestMapping("getList")
    @ApiOperation(value = "查询列表", httpMethod = "POST")
    public String getList(@ApiParam(name="searchKey",value="关键字") String searchKey,
                          @ApiParam(name="primaryId",value="主键") String primaryId,
                          @ApiParam(name="page",value="当前页数",required=true,defaultValue="1") @RequestParam(defaultValue = "1") Integer page,
                          @ApiParam(name="rows",value="每页条数",required=true,defaultValue="10")@RequestParam(defaultValue = "10") Integer rows){

        Page<CoreMenuUrlInfoEntity> p = new Page<>(page,rows);

        QueryWrapper<CoreMenuUrlInfoEntity> qw = new QueryWrapper<>();
        if(StringUtils.isNotBlank(primaryId)){
            qw.eq("primaryId", primaryId);
        }
        if(StringUtils.isNotBlank(searchKey)){
            qw.and(i -> i
                    .like("TITLE", searchKey).or()
                    .like("CODE", searchKey).or()
                    .like("URL",searchKey)
            );
        }
        return getTable(service.page(p, qw));
    }

    @RequestMapping("save")
    @ApiOperation(value = "保存", httpMethod = "POST")
    public String save(CoreMenuUrlInfoEntity entity){
        try {
            if(StringUtils.isBlank(entity.getUrlId())){
                entity.setUrlId(UUID.randomUUID().toString());
                entity.setSysTime(new Timestamp(System.currentTimeMillis()));
            }
            service.save(entity);
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
