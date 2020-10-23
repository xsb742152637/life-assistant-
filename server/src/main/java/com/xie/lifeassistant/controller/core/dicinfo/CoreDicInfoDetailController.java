package com.xie.lifeassistant.controller.core.dicinfo;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xie.lifeassistant.model.core.dicinfo.entity.CoreDicInfoDetailEntity;
import com.xie.lifeassistant.model.core.dicinfo.service.CoreDicInfoDetailService;
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

import java.util.UUID;

/**
 * @comment 数据字典明细表
 * @author xie
 * @since 2020-09-28
 */
@RestController
@RequestMapping("/core/dicinfo/detail")
@Api(tags={"数据字典明细"})
public class CoreDicInfoDetailController extends GenericController<CoreDicInfoDetailEntity> {

    @Lazy
    @Autowired
    private CoreDicInfoDetailService service;

    @RequestMapping("getList")
    @ApiOperation(value = "查询列表", httpMethod = "POST")
    public String getList(
            @ApiParam(name="searchKey",value="关键字") @RequestParam String searchKey,
            @ApiParam(name="dicId",value="数据字典主表编号",required=true) @RequestParam String dicId,
            @ApiParam(name="page",value="当前页数",required=true,defaultValue="1") @RequestParam(defaultValue = "1") Integer page,
            @ApiParam(name="rows",value="每页条数",required=true,defaultValue="10") @RequestParam(defaultValue = "10") Integer rows){
        QueryWrapper<CoreDicInfoDetailEntity> qw = new QueryWrapper<>();
        qw.and(i -> i.eq("dicId", dicId));
        if(StringUtils.isNotBlank(searchKey)){
            qw.and(i -> i
                    .like("detailName", searchKey).or()
                    .like("detailCode",searchKey).or()
                    .like("detailValue",searchKey).or()
                    .like("comment",searchKey)
            );
        }

        return getTable(service.page(new Page<>(page,rows), qw));
    }

    @RequestMapping("save")
    @ApiOperation(value = "保存", httpMethod = "POST")
    public String save(CoreDicInfoDetailEntity entity){
        if(entity == null){
            return returnFaild("没有接收到有效数据");
        }
        try {
            if(StringUtils.isBlank(entity.getDetailId())){
                entity.setDetailId(UUID.randomUUID().toString());
            }
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
