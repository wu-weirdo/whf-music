package com.example.yin.controller;

import com.example.yin.common.R;
import com.example.yin.model.request.SongRequest;
import com.example.yin.service.SongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.util.unit.DataSize;
import org.springframework.util.unit.DataUnit;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.MultipartConfigElement;

@RestController
public class SongController {

    @Autowired
    private SongService songService;


    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        // 文件最大10M,DataUnit提供5中类型B,KB,MB,GB,TB
        factory.setMaxFileSize(DataSize.of(100, DataUnit.MEGABYTES));
        // 设置总上传数据总大小10M
        factory.setMaxRequestSize(DataSize.of(100, DataUnit.MEGABYTES));
        return factory.createMultipartConfig();
    }


    /**
     * 添加歌曲
     *
     * @param addSongRequest 添加歌曲请求
     * @param mpfile         mpfile
     * @return {@code R}
     */
    @PostMapping("/song/add")
    public R addSong(SongRequest addSongRequest, @RequestParam("file") MultipartFile mpfile) {
        return songService.addSong(addSongRequest,mpfile);
    }


    /**
     * 删除歌曲
     *
     * @param id id
     * @return {@code R}
     */
    @DeleteMapping("/song/delete")
    public R deleteSong(@RequestParam int id) {
        return songService.deleteSong(id);
    }


    /**
     * 返回所有歌曲
     *
     * @return {@code R}
     */
    @GetMapping("/song")
    public R allSong() {
        return songService.allSong();
    }


    /**
     * 返回指定歌曲ID的歌曲
     *
     * @param id id
     * @return {@code R}
     */
    @GetMapping("/song/detail")
    public R songOfId(@RequestParam int id) {
        return songService.songOfId(id);
    }

    /**
     * 返回指定歌手ID的歌曲
     *
     * @param singerId 歌手id
     * @return {@code R}
     */
    @GetMapping("/song/singer/detail")
    public R songOfSingerId(@RequestParam int singerId) {
        return songService.songOfSingerId(singerId);
    }

    /**
     * 返回指定歌手ID的歌曲树
     *
     * @param singerId 歌手id
     * @return {@code R}
     */
    @GetMapping("/song/singer/detail/tree")
    public R songTreeOfSingerId(@RequestParam int singerId) {
        return songService.songTreeOfSingerId(singerId);
    }

    /**
     * 返回指定歌手名的歌曲
     *
     * @param name 名字
     * @return {@code R}
     */
    @GetMapping("/song/singerName/detail")
    public R songOfSingerName(@RequestParam String name) {
        return songService.songOfSingerName('%' + name + '%');
    }

    /**
     * 更新歌曲信息
     *
     * @param updateSongRequest 请求更新歌曲
     * @return {@code R}
     */
    @PostMapping("/song/update")
    public R updateSongMsg(@RequestBody SongRequest updateSongRequest) {
        return songService.updateSongMsg(updateSongRequest);
    }


    /**
     * 更新歌曲图片
     *
     * @param urlFile url文件
     * @param id      id
     * @return {@code R}
     */
    @PostMapping("/song/img/update")
    public R updateSongPic(@RequestParam("file") MultipartFile urlFile, @RequestParam("id") int id) {
        return songService.updateSongPic(urlFile, id);
    }

    /**
     * 更新歌曲
     *
     * @param urlFile url文件
     * @param id      id
     * @return {@code R}
     */
    @PostMapping("/song/url/update")
    public R updateSongUrl(@RequestParam("file") MultipartFile urlFile, @RequestParam("id") int id) {
        return songService.updateSongUrl(urlFile, id);
    }
}
