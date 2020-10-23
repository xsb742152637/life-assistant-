package com.xie.lifeassistant.model.core.menutree.dao;

import com.xie.lifeassistant.model.core.menutree.entity.CoreMenuTreeInfoEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

/**
 * @comment 树形菜单列表
 * @author xie
 * @since 2020-09-28
 */

public interface CoreMenuTreeInfoDao extends BaseMapper<CoreMenuTreeInfoEntity> {

    @Select("select a.* from core_menu_tree_info a join core_menu_url_info b on a.URL_ID=b.URL_ID where b.CODE = #{code} limit 0,1")
    CoreMenuTreeInfoEntity findByCode(@Param("code") String code);

    @Select("select a.* from core_menu_tree_info a where a.OUTLINE_LEVEL = #{outlineLevel} limit 0,1")
    CoreMenuTreeInfoEntity findOneByOutlineLevel(@Param("outlineLevel") String outlineLevel);

    //下面的sql在xml文件中

    Integer getMenuLevelByParLevel(@Param("outlineLevel") String outlineLevel);

    void moveChildren(@Param("oldParen")String oldParen,@Param("newParen")String newParen);


    //当参数需要在if等标签中作为条件进行判断时，需要在此用@Param指定
    List<Map<String,Object>> getMenuTree(@Param(value = "isShow") Boolean isShow);
    int updateAfterDelete(@Param("before") String before,@Param("after") String after);

}
