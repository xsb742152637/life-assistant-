package com.xie.lifeassistant.model.core.memberinfo.service;

import com.xie.lifeassistant.model.core.memberinfo.entity.CoreMemberInfoEntity;
import com.xie.lifeassistant.util.datamanage.GenericService;

/**
 * @comment 
 * @author xie
 * @since 2020-09-28
 */
public interface CoreMemberInfoService extends GenericService<CoreMemberInfoEntity> {

    /**
     * 根据用户名和密码验证登录是否成功
     * @param account
     * @param password
     * @return
     */
    CoreMemberInfoEntity loginCheck(String account, String password);

    /**
     * 用于判断账号是否存在
     * @param account
     * @return
     */
    int checkAccount(String account);

    CoreMemberInfoEntity findByTreeId(String treeId);
}
