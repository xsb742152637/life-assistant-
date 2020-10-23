package com.xie.lifeassistant.model.core.dicinfo.service.impl;

import com.xie.lifeassistant.model.core.dicinfo.dao.CoreDicInfoDao;
import com.xie.lifeassistant.model.core.dicinfo.entity.CoreDicInfoEntity;
import com.xie.lifeassistant.model.core.dicinfo.service.CoreDicInfoDetailService;
import com.xie.lifeassistant.model.core.dicinfo.service.CoreDicInfoService;
import com.xie.lifeassistant.util.datamanage.GenericServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @comment 数据字典
 * @author xie
 * @since 2020-09-28
 */
@Service
public class CoreDicInfoServiceImpl extends GenericServiceImpl<CoreDicInfoDao, CoreDicInfoEntity> implements CoreDicInfoService {

    @Lazy
    @Autowired
    private CoreDicInfoDetailService detService;

    @Override
    @Transactional
    public void deleteById(String primaryId) {
        detService.deleteByDicId(primaryId);
        getBaseMapper().deleteById(primaryId);
    }
}
