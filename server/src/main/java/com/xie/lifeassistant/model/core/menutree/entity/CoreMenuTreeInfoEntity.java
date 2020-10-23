package com.xie.lifeassistant.model.core.menutree.entity;

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
 * @comment 树形菜单列表
 * @author xie
 * @since 2020-10-22
 */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("core_menu_tree_info")
@ApiModel(value="CoreMenuTreeInfoEntity对象", description="树形菜单列表")
public class CoreMenuTreeInfoEntity extends Model<CoreMenuTreeInfoEntity> {

    private static final long serialVersionUID = 1L;

    @TableId("MENU_ID")
    private String menuId;

    @ApiModelProperty(value = "同级顺序")
    @TableField("MENU_LEVEL")
    private Integer menuLevel;

    @ApiModelProperty(value = "大纲序号")
    @TableField("OUTLINE_LEVEL")
    private String outlineLevel;

    @ApiModelProperty(value = "菜单标题")
    @TableField("TITLE")
    private String title;

    @ApiModelProperty(value = "菜单路径,对应core_menu_url_info表主键")
    @TableField("URL_ID")
    private String urlId;

    @ApiModelProperty(value = "菜单图标")
    @TableField("ICON")
    private String icon;

    @ApiModelProperty(value = "是否为应用")
    @TableField("TYPE")
    private Boolean type;

    @ApiModelProperty(value = "是否显示")
    @TableField("IS_SHOW")
    private Boolean isShow;


    @Override
    protected Serializable pkVal() {
        return this.menuId;
    }

}
