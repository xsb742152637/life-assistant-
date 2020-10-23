package com.xie.lifeassistant.model.core.menutree.service;

import com.xie.lifeassistant.model.core.menutree.entity.CoreMenuTreeInfoEntity;
import com.xie.lifeassistant.util.datamanage.GenericService;

import java.util.List;
import java.util.Map;

/**
 * @comment 树形菜单列表
 * @author xie
 * @since 2020-09-28
 */
public interface CoreMenuTreeInfoService extends GenericService<CoreMenuTreeInfoEntity> {
    /**
     * 得到全部显示的菜单
     * @return
     */
    List<Map<String,Object>> getMenuTree(Boolean needGuide, Boolean isTop, Boolean isShow);

    CoreMenuTreeInfoEntity findByCode(String code);

    void moveTree(String treeId,boolean type);

    /**
     * //根据上级，得到子级下一个排序数字
     * @param outlineLevel
     * @return
     */
    Integer getMenuLevelByParLevel(String outlineLevel);

    int updateAfterDelete(String before,String after);

    void delete(String primaryId);
}
