package com.viwcy.userserver.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.viwcy.basecommon.common.ResultEntity;
import com.viwcy.modelrepository.viwcyuser.entity.UserEntity;
import com.viwcy.modelrepository.viwcyuser.param.UserParam;

/**
 * @Description TODO
 * @Date 2020/9/1 16:35
 * @Author Fuqiang
 * <p>
 *
 * </p>
 */
public interface UserService extends IService<UserEntity> {

    /**
     * @param param
     * @return void
     * @Description TODO    新增用户(注册)
     * @date 2020/9/1 16:38
     * @author Fuqiang
     */
    boolean register(UserParam param);

    /**
     * @param param
     * @return java.lang.String
     * @Description TODO    账号密码登录
     * @date 2020/9/2 9:22
     * @author Fuqiang
     */
    JSONObject login(UserParam param);

    /**
     * @param phone
     * @return com.fuqiang.basecommons.common.ResultEntity
     * @Description TODO    发送短信验证码
     * @date 2020/12/2 15:51
     * @author Fuqiang
     */
    boolean sendSMSCode(String phone);

    /**
     * @param phone
     * @param code
     * @return boolean
     * @Description TODO    短信登录
     * @date 2020/12/2 16:02
     * @author Fuqiang
     */
    ResultEntity SMSLogin(String phone, String code);

    /**
     * @param param
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page
     * @Description TODO    分页查询user信息
     * @date 2020/9/5 17:16
     * @author Fuqiang
     */
    PageInfo queryPageUser(UserParam param);
}
