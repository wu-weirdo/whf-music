package com.example.yin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.yin.common.R;
import com.example.yin.model.domain.Video;
import com.example.yin.model.reponse.TreeResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * (Video)表服务接口
 *
 * @author whf
 * @since 2023-04-14 10:00:31
 */
public interface VideoService extends IService<Video> {

    Boolean addVideo(Video video, MultipartFile file);

    Boolean updatePic(MultipartFile urlFile, Integer id);

    Boolean updateVideo(MultipartFile urlFile, Integer id);

    List<TreeResponse> singerVideoTree();
}

