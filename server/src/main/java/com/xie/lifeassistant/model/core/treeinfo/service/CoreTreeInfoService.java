package com.xie.lifeassistant.model.core.treeinfo.service;

import com.xie.lifeassistant.model.core.treeinfo.entity.CoreTreeInfoEntity;
import com.xie.lifeassistant.util.datamanage.GenericService;

import java.util.List;
import java.util.Map;

/**
 * @comment 树形结构表
 * @author xie
 * @since 2020-09-28
 */
public interface CoreTreeInfoService extends GenericService<CoreTreeInfoEntity> {
    void intoTrees();

    List<Map<String,Object>> getList(String treeType);
    List<Map<String,Object>> getList(String treeType, String parentId, int openLevel);

    void move(String primaryId, Boolean moveOn);
}
