package com.xie.lifeassistant.model.core.dicinfo.entity;

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
 * @comment 数据字典明细表
 * @author xie
 * @since 2020-10-22
 */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("core_dic_info_detail")
@ApiModel(value="CoreDicInfoDetailEntity对象", description="数据字典明细表")
public class CoreDicInfoDetailEntity extends Model<CoreDicInfoDetailEntity> {

    private static final long serialVersionUID = 1L;

    @TableId("DETAIL_ID")
    private String detailId;

    @TableField("DIC_ID")
    private String dicId;

    @ApiModelProperty(value = "详细类型名称")
    @TableField("DETAIL_NAME")
    private String detailName;

    @TableField("DETAIL_CODE")
    private String detailCode;

    @ApiModelProperty(value = "值")
    @TableField("DETAIL_VALUE")
    private String detailValue;

    @ApiModelProperty(value = "顺序")
    @TableField("DETAIL_LEVEL")
    private Integer detailLevel;

    @ApiModelProperty(value = "备注")
    @TableField("COMMENT")
    private String comment;

    @ApiModelProperty(value = "是否有效")
    @TableField("IS_VALID")
    private Boolean isValid;


    @Override
    protected Serializable pkVal() {
        return this.detailId;
    }

}
