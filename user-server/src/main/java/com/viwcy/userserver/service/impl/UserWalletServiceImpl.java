package com.viwcy.userserver.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.viwcy.modelrepository.viwcyuser.entity.UserWallet;
import com.viwcy.modelrepository.viwcyuser.mapper.UserWalletMapper;
import com.viwcy.userserver.service.UserWalletService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * TODO //
 *
 * <p> Title: UserWalletServiceImpl </p >
 * <p> Description: UserWalletServiceImpl </p >
 * <p> History: 2020/10/27 14:58 </p >
 * <pre>
 *      Copyright (c) 2020 FQ (fuqiangvn@163.com) , ltd.
 * </pre>
 * Author  FQ
 * Version 0.0.1.RELEASE
 */
@Service
@Slf4j
public class UserWalletServiceImpl extends ServiceImpl<UserWalletMapper, UserWallet> implements UserWalletService {
}
