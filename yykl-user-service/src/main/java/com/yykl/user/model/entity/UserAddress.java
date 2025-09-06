// UserAddress.java
package com.yykl.user.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 用户收货地址表实体类
 */
@Data
@TableName("user_address")
@Schema(description = "用户收货地址实体类，存储用户的收货地址信息")
public class UserAddress {
    @TableId(type = IdType.AUTO)
    @Schema(description = "主键ID，地址记录唯一标识，自增", example = "1")
    private Long id;

    /** 用户ID */
    @Schema(description = "用户ID，关联用户表的主键", example = "1")
    private Long userId;

    /** 收货人 */
    @Schema(description = "收货人姓名", required = true, example = "张三")
    private String receiver;

    /** 联系电话 */
    @Schema(description = "收货人联系电话", required = true, example = "13800138000")
    private String phone;

    /** 省份 */
    @Schema(description = "收货地址省份", required = true, example = "广东省")
    private String province;

    /** 城市 */
    @Schema(description = "收货地址城市", required = true, example = "深圳市")
    private String city;

    /** 区县 */
    @Schema(description = "收货地址区县", required = true, example = "南山区")
    private String district;

    /** 详细地址 */
    @Schema(description = "详细收货地址", required = true, example = "科技园路100号")
    private String detailAddress;

    /** 是否默认地址 */
    @Schema(description = "是否为默认地址，true表示是，false表示否", example = "true")
    private Boolean isDefault;
}