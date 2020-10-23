package com.xie.lifeassistant.model.core.dicinfo.entity;

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
 * @comment 数据字典
 * @author xie
 * @since 2020-10-22
 */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("core_dic_info")
@ApiModel(value="CoreDicInfoEntity对象", description="数据字典")
public class CoreDicInfoEntity extends Model<CoreDicInfoEntity> {

    private static final long serialVersionUID = 1L;

    @TableId("DIC_ID")
    private String dicId;

    @ApiModelProperty(value = "字典类型")
    @TableField("DIC_NAME")
    private String dicName;

    @ApiModelProperty(value = "字典编码")
    @TableField("DIC_CODE")
    private String dicCode;

    @ApiModelProperty(value = "字典描述")
    @TableField("DIC_DES")
    private String dicDes;

    @ApiModelProperty(value = "编制时间")
    @TableField("SYS_TIME")
    private Timestamp sysTime;

    @ApiModelProperty(value = "编制人，对应core_member_info表主键")
    @TableField("MEMBER_ID")
    private String memberId;


    @Override
    protected Serializable pkVal() {
        return this.dicId;
    }

}
