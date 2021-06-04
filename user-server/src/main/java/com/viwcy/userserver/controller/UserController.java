package com.viwcy.userserver.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.viwcy.basecommon.common.BaseController;
import com.viwcy.basecommon.common.ResultEntity;
import com.viwcy.modelrepository.viwcyuser.param.UserParam;
import com.viwcy.userserver.service.UserService;
import io.seata.spring.annotation.GlobalTransactional;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Description TODO
 * @Date 2020/9/1 16:50
 * @Author Fuqiang
 * <p>
 *
 * </p>
 */
@RestController
@RequestMapping("/user")
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

    /**
     * @param param
     * @return com.fuqiang.viwcyuser.constant.ResultEntity
     * @Description TODO    新增用户信息(注册)
     * @date 2020/9/1 16:52
     * @author Fuqiang
     */
    @PostMapping("/register")
    public ResultEntity save(@RequestBody UserParam param) {
        return userService.register(param) ? success("恭喜您，账号注册成功") : fail("很遗憾，账号注册失败");
    }

    /**
     * @param param
     * @return com.fuqiang.viwcyuser.constant.ResultEntity
     * @Description TODO    账号密码登录
     * @date 2020/9/2 9:37
     * @author Fuqiang
     */
    @PostMapping("/login")
    @HystrixCommand //必须加该注解，否则监控无效
    @GlobalTransactional(rollbackFor = Exception.class)
    public ResultEntity login(@RequestBody UserParam param) {
        if (StringUtils.isBlank(param.getAccount())) {
            return hint("账号不能为空");
        }
        if (StringUtils.isBlank(param.getPassword())) {
            return hint("密码不能为空");
        }
        return success(userService.login(param));
    }

    /**
     * @param phone
     * @return com.fuqiang.basecommons.common.ResultEntity<?>
     * @Description TODO    发送短信验证码
     * @date 2020/12/2 15:55
     * @author Fuqiang
     */
    @PostMapping("/sendSMSCode")
    public ResultEntity<?> sendSMSCode(@RequestParam("phone") String phone) {
        if (StringUtils.isBlank(phone)) {
            return fail("手机号不能为空");
        }
        boolean send = userService.sendSMSCode(phone);
        return send ? success("短信发送成功") : fail("短信发送失败");
    }

    /**
     * @param phone
     * @param code
     * @return com.fuqiang.basecommons.common.ResultEntity<?>
     * @Description TODO    短信验证码登录
     * @date 2020/12/2 16:26
     * @author Fuqiang
     */
    @PostMapping("/SMSLogin")
    public ResultEntity<?> SMSLogin(@RequestParam("phone") String phone, @RequestParam("code") String code) {
        if (StringUtils.isBlank(phone)) {
            return fail("手机号不能为空");
        }
        if (StringUtils.isBlank(code)) {
            return fail("验证码不能为空");
        }
        return userService.SMSLogin(phone, code);
    }

    /**
     * @param param
     * @return com.fuqiang.basecommons.constant.ResultEntity
     * @Description TODO    分页查询user信息
     * @date 2020/9/5 17:28
     * @author Fuqiang
     */
    @PostMapping("/queryPage")
    public ResultEntity queryPageUser(@RequestBody UserParam param) {
        return success(userService.queryPageUser(param));
    }
}
