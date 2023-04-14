package com.example.yin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.yin.common.R;
import com.example.yin.mapper.SingerMapper;
import com.example.yin.mapper.VideoMapper;
import com.example.yin.model.domain.Singer;
import com.example.yin.model.domain.Song;
import com.example.yin.model.domain.Video;
import com.example.yin.model.reponse.TreeResponse;
import com.example.yin.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * (Video)表服务实现类
 *
 * @author whf
 * @since 2023-04-14 10:00:31
 */
@Service
public class VideoServiceImpl extends ServiceImpl<VideoMapper, Video> implements VideoService {

    @Autowired
    private SingerMapper singerMapper;

    @Override
    public R addVideo(Video video, MultipartFile file) {
        Singer singer = singerMapper.selectOne(new QueryWrapper<Singer>().eq("id", video.getSingerId()));
        String pic = "/img/songPic/tubiao.jpg";
        String fileName = file.getOriginalFilename();
        String filePath = System.getProperty("user.dir") + System.getProperty("file.separator")
                + "video" + System.getProperty("file.separator") + singer.getName();
        File file1 = new File(filePath);
        if (!file1.exists()) {
            if (!file1.mkdir()) {
                return R.fatal("创建文件失败");
            }
        }
        File dest = new File(filePath + System.getProperty("file.separator") + fileName);
        String storeUrlPath = "/video/" + singer.getName() + "/" + fileName;
        try {
            file.transferTo(dest);
        } catch (IOException e) {
            return R.fatal("上传失败" + e.getMessage());
        }
        video.setCreateTime(LocalDateTime.now());
        video.setUpdateTime(LocalDateTime.now());
        video.setPic(pic);
        video.setUrl(storeUrlPath);
        if (this.baseMapper.insert(video) > 0) {
            return R.success("上传成功", storeUrlPath);
        } else {
            return R.error("上传失败");
        }
    }

    @Override
    public R updatePic(MultipartFile urlFile, Integer id) {
        String fileName = System.currentTimeMillis() + urlFile.getOriginalFilename();
        String filePath = System.getProperty("user.dir") + System.getProperty("file.separator") + "img" + System.getProperty("file.separator") + "videoPic";
        File file1 = new File(filePath);
        if (!file1.exists()) {
            if (!file1.mkdir()) {
                return R.fatal("创建文件夹失败");
            }
        }

        File dest = new File(filePath + System.getProperty("file.separator") + fileName);
        String storeUrlPath = "/img/videoPic/" + fileName;
        try {
            urlFile.transferTo(dest);
        } catch (IOException e) {
            return R.fatal("上传失败" + e.getMessage());
        }
        Video video = new Video();
        video.setId(id);
        video.setPic(storeUrlPath);
        if (this.baseMapper.updateById(video) > 0) {
            return R.success("上传成功", storeUrlPath);
        } else {
            return R.error("上传失败");
        }
    }

    @Override
    public R updateVideo(MultipartFile urlFile, Integer id) {
        Video video = baseMapper.selectById(id);
        if (Objects.isNull(video)) {
            return R.error("更新失败");
        }
        Singer singer = singerMapper.selectOne(new QueryWrapper<Singer>().eq("id", video.getSingerId()));
        String fileName = urlFile.getOriginalFilename();
        String filePath = System.getProperty("user.dir") + System.getProperty("file.separator")
                + "video" + System.getProperty("file.separator") + singer.getName();
        File file1 = new File(filePath);
        if (!file1.exists()) {
            if (!file1.mkdir()) {
                return R.fatal("创建文件失败");
            }
        }
        File dest = new File(filePath + System.getProperty("file.separator") + fileName);
        String storeUrlPath = "/video/" + singer.getName() + "/" + fileName;
        try {
            urlFile.transferTo(dest);
        } catch (IOException e) {
            return R.fatal("上传失败" + e.getMessage());
        }
        video.setUpdateTime(LocalDateTime.now());
        video.setUrl(storeUrlPath);
        if (this.baseMapper.updateById(video) > 0) {
            return R.success("上传成功", storeUrlPath);
        } else {
            return R.error("上传失败");
        }
    }

    @Override
    public R singerVideoTree() {
        List<TreeResponse> responseList = new ArrayList<>();
        QueryWrapper<Video> queryWrapper = new QueryWrapper<>();
        List<Video> videos = baseMapper.selectList(queryWrapper);
        if (CollectionUtils.isEmpty(videos)) {
            return R.success(null, responseList);
        }
        Map<Integer, List<Video>> collect = videos.stream().collect(Collectors.groupingBy(Video::getSingerId, Collectors.toList()));
        for (Map.Entry<Integer, List<Video>> videoMap : collect.entrySet()) {
            TreeResponse response = new TreeResponse();
            response.setValue(String.valueOf(videoMap.getKey()));
            Singer singer = singerMapper.selectOne(new QueryWrapper<Singer>().eq("id", videoMap.getKey()));
            response.setLabel(singer.getName());
            List<Video> songList = videoMap.getValue();
            if (!CollectionUtils.isEmpty(songList)) {
                List<TreeResponse> childrenList = new ArrayList<>();
                for (Video video : songList) {
                    TreeResponse children = new TreeResponse();
                    children.setValue(String.valueOf(video.getId()));
                    children.setLabel(video.getName());
                    children.setChildren(Collections.emptyList());
                    childrenList.add(children);
                }
                response.setChildren(childrenList);
            }
            responseList.add(response);
        }
        return R.success(null, responseList);
    }
}

