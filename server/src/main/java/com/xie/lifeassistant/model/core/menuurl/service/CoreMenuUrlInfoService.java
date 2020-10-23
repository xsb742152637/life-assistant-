package com.xie.lifeassistant.model.core.menuurl.service;

import com.xie.lifeassistant.model.core.menuurl.entity.CoreMenuUrlInfoEntity;
import com.xie.lifeassistant.util.datamanage.GenericService;

/**
 * @comment 菜单路径列表
 * @author xie
 * @since 2020-05-03
 */
public interface CoreMenuUrlInfoService extends GenericService<CoreMenuUrlInfoEntity> {
    CoreMenuUrlInfoEntity findByCode(String code);
}
