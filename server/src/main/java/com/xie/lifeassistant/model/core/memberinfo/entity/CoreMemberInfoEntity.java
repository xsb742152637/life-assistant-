package com.xie.lifeassistant.model.core.memberinfo.entity;

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
 * @comment 
 * @author xie
 * @since 2020-10-22
 */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("core_member_info")
@ApiModel(value="CoreMemberInfoEntity对象", description="")
public class CoreMemberInfoEntity extends Model<CoreMemberInfoEntity> {

    private static final long serialVersionUID = 1L;

    @TableId("MEMBER_ID")
    private String memberId;

    @ApiModelProperty(value = "成员名称")
    @TableField("MEMBER_NAME")
    private String memberName;

    @ApiModelProperty(value = "成员类型")
    @TableField("MEMBER_TYPE")
    private Integer memberType;

    @ApiModelProperty(value = "账号")
    @TableField("ACCOUNT")
    private String account;

    @ApiModelProperty(value = "密码")
    @TableField("PASSWORD")
    private String password;

    @ApiModelProperty(value = "成员状态")
    @TableField("MEMBER_STATE")
    private Boolean memberState;

    @ApiModelProperty(value = "树结构ID")
    @TableField("TREE_ID")
    private String treeId;

    @ApiModelProperty(value = "版本")
    @TableField("VERSION")
    private Integer version;


    @Override
    protected Serializable pkVal() {
        return this.memberId;
    }

}
