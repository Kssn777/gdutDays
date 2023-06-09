package com.lrh.controller;

import com.lrh.entity.Result;
import com.lrh.entity.param.PasswordParam;
import com.lrh.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;

/**
 * @author lrh
 * @description
 * @date 2023/2/24
 */

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/currentUser")
    public Result currentUser(@RequestHeader("Authorization") String token){

        return userService.findUserByToken(token);
    }

    //TODO 根据用户id获取用户信息
    @GetMapping("/{id}")
    public Result getUserInfoById(@PathVariable("id")Long id){
        return userService.getUserInfoById(id);
    }

    @PutMapping("updateUserName/{id}")
    public Result updateUserName(@PathVariable("id")Long id,@RequestBody String newName){
        return userService.updateUserName(id,newName);
    }

    @PutMapping("updatePassword/{id}")
    public Result updatePassword(@PathVariable("id")Long id,@RequestBody PasswordParam passwordParam){
        return userService.updatePassword(id,passwordParam);
    }
}
