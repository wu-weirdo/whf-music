package com.example.yin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.yin.common.R;
import com.example.yin.model.domain.User;
import com.example.yin.model.request.UserRequest;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;

public interface UserService extends IService<User> {

    R updateUserMsg(UserRequest updateRequest);

    R updateUserAvator(MultipartFile avatorFile, int id);

    R updatePassword(UserRequest updatePasswordRequest);

    boolean existUser(String username);

    boolean verityPasswd(String username, String password);

    R deleteUser(Integer id);

    R allUser();

    R userOfId(Integer id);

    User selectUserByUserName(String userName);

}
