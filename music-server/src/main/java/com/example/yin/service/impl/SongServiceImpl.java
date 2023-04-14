package com.example.yin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.yin.common.R;
import com.example.yin.mapper.SingerMapper;
import com.example.yin.mapper.SongMapper;
import com.example.yin.model.domain.Singer;
import com.example.yin.model.domain.Song;
import com.example.yin.model.reponse.TreeResponse;
import com.example.yin.model.request.SongRequest;
import com.example.yin.service.SongService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SongServiceImpl extends ServiceImpl<SongMapper, Song> implements SongService {

    @Autowired
    private SongMapper songMapper;

    @Autowired
    private SingerMapper singerMapper;

    @Override
    public R allSong() {
        return R.success(null, songMapper.selectList(null));
    }

    @Override
    public R addSong(SongRequest addSongRequest, MultipartFile mpfile) {
        Singer singer = singerMapper.selectOne(new QueryWrapper<Singer>().eq("id", addSongRequest.getSingerId()));
        Song song = new Song();
        BeanUtils.copyProperties(addSongRequest, song);
        String pic = "/img/songPic/tubiao.jpg";
        String fileName = mpfile.getOriginalFilename();
        String filePath = System.getProperty("user.dir") + System.getProperty("file.separator")
                + "song" + System.getProperty("file.separator") + singer.getName()
                + System.getProperty("file.separator") + addSongRequest.getIntroduction();
        File file1 = new File(filePath);
        if (!file1.exists()) {
            if (!file1.mkdir()) {
                return R.fatal("创建文件失败");
            }
        }
        File dest = new File(filePath + System.getProperty("file.separator") + fileName);
        String storeUrlPath = "/song/" + singer.getName() + "/" + addSongRequest.getIntroduction() + "/" + fileName;
        try {
            mpfile.transferTo(dest);
        } catch (IOException e) {
            return R.fatal("上传失败" + e.getMessage());
        }
        song.setCreateTime(new Date());
        song.setUpdateTime(new Date());
        song.setPic(pic);
        song.setUrl(storeUrlPath);
        if (songMapper.insert(song) > 0) {
            return R.success("上传成功", storeUrlPath);
        } else {
            return R.error("上传失败");
        }
    }

    @Override
    public R updateSongMsg(SongRequest updateSongRequest) {
        Song song = new Song();
        BeanUtils.copyProperties(updateSongRequest, song);
        if (songMapper.updateById(song) > 0) {
            return R.success("修改成功");
        } else {
            return R.error("修改失败");
        }
    }

    @Override
    public R updateSongUrl(MultipartFile urlFile, int id) {
        Song song = songMapper.selectById(id);
        if (Objects.isNull(song)) {
            return R.error("更新失败");
        }
        Singer singer = singerMapper.selectOne(new QueryWrapper<Singer>().eq("id", song.getSingerId()));
        String fileName = urlFile.getOriginalFilename();
        String filePath = System.getProperty("user.dir") + System.getProperty("file.separator")
                + "song" + System.getProperty("file.separator") + singer.getName()
                + System.getProperty("file.separator") + song.getIntroduction();
        File file1 = new File(filePath);
        if (!file1.exists()) {
            if (!file1.mkdir()) {
                return R.fatal("创建目的文件夹失败");
            }
        }
        File dest = new File(filePath + System.getProperty("file.separator") + fileName);
        String storeUrlPath = "/song/" + singer.getName() + "/" + song.getIntroduction() + "/" + fileName;
        try {
            urlFile.transferTo(dest);
        } catch (IOException e) {
            return R.fatal("更新失败" + e.getMessage());
        }
        Song update = new Song();
        update.setId(id);
        update.setUrl(storeUrlPath);
        if (songMapper.updateById(update) > 0) {
            return R.success("更新成功", storeUrlPath);
        } else {
            return R.error("更新失败");
        }
    }

    @Override
    public R updateSongPic(MultipartFile urlFile, int id) {
        String fileName = System.currentTimeMillis() + urlFile.getOriginalFilename();
        String filePath = System.getProperty("user.dir") + System.getProperty("file.separator") + "img" + System.getProperty("file.separator") + "songPic";
        File file1 = new File(filePath);
        if (!file1.exists()) {
            if (!file1.mkdir()) {
                return R.fatal("创建文件夹失败");
            }
        }

        File dest = new File(filePath + System.getProperty("file.separator") + fileName);
        String storeUrlPath = "/img/songPic/" + fileName;
        try {
            urlFile.transferTo(dest);
        } catch (IOException e) {
            return R.fatal("上传失败" + e.getMessage());
        }
        Song song = new Song();
        song.setId(id);
        song.setPic(storeUrlPath);
        if (songMapper.updateById(song) > 0) {
            return R.success("上传成功", storeUrlPath);
        } else {
            return R.error("上传失败");
        }
    }

    @Override
    public R deleteSong(Integer id) {
        if (songMapper.deleteById(id) > 0) {
            return R.success("删除成功");
        } else {
            return R.error("删除失败");
        }
    }

    @Override
    public R songOfSingerId(Integer singerId) {
        QueryWrapper<Song> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("singer_id",singerId);
        return R.success(null, songMapper.selectList(queryWrapper));
    }

    @Override
    public R songOfId(Integer id) {
        QueryWrapper<Song> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",id);
        return R.success(null, songMapper.selectList(queryWrapper));
    }

    @Override
    public R songOfSingerName(String name) {
        QueryWrapper<Song> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("name",name);
        return R.success(null, songMapper.selectList(queryWrapper));
    }

    @Override
    public R songTreeOfSingerId(int singerId) {
        List<TreeResponse> responseList = new ArrayList<>();
        QueryWrapper<Song> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("singer_id",singerId);
        List<Song> songs = songMapper.selectList(queryWrapper);
        if (CollectionUtils.isEmpty(songs)) {
            return R.success(null, responseList);
        }
        Map<String, List<Song>> collect = songs.stream().collect(Collectors.groupingBy(Song::getIntroduction, Collectors.toList()));
        for (Map.Entry<String, List<Song>> songMap : collect.entrySet()) {
            TreeResponse response = new TreeResponse();
            response.setValue(songMap.getKey());
            response.setLabel(songMap.getKey());
            List<Song> songList = songMap.getValue();
            if (!CollectionUtils.isEmpty(songList)) {
                List<TreeResponse> childrenList = new ArrayList<>();
                for (Song song : songList) {
                    TreeResponse children = new TreeResponse();
                    children.setValue(String.valueOf(song.getId()));
                    children.setLabel(song.getName());
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
