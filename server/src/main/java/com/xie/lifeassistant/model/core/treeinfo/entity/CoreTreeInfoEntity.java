package com.xie.lifeassistant.model.core.treeinfo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * @comment 树形结构表
 * @author xie
 * @since 2020-10-22
 */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("core_tree_info")
@ApiModel(value="CoreTreeInfoEntity对象", description="树形结构表")
public class CoreTreeInfoEntity extends Model<CoreTreeInfoEntity> {

    private static final long serialVersionUID = 1L;

    @TableId("TREE_ID")
    private String treeId;

    @ApiModelProperty(value = "父id")
    @TableField("PARENT_ID")
    private String parentId;

    @ApiModelProperty(value = "树名称")
    @TableField("TREE_NAME")
    private String treeName;

    @ApiModelProperty(value = "树左值")
    @TableField("TREE_LEFT")
    private Integer treeLeft;

    @ApiModelProperty(value = "树右值")
    @TableField("TREE_RIGHT")
    private Integer treeRight;

    @ApiModelProperty(value = "树类型")
    @TableField("TREE_TYPE")
    private Integer treeType;

    @ApiModelProperty(value = "是否能修改")
    @TableField("CAN_UPDATE")
    private Boolean canUpdate;

    @ApiModelProperty(value = "是否能删除")
    @TableField("CAN_DELETE")
    private Boolean canDelete;


    @Override
    protected Serializable pkVal() {
        return this.treeId;
    }

}
