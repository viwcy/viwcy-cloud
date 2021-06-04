package com.viwcy.modelrepository.viwcyuser.param;

import com.viwcy.modelrepository.viwcyuser.entity.UserEntity;
import lombok.Data;

/**
 * @Description TODO
 * @Date 2020/9/1 16:38
 * @Author Fuqiang
 * <p>
 *
 * </p>
 */
@Data
public class UserParam extends UserEntity {

    /**
     * 登录账号(电话或邮箱)
     */
    private String account;
}
