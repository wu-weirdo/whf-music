package com.example.yin.controller;

import com.example.yin.common.R;
import com.example.yin.model.domain.User;
import com.example.yin.model.request.UserRequest;
import com.example.yin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 返回所有用户
     */
    @GetMapping("/list")
    public R<List<User>> allUser() {
        return R.success(userService.list());
    }


    /**
     * 返回指定 ID 的用户
     */
    @GetMapping("/detail")
    public R<User> userOfId(@RequestParam Integer id) {
        return R.success(userService.getById(id));
    }

    /**
     * 删除用户
     */
    @GetMapping("/delete")
    public R<Boolean> deleteUser(@RequestParam Integer id) {
        return R.success(userService.removeById(id));
    }

    /**
     * 更新用户信息
     */
    @PostMapping("/update")
    public R<Boolean> updateUserMsg(@RequestBody UserRequest updateRequest) {
        Boolean result = userService.updateUserMsg(updateRequest);
        return R.success(result, result ? "更新信息成功！" : "更新信息失败，请重试！");
    }

    /**
     * 更新用户密码
     */
    @PostMapping("/updatePassword")
    public R<Boolean> updatePassword(@RequestBody UserRequest updatePasswordRequest) {
        Boolean result = userService.updatePassword(updatePasswordRequest);
        return R.success(result, result ? "更新密码成功！" : "更新密码失败，请重试！");
    }

    /**
     * 更新用户头像
     */
    @PostMapping("/avatar/update")
    public R<String> updateUserPic(@RequestParam("file") MultipartFile avatorFile, @RequestParam("id") int id) {
        return R.success(userService.updateUserAvator(avatorFile, id), "更新成功");
    }
}
