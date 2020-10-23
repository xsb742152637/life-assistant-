package com.xie.lifeassistant.model.core.memberinfo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xie.lifeassistant.model.core.guidefile.service.core.MenuEx;
import com.xie.lifeassistant.model.core.memberarchives.service.CoreMemberArchivesService;
import com.xie.lifeassistant.model.core.memberinfo.dao.CoreMemberInfoDao;
import com.xie.lifeassistant.model.core.memberinfo.entity.CoreMemberInfoEntity;
import com.xie.lifeassistant.model.core.memberinfo.service.CoreMemberInfoService;
import com.xie.lifeassistant.model.core.treeinfo.service.CoreTreeInfoService;
import com.xie.lifeassistant.util.datamanage.GenericServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @comment 
 * @author xie
 * @since 2020-09-28
 */
@Service
public class CoreMemberInfoServiceImpl extends GenericServiceImpl<CoreMemberInfoDao, CoreMemberInfoEntity> implements CoreMemberInfoService {

    @Lazy
    @Autowired
    private CoreTreeInfoService treeService;
    @Lazy
    @Autowired
    private CoreMemberArchivesService memberArchivesService;
    @Lazy
    @Autowired
    private MenuEx menuEx;

    @Override
    public CoreMemberInfoEntity findByTreeId(String treeId) {
        QueryWrapper<CoreMemberInfoEntity> qw = new QueryWrapper<>();
        qw.eq("treeId", treeId);
        return getBaseMapper().selectOne(qw);
    }

    @Override
    public CoreMemberInfoEntity loginCheck(String account, String password) {
        QueryWrapper<CoreMemberInfoEntity> qw = new QueryWrapper<>();
        qw.eq("account", account).eq("password", password);
        return getBaseMapper().selectOne(qw);
    }

    @Override
    public int checkAccount(String account) {
        QueryWrapper<CoreMemberInfoEntity> qw = new QueryWrapper<>();
        qw.eq("account", account);
        return getBaseMapper().selectCount(qw);
    }

    @Override
    @Transactional
    public void deleteById(String primaryId){
        CoreMemberInfoEntity entity = findById(primaryId);

        treeService.deleteById(entity.getTreeId());//删除树形
        memberArchivesService.deleteById(primaryId);//删除成员档案
        menuEx.updateGuideByMemChange(primaryId,"deleteMem");//更新授权信息
        getBaseMapper().deleteById(primaryId);//删除成员
    }

}
