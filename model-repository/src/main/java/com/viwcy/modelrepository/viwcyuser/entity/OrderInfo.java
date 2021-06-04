package com.viwcy.modelrepository.viwcyuser.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.viwcy.modelrepository.entity.AbstractBaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * TODO //、、支付订单实体类
 *
 * <p> Title: OrderInfo </p >
 * <p> Description: OrderInfo </p >
 * <p> History: 2020/9/21 16:18 </p >
 * <pre>
 *      Copyright (c) 2020 FQ (fuqiangvn@163.com) , ltd.
 * </pre>
 * Author  FQ
 * Version 0.0.1.RELEASE
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderInfo extends AbstractBaseEntity<OrderInfo> implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 订单编号，唯一
     */
    private String orderNo;
    /**
     * 订单总金额。单位元
     */
    private BigDecimal orderAmount;
    /**
     * 订单状态。1待付款，2已付款(待发货)，3已发货(商家收到付款订单，生成物流订单)，4已关闭(订单超时)，5已取消(手动取消订单)，6已完成(确认收货，该状态下的订单不支持退款操作)，7退款中，8退款成功，9退款失败
     */
    private Integer orderStatus;
    /**
     * 收货人名称
     */
    private String consigneeName;
    /**
     * 收货人电话
     */
    private String consigneePhone;
    /**
     * 收货地址
     */
    private String consigneeAddress;
    /**
     * 订单备注
     */
    private String orderRemark;

    private Long createUser;
    private Long updateUser;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
