package com.example.yin.controller;

import com.example.yin.common.R;
import com.example.yin.model.request.UserRequest;
import com.example.yin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 返回所有用户
     */
    @GetMapping("/user")
    public R allUser() {
        return userService.allUser();
    }


    /**
     * 返回指定 ID 的用户
     */
    @GetMapping("/user/detail")
    public R userOfId(@RequestParam int id) {
        return userService.userOfId(id);
    }

    /**
     * 删除用户
     */
    @GetMapping("/user/delete")
    public R deleteUser(@RequestParam int id) {
        return userService.deleteUser(id);
    }

    /**
     * 更新用户信息
     */
    @PostMapping("/user/update")
    public R updateUserMsg(@RequestBody UserRequest updateRequest) {
        return userService.updateUserMsg(updateRequest);
    }

    /**
     * 更新用户密码
     */
    @PostMapping("/user/updatePassword")
    public R updatePassword(@RequestBody UserRequest updatePasswordRequest) {
        return userService.updatePassword(updatePasswordRequest);
    }

    /**
     * 更新用户头像
     */
    @PostMapping("/user/avatar/update")
    public R updateUserPic(@RequestParam("file") MultipartFile avatorFile, @RequestParam("id") int id) {
        return userService.updateUserAvator(avatorFile, id);
    }
}
