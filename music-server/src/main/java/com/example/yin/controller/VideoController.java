package com.example.yin.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.yin.common.R;
import com.example.yin.model.domain.Video;
import com.example.yin.service.VideoService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * (Video)表控制层
 *
 * @author whf
 * @since 2023-04-14 10:00:29
 */
@RestController
@RequestMapping("/video/")
public class VideoController {
    /**
     * 服务对象
     */
    @Resource
    private VideoService videoService;

    /**
     * 分页查询所有数据
     *
     * @param video 查询实体
     * @return 所有数据
     */
    @PostMapping("list")
    public R selectAll(@RequestBody Video video) {
        QueryWrapper<Video> queryWrapper = new QueryWrapper<>(video);
        queryWrapper.eq(Objects.nonNull(video.getName()), "name", video.getName());
        queryWrapper.eq(Objects.nonNull(video.getSingerId()), "singer_id", video.getSingerId());
        return R.success(null, this.videoService.list(queryWrapper));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("detail")
    public R selectOne(@RequestParam Serializable id) {
        return R.success(null, this.videoService.getById(id));
    }

    /**
     * 新增数据
     *
     * @param video 实体对象
     * @return 新增结果
     */
    @PostMapping("add")
    public R insert(@RequestBody Video video, @RequestParam("file") MultipartFile file) {
        return this.videoService.addVideo(video, file);
    }

    /**
     * 修改数据
     *
     * @param video 实体对象
     * @return 修改结果
     */
    @PostMapping("update")
    public R update(@RequestBody Video video) {
        boolean result = this.videoService.updateById(video);
        return R.success(result ? "修改成功" : "修改失败", result);
    }

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果
     */
    @GetMapping("delete")
    public R delete(@RequestParam("idList") List<Long> idList) {
        boolean result = this.videoService.removeByIds(idList);
        return R.success(result ? "删除成功" : "删除失败", result);
    }

    /**
     * 更新图片
     *
     * @param urlFile url文件
     * @param id      id
     * @return {@code R}
     */
    @PostMapping("img/update")
    public R updatePic(@RequestParam("file") MultipartFile urlFile, @RequestParam("id") Integer id) {
        return videoService.updatePic(urlFile, id);
    }

    /**
     * 更新视频
     *
     * @param urlFile url文件
     * @param id      id
     * @return {@code R}
     */
    @PostMapping("url/update")
    public R updateVideo(@RequestParam("file") MultipartFile urlFile, @RequestParam("id") Integer id) {
        return videoService.updateVideo(urlFile, id);
    }

    /**
     * 歌手视频树
     *
     * @return {@code R}
     */
    @GetMapping("tree")
    public R singerVideoTree() {
        return videoService.singerVideoTree();
    }
}

