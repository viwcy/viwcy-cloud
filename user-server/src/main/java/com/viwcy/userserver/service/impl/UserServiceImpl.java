package com.viwcy.userserver.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.viwcy.basecommon.common.ResultEntity;
import com.viwcy.basecommon.constant.GlobalRedisPrefix;
import com.viwcy.basecommon.exception.BusinessException;
import com.viwcy.basecommon.util.JwtUtil;
import com.viwcy.modelrepository.viwcyuser.entity.UserEntity;
import com.viwcy.modelrepository.viwcyuser.entity.UserWallet;
import com.viwcy.modelrepository.viwcyuser.mapper.UserMapper;
import com.viwcy.modelrepository.viwcyuser.param.UserParam;
import com.viwcy.userserver.constant.RedisPrefix;
import com.viwcy.userserver.service.UserService;
import com.viwcy.userserver.service.UserWalletService;
import com.viwcy.userserver.util.AliYunMessageUtil;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @Description TODO
 * @Date 2020/9/1 16:35
 * @Author Fuqiang
 * <p>
 *
 * </p>
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, UserEntity> implements UserService {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserWalletService userWalletService;

    @Autowired
    private AliYunMessageUtil aliYunMessageUtil;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private RedissonClient redissonClient;

    /**
     * @param param
     * @return void
     * @Description TODO    新增用户信息(注册)
     * @date 2020/9/1 16:50
     * @author Fuqiang
     */
    @Override
    @GlobalTransactional(rollbackFor = Exception.class)
    public boolean register(UserParam param) {
        log.info("新增用户信息，入参 = [{}]", JSONObject.toJSONString(param));

        RLock lock = redissonClient.getLock("user_register_lock_");
        try {
            lock.lock();
            UserEntity nickname = this.getOne(new QueryWrapper<UserEntity>().eq("user_name", param.getUserName()));
            if (Optional.ofNullable(nickname).isPresent()) {
                throw new BusinessException("该用户名已被占用，请重新输入");
            }
            UserEntity phone = this.getOne(new QueryWrapper<UserEntity>().eq("phone", param.getPhone()));
            if (Optional.ofNullable(phone).isPresent()) {
                throw new BusinessException("该手机号已被注册，请重新输入");
            }
            UserEntity email = this.getOne(new QueryWrapper<UserEntity>().eq("email", param.getEmail()));
            if (Optional.ofNullable(email).isPresent()) {
                throw new BusinessException("该邮箱已被注册，请重新输入");
            }
            UserEntity userEntity = new UserEntity();
            //security加密存储，对于同一个明文，每次加密之后的密文都是不同的。
            param.setPassword(encoder.encode(param.getPassword()));
            BeanUtils.copyProperties(param, userEntity);
            boolean saveUser = this.save(userEntity);
            if (saveUser) {
                log.info("用户注册成功，用户信息 = [{}]", JSON.toJSONString(userEntity));
            } else {
                throw new BusinessException("用户注册失败");
            }

            UserWallet userWallet = UserWallet.builder().userId(userEntity.getId()).userName(userEntity.getUserName()).build();
            boolean saveWallet = userWalletService.save(userWallet);
            if (saveWallet) {
                log.info("用户钱包保存成功");
            } else {
                throw new BusinessException("用户钱包数据保存失败");
            }
            //异步发送短信，邮件
//        ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 2, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
        } finally {
            lock.unlock();
        }
        return true;
    }

    /**
     * @param param
     * @return java.lang.String
     * @Description TODO    账号密码登录
     * @date 2020/9/2 9:22
     * @author Fuqiang
     */
    @Override
    public JSONObject login(UserParam param) {
        log.info("用户登录，入参 = [{}]", JSON.toJSONString(param));
        UserEntity userEntity = this.getOne(new QueryWrapper<UserEntity>().and(wrapper -> wrapper.eq("phone", param.getAccount()).or().eq("email", param.getAccount())));
        if (!Optional.ofNullable(userEntity).isPresent()) {
            throw new BusinessException("账号信息不存在，请先注册再进行登录操作");
        }
        //security密码匹配
        if (!encoder.matches(param.getPassword(), userEntity.getPassword())) {
            throw new BusinessException("账号或密码错误，请重新输入");
        }
        Map map = JSON.parseObject(JSON.toJSONString(userEntity), Map.class);
        JSONObject jsonObject = jwtUtil.createJwt(map, userEntity.getId().toString(), 30);
        jsonObject.put("user", userEntity);
        log.info("用户登录成功，颁发JWT = [{}]", jsonObject);
        //、、加入redis，便于以后的令牌吊销
//            redisTemplate.opsForValue().set(LOGIN_USER_PREFIX + userEntity.getId(), jsonObject.get("token").toString(), 30, TimeUnit.SECONDS);
        //TODO  RSA密钥删除(非必须)
        return jsonObject;
    }

    /**
     * @param phone
     * @return com.fuqiang.basecommons.common.ResultEntity
     * @Description TODO    发送短信验证码
     * @date 2020/12/2 15:51
     * @author Fuqiang
     */
    @Override
    public boolean sendSMSCode(String phone) {
        String code = redisTemplate.opsForValue().get(GlobalRedisPrefix.USER_SERVER + RedisPrefix.LOGIN_CODE + phone);
        if (StringUtils.isNotBlank(code)) {
            Long expire = redisTemplate.getExpire(GlobalRedisPrefix.USER_SERVER + RedisPrefix.LOGIN_CODE + phone, TimeUnit.SECONDS);
            throw new BusinessException("验证码发送太过频繁，请在" + expire + "秒后再尝试发送");
        }
        UserEntity userEntity = this.getOne(new QueryWrapper<UserEntity>().eq("phone", phone).last("limit 1"));
        if (ObjectUtils.isEmpty(userEntity)) {
            throw new BusinessException("用户信息不存在，请先注册之后，再尝试发送验证码和登录操作");
        }
        try {
            SendSmsResponse sendSmsResponse = aliYunMessageUtil.loginSend(phone);
            if (sendSmsResponse.getCode() != null && sendSmsResponse.getCode().equals("OK")) {
                return true;
            }
            return false;
        } catch (ClientException e) {
            e.printStackTrace();
            log.error("短信发送异常，原因：{}", e.getMessage());
        }
        return false;
    }

    /**
     * @param phone
     * @param code
     * @return boolean
     * @Description TODO    短信登录
     * @date 2020/12/2 16:02
     * @author Fuqiang
     */
    @Override
    public ResultEntity SMSLogin(String phone, String code) {
        String redisCode = redisTemplate.opsForValue().get(GlobalRedisPrefix.USER_SERVER + RedisPrefix.LOGIN_CODE + phone);
        if (StringUtils.isBlank(redisCode)) {
            throw new BusinessException("未发送验证码或验证码已过期，请发送验证码之后，再进行登录操作");
        }
        if (!redisCode.equals(code)) {
            throw new BusinessException("验证码输入有误，登录失败");
        }
        UserEntity userEntity = this.getOne(new QueryWrapper<UserEntity>().eq("phone", phone));
        if (ObjectUtils.isEmpty(userEntity)) {
            throw new BusinessException("用户信息不存在，登录失败");
        }
        Map map = JSON.parseObject(JSON.toJSONString(userEntity), Map.class);
        JSONObject jsonObject = jwtUtil.createJwt(map, userEntity.getId().toString(), 30);
        jsonObject.put("user", userEntity);
        log.info("用户登录成功，颁发JWT = [{}]", jsonObject);
        return ResultEntity.success(jsonObject);
    }

    /**
     * @param param
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page
     * @Description TODO    分页查询user信息
     * @date 2020/9/5 17:25
     * @author Fuqiang
     */
    @Override
    public PageInfo queryPageUser(UserParam param) {
        PageHelper.startPage(param.getPageNum(), param.getPageSize());
        return new PageInfo(this.baseMapper.selectList(new QueryWrapper<>()));
    }
}
