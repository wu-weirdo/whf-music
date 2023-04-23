package com.example.yin.config;

import com.example.yin.constant.Constants;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 集中一下图像的配置类吧
 *
 * @Author 祝英台炸油条
 * @Time : 2022/6/5 22:23
 **/
@Configuration
public class WebPicConfig implements WebMvcConfigurer {

    //TODO 这个配置类的目的是什么  就是注册了一个类似于拦截器吧  看到对应的资源 会将其修改成相应的地址
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resource/img/**")
                .addResourceLocations(Constants.IMAGES_PATH);
        registry.addResourceHandler("/resource/img/avatorImages/**")
                .addResourceLocations(Constants.AVATOR_IMAGES_PATH);
        registry.addResourceHandler("/resource/img/singerPic/**")
                .addResourceLocations(Constants.SINGER_PIC_PATH);
        registry.addResourceHandler("/resource/img/songPic/**")
                .addResourceLocations(Constants.SONG_PIC_PATH);
        registry.addResourceHandler("/resource/song/**")
                .addResourceLocations(Constants.SONG_PATH);
        registry.addResourceHandler("/resource/img/songListPic/**")
                .addResourceLocations(Constants.SONGLIST_PIC_PATH);
        registry.addResourceHandler("/resource/img/swiper/**")
                .addResourceLocations(Constants.BANNER_PIC_PATH);
        registry.addResourceHandler("/resource/video/**")
                .addResourceLocations(Constants.VIDEO_PATH);
        registry.addResourceHandler("/resource/img/videoPic/**")
                .addResourceLocations(Constants.VIDEO_PIC_PATH);
    }

}
