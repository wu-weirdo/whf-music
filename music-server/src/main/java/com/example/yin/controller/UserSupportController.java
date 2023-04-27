package com.example.yin.controller;

import com.example.yin.common.R;
import com.example.yin.model.request.UserSupportRequest;
import com.example.yin.service.UserSupportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author 祝英台炸油条
 * @Time : 2022/6/11 16:07
 **/
@RestController
@RequestMapping("/userSupport")
public class UserSupportController {

    @Autowired
    UserSupportService userSupportService;

    @PostMapping("/test")
    public R<Boolean> isUserSupportComment(@RequestBody UserSupportRequest request) {
        return R.success(userSupportService.isUserSupportComment(request));
    }

    @PostMapping("/insert")
    public R<Boolean> insertCommentSupport(@RequestBody UserSupportRequest request) {
        return R.success(userSupportService.insertCommentSupport(request));
    }

    @PostMapping("/delete")
    public R<Boolean> deleteCommentSupport(@RequestBody UserSupportRequest request) {
        return R.success(userSupportService.deleteCommentSupport(request));
    }
}
