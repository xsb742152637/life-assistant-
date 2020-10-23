package com.xie.lifeassistant.model.core.menuurl.dao;

import com.xie.lifeassistant.model.core.menuurl.entity.CoreMenuUrlInfoEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @comment 菜单路径列表
 * @author xie
 * @since 2020-05-03
 */

public interface CoreMenuUrlInfoDao extends BaseMapper<CoreMenuUrlInfoEntity> {

    @Select("select * from core_menu_url_info where code = #{code} limit 0,1")
    CoreMenuUrlInfoEntity findByCode(@Param("code") String code);

}
