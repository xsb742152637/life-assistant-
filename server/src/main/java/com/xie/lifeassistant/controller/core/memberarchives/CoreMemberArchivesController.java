package com.xie.lifeassistant.controller.core.memberarchives;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import com.xie.lifeassistant.model.core.memberarchives.entity.CoreMemberArchivesEntity;
import com.xie.lifeassistant.model.core.memberarchives.service.CoreMemberArchivesService;
import com.xie.lifeassistant.util.Util;
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

import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.OutputStream;

/**
 * @comment 人员档案
 * @author xie
 * @since 2020-09-28
 */
@RestController
@RequestMapping("/core/memberarchives")
@Api(tags={"人员档案"})
public class CoreMemberArchivesController extends GenericController<CoreMemberArchivesEntity> {

    @Lazy
    @Autowired
    private CoreMemberArchivesService service;

    /**
     *
     * memberId 成员编号，获取成员头像，如果没有则自动新增
     * text 图片内容文字，如果只传入该参数，则表示临时生成一个图片
     */
    @RequestMapping("getPhoto")
    @ApiOperation(value = "获取成员头像图片", httpMethod = "POST")
    public void getPhoto(@ApiParam(name="memberId",value="成员ID") @RequestParam String memberId,
                         @ApiParam(name="text",value="图片内容文字，如果只传入该参数，则表示临时生成一个图片") @RequestParam String text,
                         HttpServletResponse response){

        response.setContentType("image/png");
        if(StringUtils.isBlank(memberId) && StringUtils.isBlank(text)){
            //memberId = Context.getMember().getMemberId().toString();
            memberId = "22222222-2222-2222-2222-222222222222";
        }
        BufferedImage image = service.getPhoto(memberId,text);
        if(image!=null){
            OutputStream sos = null;
            try{
                sos = response.getOutputStream();
                JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(sos);
                encoder.encode(image);
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                try{
                    if(sos != null){
                        sos.flush();
                        sos.close();
                    }
                }catch (Exception e){

                }
            }
        }
    }

    @RequestMapping("getPhotoByBase64")
    @ApiOperation(value = "获取成员头像图片", httpMethod = "POST")
    public String getPhotoByBase64(@ApiParam(name="memberId",value="成员ID") @RequestParam String memberId,
                           @ApiParam(name="text",value="图片内容文字，如果只传入该参数，则表示临时生成一个图片") @RequestParam String text,
                           HttpServletResponse response){

        if(StringUtils.isBlank(memberId) && StringUtils.isBlank(text)){
            memberId = "22222222-2222-2222-2222-222222222222";
        }
        BufferedImage image = service.getPhoto(memberId,text);
        if(image!=null){
            return Util.BlobAndImage.getBase64(Util.BlobAndImage.getBlob(image));
        }
        return "";
    }

    @RequestMapping("save")
    @ApiOperation(value = "保存", httpMethod = "POST")
    public String save(@ApiParam(name="memberId",value="成员ID",required=true) @RequestParam String memberId,
                       @ApiParam(name="photoUrl",value="图片",required=true) @RequestParam String photoUrl){

        if(StringUtils.isBlank(memberId)){
            return returnFaild("没有正确接收到参数");
        }

        try {
            CoreMemberArchivesEntity entity = service.findById(memberId);
            if(entity == null){
                entity = new CoreMemberArchivesEntity();
                entity.setMemberId(memberId);
            }
            entity.setPhoto(Util.BlobAndImage.getBytesByPath(photoUrl));
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
