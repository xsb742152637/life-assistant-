package com.xie.lifeassistant.model.core.memberarchives.entity;

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
 * @comment 人员档案
 * @author xie
 * @since 2020-10-22
 */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("core_member_archives")
@ApiModel(value="CoreMemberArchivesEntity对象", description="人员档案")
public class CoreMemberArchivesEntity extends Model<CoreMemberArchivesEntity> {

    private static final long serialVersionUID = 1L;

    @TableId("MEMBER_ID")
    private String memberId;

    @ApiModelProperty(value = "头像")
    @TableField("PHOTO")
    private byte[] photo;


    @Override
    protected Serializable pkVal() {
        return this.memberId;
    }

}
