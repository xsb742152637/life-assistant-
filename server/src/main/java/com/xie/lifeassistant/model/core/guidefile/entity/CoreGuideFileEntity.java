package com.xie.lifeassistant.model.core.guidefile.entity;

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
 * @comment 授权文件
 * @author xie
 * @since 2020-10-22
 */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("core_guide_file")
@ApiModel(value="CoreGuideFileEntity对象", description="授权文件")
public class CoreGuideFileEntity extends Model<CoreGuideFileEntity> {

    private static final long serialVersionUID = 1L;

    @TableId("GUIDE_ID")
    private String guideId;

    @ApiModelProperty(value = "授权xml文件")
    @TableField("DOCUMENT")
    private String document;


    @Override
    protected Serializable pkVal() {
        return this.guideId;
    }

}
