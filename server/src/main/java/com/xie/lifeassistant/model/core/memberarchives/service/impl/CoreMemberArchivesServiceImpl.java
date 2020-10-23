package com.xie.lifeassistant.model.core.memberarchives.service.impl;

import com.xie.lifeassistant.model.core.memberarchives.dao.CoreMemberArchivesDao;
import com.xie.lifeassistant.model.core.memberarchives.entity.CoreMemberArchivesEntity;
import com.xie.lifeassistant.model.core.memberarchives.service.CoreMemberArchivesService;
import com.xie.lifeassistant.model.core.memberinfo.entity.CoreMemberInfoEntity;
import com.xie.lifeassistant.model.core.memberinfo.service.CoreMemberInfoService;
import com.xie.lifeassistant.util.Util;
import com.xie.lifeassistant.util.datamanage.GenericServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;

/**
 * @comment 人员档案
 * @author xie
 * @since 2020-09-28
 */
@Service
public class CoreMemberArchivesServiceImpl extends GenericServiceImpl<CoreMemberArchivesDao, CoreMemberArchivesEntity> implements CoreMemberArchivesService {

    @Lazy
    @Autowired
    private CoreMemberInfoService memberInfoService;

    @Override
    public BufferedImage getPhoto(String memberId, String text) {
        BufferedInputStream inputimage=null;
        BufferedImage image = null;
        try{
            if(StringUtils.isNotBlank(memberId)){
                CoreMemberArchivesEntity entity = findById(memberId);
                image = Util.BlobAndImage.getImage(entity.getPhoto());

                boolean have=false;
                if(entity != null && entity.getPhoto() != null){
                    try{
                        image = Util.BlobAndImage.getImage(entity.getPhoto());
                        if(image==null){
                            have=true;
                        }else{
                            have=false;
                        }
                    }catch(Exception e){
                        System.out.println(e);
                    }
                }else{
                    have=true;
                }
                /*
                * 如果头像为空，自动生成临时头像！
                * 2016-05-12 xsb
                * */
                if(have){
                    CoreMemberInfoEntity mem = memberInfoService.findById(memberId);
                    //名称
                    if(mem != null && mem.getMemberName() != null){
                        text = mem.getMemberName().toString().substring(0,1);//处理字符串超长问题
                    }else{
                        text="空";
                    }
                    image = createPhoto(text);
                    if(entity == null){
                        entity = new CoreMemberArchivesEntity();
                    }
                    entity.setMemberId(memberId);
                    entity.setPhoto(Util.BlobAndImage.getBytes(image));
                    save(entity);
                }
            }else if(StringUtils.isNotBlank(text)){
                image = createPhoto(text.trim().substring(0,1));
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(inputimage != null){
                try{
                    inputimage.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        return image;
    }

    private BufferedImage createPhoto(String name){
        name = name.toUpperCase();
        //备选背景色
        int[] rC={45,90,70,80,200,200,220,140,110,80,180};
        int[] gC={204,200,140,80,120,120,150,200,150,15,200};
        int[] bC={112,200,200,180,200,120,40,100,160,10,160};
        int i=(int)(Math.random()*10);
        int width=200,height=200;

        BufferedImage image= new BufferedImage(width,height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.clearRect(0, 0, width, height);
        Color c=new Color(rC[i],gC[i],bC[i]);//背景颜色
        g.setColor(c);
        g.fillRect(0, 0, width, height);

        g.setColor(new Color(255,255,255));//字体颜色 白色
        Font font = new Font("微软雅黑", Font.CENTER_BASELINE, 100);
        g.setFont(font);

        //字体居中
        FontRenderContext context = g.getFontRenderContext();
        Rectangle2D bounds = font.getStringBounds(name, context);
        double x = (width - bounds.getWidth()) / 2;
        double y = (height - bounds.getHeight()) / 2;
        double ascent = -bounds.getY();
        double baseY = y + ascent;
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        g.drawString(name, (int) x, (int) baseY);
        return image;
    }

    private String getStr(String str,int len){
        int i=0;
        String newStr="";
        for(String s:str.split("")){
            i+=s.getBytes().length;
            if(i>len){
                newStr+="..";
                break;
            }
            newStr+=s;
        }
        return newStr;
    }

}
