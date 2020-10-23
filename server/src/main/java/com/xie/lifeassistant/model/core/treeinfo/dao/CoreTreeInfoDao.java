package com.xie.lifeassistant.model.core.treeinfo.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xie.lifeassistant.model.core.treeinfo.entity.CoreTreeInfoEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @comment 树形结构表
 * @author xie
 * @since 2020-09-28
 */

public interface CoreTreeInfoDao  extends BaseMapper<CoreTreeInfoEntity> {
    List<CoreTreeInfoEntity> getMainInfo(@Param("treeType") String treeType, @Param("parentId") String parentId);

    List<CoreTreeInfoEntity> findRoots();
    List<CoreTreeInfoEntity> findSons(@Param("primaryId") String primaryId);
    CoreTreeInfoEntity findNeighborEntity(@Param("entity") CoreTreeInfoEntity entity, @Param("moveOn") Boolean moveOn);

    int insert_updateBefore(@Param("treeType") Integer treeType, @Param("left") Integer left, @Param("right") Integer right);
    int insert_updateAfter(@Param("treeType") Integer treeType,@Param("left") Integer left);

    int delete(@Param("primaryId") String primaryId);
    int delete_updateBefor(@Param("treeType") Integer treeType,@Param("left") Integer left, @Param("right") Integer right, @Param("sonCount") Integer sonCount);
    int delete_updateAfter(@Param("treeType") Integer treeType,@Param("left") Integer left, @Param("sonCount") Integer sonCount);

}
