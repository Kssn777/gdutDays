package com.lrh.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lrh.dao.UserDao;
import com.lrh.entity.Result;
import com.lrh.entity.User;
import com.lrh.entity.param.PasswordParam;
import com.lrh.entity.vo.UserVo;
import com.lrh.service.LoginService;
import com.lrh.service.UserService;
import com.lrh.utils.Base64Util;
import com.lrh.utils.ErrorMsg;
import com.lrh.utils.UserThreadLocal;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author lrh
 * @description
 * @date 2023/2/15
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private LoginService loginService;

    @Override
    public User findUser(String account, String password) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<User>();
        wrapper.eq(User::getAccount,account)
                .eq(User::getPassword,password);
        User user = userDao.selectOne(wrapper);
        return user;
    }

    @Override
    public User getUserByAccount(String account){
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<User>();
        wrapper.eq(User::getAccount,account);
        User user = userDao.selectOne(wrapper);
        return user;
    }

    @Override
    public Result findUserByToken(String token) {
        User user = loginService.checkToken(token);
        if(user == null){
            return Result.fail(ErrorMsg.TOKEN_ERROR.getCode(),null,ErrorMsg.TOKEN_ERROR.getMessage());
        }
        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(user,userVo);
        return Result.success(userVo);
    }

    @Override
    public Result getUserInfoById(Long id) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<User>();
        wrapper.eq(User::getId,id);
        User user = userDao.selectOne(wrapper);
        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(user,userVo);
        return Result.success(userVo);
    }

    @Override
    public Result changeAvatar(String avatar) {
        User user = new User();
        user.setId(UserThreadLocal.get().getId());
        user.setAvatar(avatar);
        userDao.updateById(user);
        return Result.success(null);
    }

    @Override
    public Result updateUserName(Long id, String newName) {
        User user = new User();
        user.setId(id);
        user.setUsername(newName);
        userDao.updateById(user);
        return Result.success(newName);
    }

    @Override
    public Result updatePassword(Long id, PasswordParam passwordParam) {
        String oldPassword = passwordParam.getOldPassword();
        String newPassword = passwordParam.getNewPassword();
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<User>();
        wrapper.eq(User::getId,id);
        User userFromDB = userDao.selectOne(wrapper);
        if(!userFromDB.getPassword().equals(Base64Util.decode(oldPassword))){
            return Result.fail(ErrorMsg.PASSWORD_ERROR.getCode(),null,ErrorMsg.PASSWORD_ERROR.getMessage());
        }

        User user = new User();
        user.setId(id);
        user.setPassword(Base64Util.decode(newPassword));
        userDao.updateById(user);
        return Result.success(null);
    }
}
