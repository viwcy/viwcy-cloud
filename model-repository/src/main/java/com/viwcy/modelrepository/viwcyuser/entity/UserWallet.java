package com.viwcy.modelrepository.viwcyuser.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.viwcy.modelrepository.entity.AbstractBaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * TODO //
 *
 * <p> Title: UserWallet </p >
 * <p> Description: UserWallet </p >
 * <p> History: 2020/10/27 14:55 </p >
 * <pre>
 *      Copyright (c) 2020 FQ (fuqiangvn@163.com) , ltd.
 * </pre>
 * Author  FQ
 * Version 0.0.1.RELEASE
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("user_wallet")
public class UserWallet extends AbstractBaseEntity<UserWallet> implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;
    private String userName;
    private Integer isVip;
    private BigDecimal nowBalance;
    private BigDecimal nowIntegral;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
