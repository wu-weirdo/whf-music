package com.example.yin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.yin.common.R;
import com.example.yin.model.domain.Video;
import org.springframework.web.multipart.MultipartFile;

/**
 * (Video)表服务接口
 *
 * @author whf
 * @since 2023-04-14 10:00:31
 */
public interface VideoService extends IService<Video> {

    R addVideo(Video video, MultipartFile file);

    R updatePic(MultipartFile urlFile, Integer id);

    R updateVideo(MultipartFile urlFile, Integer id);

    R singerVideoTree();
}

