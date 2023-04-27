package com.example.yin.controller;

import com.example.yin.common.R;
import com.example.yin.model.domain.Banner;
import com.example.yin.service.BannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * @author whf
 * @date 2023/04/27
 */
@RestController
@RequestMapping("/banner")
public class BannerController {

    @Autowired
    private BannerService bannerService;

    /**
     * 获取所有轮播图
     *
     * @return {@code R<List<Banner>>}
     */
    @GetMapping("/list")
    public R<List<Banner>> getAllBanner(){
        return R.success(bannerService.getAllBanner());
    }
}
