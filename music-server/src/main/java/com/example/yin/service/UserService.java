package com.example.yin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.yin.common.R;
import com.example.yin.model.domain.User;
import com.example.yin.model.request.UserRequest;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;

public interface UserService extends IService<User> {

    Boolean updateUserMsg(UserRequest updateRequest);

    Boolean updateUserAvator(MultipartFile avatorFile, int id);

    Boolean updatePassword(UserRequest updatePasswordRequest);

    Boolean existUser(String username);

    User selectUserByUserName(String userName);

}
