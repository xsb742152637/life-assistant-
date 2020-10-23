package com.xie.lifeassistant.controller.core.dicinfo;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xie.lifeassistant.model.core.dicinfo.entity.CoreDicInfoEntity;
import com.xie.lifeassistant.model.core.dicinfo.service.CoreDicInfoService;
import com.xie.lifeassistant.util.context.Context;
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
 * @comment 数据字典
 * @author xie
 * @since 2020-09-28
 */
@RestController
@RequestMapping("/core/dicinfo")
@Api(tags={"数据字典"})
public class CoreDicInfoController extends GenericController<CoreDicInfoEntity> {

    @Lazy
    @Autowired
    private CoreDicInfoService service;

    @RequestMapping("getList")
    @ApiOperation(value = "查询列表", httpMethod = "POST")
    public String getList(
            @ApiParam(name="searchKey",value="关键字") @RequestParam String searchKey,
            @ApiParam(name="page",value="当前页数",required=true,defaultValue="1") @RequestParam(defaultValue = "1") Integer page,
            @ApiParam(name="rows",value="每页条数",required=true,defaultValue="10") @RequestParam(defaultValue = "10") Integer rows){
        QueryWrapper<CoreDicInfoEntity> qw = new QueryWrapper<>();
        if(StringUtils.isNotBlank(searchKey)){
            qw.and(i -> i
                    .like("DIC_NAME", searchKey).or()
                    .like("DIC_CODE",searchKey).or()
                    .like("DIC_DES",searchKey)
            );
        }

        return getTable(service.page(new Page<>(page,rows), qw));
    }

    @RequestMapping("save")
    @ApiOperation(value = "保存", httpMethod = "POST")
    public String save(CoreDicInfoEntity entity){
        if(entity == null){
            return returnFaild("没有接收到有效数据");
        }
        try {
            entity.setMemberId(Context.getMember().getMemberId());
            if(StringUtils.isBlank(entity.getDicId())){
                entity.setDicId(UUID.randomUUID().toString());
                entity.setSysTime(new Timestamp(System.currentTimeMillis()));
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
