package com.example.yin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.yin.common.R;
import com.example.yin.constant.Constants;
import com.example.yin.constant.ResultEnum;
import com.example.yin.excepetion.ServiceException;
import com.example.yin.mapper.UserMapper;
import com.example.yin.model.domain.User;
import com.example.yin.model.request.UserRequest;
import com.example.yin.security.utils.SecurityUtils;
import com.example.yin.service.UserService;
import com.example.yin.utils.ExceptionUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import static com.example.yin.constant.Constants.SALT;

@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public Boolean updateUserMsg(UserRequest updateRequest) {
        User user = new User();
        BeanUtils.copyProperties(updateRequest, user);
        return userMapper.updateById(user) > 0;
    }

    @Override
    public Boolean updatePassword(UserRequest updatePasswordRequest) {
        User user = userMapper.selectById(updatePasswordRequest.getId());
        if (Objects.isNull(user)) {
            throw new ServiceException(ResultEnum.USER_NOT_EXIST);
        }
        if (!SecurityUtils.matchesPassword(updatePasswordRequest.getOldPassword(), user.getPassword())) {
            throw new ServiceException(ResultEnum.USERNAME_PASSWORD_ERROR);
        }
        String secretPassword = SecurityUtils.encryptPassword(updatePasswordRequest.getPassword());
        user.setPassword(secretPassword);
        return userMapper.updateById(user) > 0;
    }

    @Override
    public String updateUserAvator(MultipartFile avatorFile, int id) {
        String fileName = System.currentTimeMillis() + avatorFile.getOriginalFilename();
        //路径 他这个会根据你的系统获取对应的文件分隔符
        String filePath = Constants.PROJECT_PATH + System.getProperty("file.separator") + "img" + System.getProperty("file.separator") + "avatorImages";
        File file = new File(filePath);
        if (!file.exists() && !file.mkdir()) {
            log.error("UserService updateUserAvator mkdir error");
            throw new ServiceException(ResultEnum.FILE_UPLOAD_ERROR.getCode(), "图片上传失败");
        }
        File dest = new File(filePath + System.getProperty("file.separator") + fileName);
        String imgPath = "/resource/img/avatorImages/" + fileName;
        try {
            avatorFile.transferTo(dest);
        } catch (IOException e) {
            log.error("SongService updateUserAvator error:{}", ExceptionUtils.getStackTraceMessage(e));
            throw new ServiceException(ResultEnum.FILE_UPLOAD_ERROR.getCode(), "图片上传失败");
        }
        User user = new User();
        user.setId(id);
        user.setAvator(imgPath);
        userMapper.updateById(user);
        return imgPath;
    }

    @Override
    public Boolean existUser(String username) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_name",username);
        return userMapper.selectCount(queryWrapper) > 0;
    }

    @Override
    public User selectUserByUserName(String userName) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_name",userName);
        return userMapper.selectOne(queryWrapper);
    }
}
