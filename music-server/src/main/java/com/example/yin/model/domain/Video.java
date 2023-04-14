package com.example.yin.model.domain;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.io.Serializable;

/**
 * (Video)表实体类
 *
 * @author whf
 * @since 2023-04-14 10:00:31
 */
@Data
public class Video implements Serializable {

    private Integer id;

    /**
     * 歌手id
     */
    private Integer singerId;

    /**
     * 名称
     */
    private String name;

    /**
     * 简介
     */
    private String introduction;

    /**
     * 图片地址
     */
    private String pic;

    /**
     * 视频地址
     */
    private String url;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;

}

