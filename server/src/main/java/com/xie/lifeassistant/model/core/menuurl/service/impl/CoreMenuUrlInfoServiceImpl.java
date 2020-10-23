package com.xie.lifeassistant.model.core.menuurl.service.impl;

import com.xie.lifeassistant.model.core.menuurl.dao.CoreMenuUrlInfoDao;
import com.xie.lifeassistant.model.core.menuurl.entity.CoreMenuUrlInfoEntity;
import com.xie.lifeassistant.model.core.menuurl.service.CoreMenuUrlInfoService;
import com.xie.lifeassistant.util.datamanage.GenericServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @comment 菜单路径列表
 * @author xie
 * @since 2020-05-03
 */
@Service
public class CoreMenuUrlInfoServiceImpl extends GenericServiceImpl<CoreMenuUrlInfoDao, CoreMenuUrlInfoEntity> implements CoreMenuUrlInfoService {

    @Override
    public CoreMenuUrlInfoEntity findByCode(String code){
        return getBaseMapper().findByCode(code);
    }

}
