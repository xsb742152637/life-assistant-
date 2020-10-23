package com.xie.lifeassistant.model.core.memberarchives.service;

import com.xie.lifeassistant.model.core.memberarchives.entity.CoreMemberArchivesEntity;
import com.xie.lifeassistant.util.datamanage.GenericService;

import java.awt.image.BufferedImage;

/**
 * @comment 人员档案
 * @author xie
 * @since 2020-09-28
 */
public interface CoreMemberArchivesService extends GenericService<CoreMemberArchivesEntity> {
    /**
     *
     * memberId 成员编号，获取成员头像，如果没有则自动新增
     * text 图片内容文字，如果只传入该参数，则表示临时生成一个图片
     */
    BufferedImage getPhoto(String memberId, String text);

}
