package com.xie.lifeassistant.controller.core.memberinfo;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xie.lifeassistant.model.core.guidefile.service.CoreGuideFileService;
import com.xie.lifeassistant.model.core.guidefile.service.core.MenuEx;
import com.xie.lifeassistant.model.core.memberinfo.entity.CoreMemberInfoEntity;
import com.xie.lifeassistant.model.core.memberinfo.service.CoreMemberInfoService;
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
 * @comment 
 * @author xie
 * @since 2020-09-28
 */
@RestController
@RequestMapping("/core/memberinfo")
@Api(tags={"成员管理"})
public class CoreMemberInfoController extends GenericController<CoreMemberInfoEntity> {

    @Lazy
    @Autowired
    private CoreMemberInfoService service;
    @Lazy
    @Autowired
    private CoreGuideFileService guideFileService;
    @Lazy
    @Autowired
    private MenuEx menuEx;

    @RequestMapping("getList")
    @ApiOperation(value = "查询列表", httpMethod = "POST")
    public String getList(@ApiParam(name="searchKey",value="关键字") @RequestParam String searchKey,
            @ApiParam(name="page",value="当前页数",required=true,defaultValue="1") @RequestParam(defaultValue = "1") Integer page,
            @ApiParam(name="rows",value="每页条数",required=true,defaultValue="10") @RequestParam(defaultValue = "10") Integer rows){

        QueryWrapper<CoreMemberInfoEntity> qw = new QueryWrapper<>();
        if(StringUtils.isNotBlank(searchKey)){
            qw.and(i -> i
                    .like("", searchKey).or()
                    .like("",searchKey)
            );
        }
        return getTable(service.page(new Page<>(page,rows), qw));
    }

    @RequestMapping("findByTreeId")
    @ApiOperation(value = "查询列表", httpMethod = "POST")
    public String findByTreeId(@ApiParam(name="treeId",value="",required=true) @RequestParam String treeId){
        CoreMemberInfoEntity entity = service.findByTreeId(treeId);
        log.debug("查询成员");
        return returnStringByMap(entity);
    }

    @RequestMapping("save")
    @ApiOperation(value = "保存", httpMethod = "POST")
    public String save(CoreMemberInfoEntity entity){
        if(StringUtils.isNotBlank(entity.getAccount()) && service.checkAccount(entity.getAccount()) > 0){
            return returnFaild("账号已存在");
        }else if(StringUtils.isBlank(entity.getTreeId())){
            return returnFaild("没有找到当前成员的树形编号");
        }
        try {
            if (StringUtils.isBlank(entity.getMemberId())){
                entity.setMemberId(UUID.randomUUID().toString());
                //授权xml同步成员
                guideFileService.createMemberXml("add",entity.getMemberId(),entity.getMemberName());
            }else {
                menuEx.updateGuideByMemChange(entity.getMemberId(),"updateMem");
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
            service.deleteById(primaryId);//删除成员信息
            return returnSuccess();
        }catch (Exception e){
            e.printStackTrace();
            return returnFaild();
        }
    }

}
