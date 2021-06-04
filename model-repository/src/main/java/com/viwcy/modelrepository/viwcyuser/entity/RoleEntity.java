package com.viwcy.modelrepository.viwcyuser.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.io.Serializable;

/**
 * TODO //
 *
 * <p> Title: RoleEntity </p >
 * <p> Description: RoleEntity </p >
 * <p> History: 2021/6/3 17:37 </p >
 * <pre>
 *      Copyright (c) 2020 FQ (fuqiangvn@163.com) , ltd.
 * </pre>
 * Author  FQ
 * Version 0.0.1.RELEASE
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@TableName("role")
public class RoleEntity implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id;
    private String roleName;
    private String roleNameZh;
}
