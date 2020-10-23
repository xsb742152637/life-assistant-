package com.xie.lifeassistant.model.core.menuurl.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.sql.Timestamp;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * @comment 菜单路径列表
 * @author xie
 * @since 2020-10-22
 */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("core_menu_url_info")
@ApiModel(value="CoreMenuUrlInfoEntity对象", description="菜单路径列表")
public class CoreMenuUrlInfoEntity extends Model<CoreMenuUrlInfoEntity> {

    private static final long serialVersionUID = 1L;

    @TableId("URL_ID")
    private String urlId;

    @ApiModelProperty(value = "菜单名称")
    @TableField("TITLE")
    private String title;

    @ApiModelProperty(value = "编码")
    @TableField("CODE")
    private String code;

    @ApiModelProperty(value = "路径")
    @TableField("URL")
    private String url;

    @ApiModelProperty(value = "参数")
    @TableField("PARAMETER")
    private String parameter;

    @TableField("SYS_TIME")
    private Timestamp sysTime;


    @Override
    protected Serializable pkVal() {
        return this.urlId;
    }

}
