package com.viwcy.modelrepository.viwcyuser.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.viwcy.modelrepository.entity.AbstractBaseEntity;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * TODO //
 *
 * <p> Title: UserArticle </p >
 * <p> Description: UserArticle </p >
 * <p> History: 2021/5/19 17:00 </p >
 * <pre>
 *      Copyright (c) 2020 FQ (fuqiangvn@163.com) , ltd.
 * </pre>
 * Author  FQ
 * Version 0.0.1.RELEASE
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode
@Accessors(chain = true)
@ToString
@TableName("user_article")
public class UserArticle extends AbstractBaseEntity<UserArticle> implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private String title;
    private String content;
    private Long createId;
    private String createName;
    private Long updateId;
    private String updateName;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
