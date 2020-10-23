package com.xie.lifeassistant.model.core.guidefile.service;

import com.xie.lifeassistant.model.core.guidefile.entity.CoreGuideFileEntity;
import com.xie.lifeassistant.util.datamanage.GenericService;
import org.dom4j.Document;

import java.util.List;

/**
 * @comment 授权文件
 * @author xie
 * @since 2020-09-28
 */
public interface CoreGuideFileService extends GenericService<CoreGuideFileEntity> {
    String getXmlStr(String primaryId);

    CoreGuideFileEntity save(String projectId, String str);
    CoreGuideFileEntity save(String projectId, Document d);
    void save(List<CoreGuideFileEntity> list);
    void deleteMenu(String menuId);

    String createMemberXml(String sourceType,String memberId,String memberName);
    void addMenu(String parId,String menuId,String menuName);
}
