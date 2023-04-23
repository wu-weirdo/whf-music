package com.example.yin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.yin.common.R;
import com.example.yin.constant.Constants;
import com.example.yin.mapper.UserMapper;
import com.example.yin.model.domain.User;
import com.example.yin.model.request.UserRequest;
import com.example.yin.service.UserService;
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

import static com.example.yin.constant.Constants.SALT;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public R updateUserMsg(UserRequest updateRequest) {
        User user = new User();
        BeanUtils.copyProperties(updateRequest, user);
        if (userMapper.updateById(user) > 0) {
            return R.success("修改成功");
        } else {
            return R.error("修改失败");
        }
    }

    @Override
    public R updatePassword(UserRequest updatePasswordRequest) {

       if (!this.verityPasswd(updatePasswordRequest.getUsername(),updatePasswordRequest.getOldPassword())) {
            return R.error("密码输入错误");
        }

        User user = new User();
        user.setId(updatePasswordRequest.getId());
        String secretPassword = DigestUtils.md5DigestAsHex((SALT + updatePasswordRequest.getPassword()).getBytes(StandardCharsets.UTF_8));
        user.setPassword(secretPassword);

        if (userMapper.updateById(user) > 0) {
            return R.success("密码修改成功");
        } else {
            return R.error("密码修改失败");
        }
    }

    @Override
    public R updateUserAvator(MultipartFile avatorFile, int id) {
        String fileName = System.currentTimeMillis() + avatorFile.getOriginalFilename();
        //路径 他这个会根据你的系统获取对应的文件分隔符
        String filePath = Constants.PROJECT_PATH + System.getProperty("file.separator") + "img" + System.getProperty("file.separator") + "avatorImages";
        File file = new File(filePath);
        if (!file.exists() && !file.mkdir()) {
            return R.fatal("创建文件失败");
        }
        File dest = new File(filePath + System.getProperty("file.separator") + fileName);
        String imgPath = "/img/avatorImages/" + fileName;
        try {
            avatorFile.transferTo(dest);
        } catch (IOException e) {
            return R.fatal("上传失败" + e.getMessage());
        }
        User user = new User();
        user.setId(id);
        user.setAvator(imgPath);
        if (userMapper.updateById(user) > 0) {
            return R.success("上传成功", imgPath);
        } else {
            return R.error("上传失败");
        }
    }

    @Override
    public boolean existUser(String username) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_name",username);
        return userMapper.selectCount(queryWrapper) > 0;
    }

    @Override
    public boolean verityPasswd(String username, String password) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_name",username);
        String secretPassword = DigestUtils.md5DigestAsHex((SALT + password).getBytes(StandardCharsets.UTF_8));

        queryWrapper.eq("password",secretPassword);
        return userMapper.selectCount(queryWrapper) > 0;
    }

    // 删除用户
    @Override
    public R deleteUser(Integer id) {
        if (userMapper.deleteById(id) > 0) {
            return R.success("删除成功");
        } else {
            return R.error("删除失败");
        }
    }

    @Override
    public R allUser() {
        return R.success(null, userMapper.selectList(null));
    }

    @Override
    public R userOfId(Integer id) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",id);
        return R.success(null, userMapper.selectList(queryWrapper));
    }

    @Override
    public User selectUserByUserName(String userName) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_name",userName);
        return userMapper.selectOne(queryWrapper);
    }
}
