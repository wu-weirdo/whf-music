package com.example.yin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.yin.model.domain.Banner;

import java.util.List;

/**
 * 轮播图服务
 *
 * @author whf
 * @date 2023/04/27
 */
public interface BannerService extends IService<Banner> {

    /**
     * 获取所有轮播图
     *
     * @return {@code List<Banner>}
     */
    List<Banner> getAllBanner();
}
