package com.example.yin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.yin.constant.ResultEnum;
import com.example.yin.excepetion.ServiceException;
import com.example.yin.mapper.SingerMapper;
import com.example.yin.mapper.SongListMapper;
import com.example.yin.mapper.SongMapper;
import com.example.yin.model.domain.Singer;
import com.example.yin.model.domain.Song;
import com.example.yin.model.domain.SongList;
import com.example.yin.model.reponse.TreeResponse;
import com.example.yin.model.request.SongRequest;
import com.example.yin.service.SongListService;
import com.example.yin.service.SongService;
import com.example.yin.utils.ExceptionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SongServiceImpl extends ServiceImpl<SongMapper, Song> implements SongService {

    @Autowired
    private SongMapper songMapper;

    @Autowired
    private SingerMapper singerMapper;

    @Autowired
    private SongListMapper songListMapper;

    @Override
    public List<Song> allSong(SongRequest request) {
        LambdaQueryWrapper<Song> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Objects.nonNull(request.getSongListId()), Song::getSongListId, request.getSongListId());
        wrapper.eq(Objects.nonNull(request.getSingerId()), Song::getSingerId, request.getSingerId());
        wrapper.like(Objects.nonNull(request.getName()), Song::getName, request.getName());
        wrapper.like(Objects.nonNull(request.getId()), Song::getId, request.getId());
        return songMapper.selectList(wrapper);
    }

    @Override
    public Boolean addSong(SongRequest addSongRequest, MultipartFile mpfile) {
        Singer singer = singerMapper.selectOne(new QueryWrapper<Singer>().eq("id", addSongRequest.getSingerId()));
        SongList songList = songListMapper.selectOne(new QueryWrapper<SongList>().eq("id", addSongRequest.getSongListId()));
        Song song = new Song();
        BeanUtils.copyProperties(addSongRequest, song);
        String pic = Optional.ofNullable(songList.getPic()).orElse("/resource/img/songPic/tubiao.jpg");
        String fileName = mpfile.getOriginalFilename();
        String singerPath = System.getProperty("user.dir") + System.getProperty("file.separator")
                + "resource" + System.getProperty("file.separator")
                + "song" + System.getProperty("file.separator") + singer.getName();
        File file1 = new File(singerPath);
        if (!file1.exists()) {
            if (!file1.mkdir()) {
                log.error("SongService addSong mkdir error");
                throw new ServiceException(ResultEnum.FILE_UPLOAD_ERROR.getCode(), "歌曲添加失败");
            }
        }
        String filePath = singerPath + System.getProperty("file.separator") + addSongRequest.getIntroduction();
        File file2 = new File(filePath);
        if (!file2.exists()) {
            if (!file2.mkdir()) {
                log.error("SongService addSong mkdir error");
                throw new ServiceException(ResultEnum.FILE_UPLOAD_ERROR.getCode(), "歌曲添加失败");
            }
        }
        File dest = new File(filePath + System.getProperty("file.separator") + fileName);
        String storeUrlPath = "/resource/song/" + singer.getName() + "/" + addSongRequest.getIntroduction() + "/" + fileName;
        try {
            mpfile.transferTo(dest);
        } catch (IOException e) {
            log.error("SongService addSong error:{}", ExceptionUtils.getStackTraceMessage(e));
            throw new ServiceException(ResultEnum.FILE_UPLOAD_ERROR.getCode(), "歌曲添加失败");
        }
        song.setCreateTime(new Date());
        song.setUpdateTime(new Date());
        song.setPic(pic);
        song.setUrl(storeUrlPath);
        return songMapper.insert(song) > 0;
    }

    @Override
    public Boolean updateSongMsg(SongRequest updateSongRequest) {
        Song song = new Song();
        BeanUtils.copyProperties(updateSongRequest, song);
        return songMapper.updateById(song) > 0;
    }

    @Override
    public Boolean updateSongUrl(MultipartFile urlFile, Integer id) {
        Song song = songMapper.selectById(id);
        if (Objects.isNull(song)) {
            throw new ServiceException(ResultEnum.PARAMETER_ERROR.getCode(), "歌曲不存在");
        }
        Singer singer = singerMapper.selectOne(new QueryWrapper<Singer>().eq("id", song.getSingerId()));
        String fileName = urlFile.getOriginalFilename();
        String filePath = System.getProperty("user.dir") + System.getProperty("file.separator")
                + "resource" + System.getProperty("file.separator")
                + "song" + System.getProperty("file.separator") + singer.getName()
                + System.getProperty("file.separator") + song.getIntroduction();
        File file1 = new File(filePath);
        if (!file1.exists()) {
            if (!file1.mkdir()) {
                log.error("SongService updateSongUrl mkdir error");
                throw new ServiceException(ResultEnum.FILE_UPLOAD_ERROR.getCode(), "歌曲上传失败");
            }
        }
        File dest = new File(filePath + System.getProperty("file.separator") + fileName);
        String storeUrlPath = "/resource/song/" + singer.getName() + "/" + song.getIntroduction() + "/" + fileName;
        try {
            urlFile.transferTo(dest);
        } catch (IOException e) {
            log.error("SongService addSong error:{}", ExceptionUtils.getStackTraceMessage(e));
            throw new ServiceException(ResultEnum.FILE_UPLOAD_ERROR.getCode(), "歌曲上传失败");
        }
        Song update = new Song();
        update.setId(id);
        update.setUrl(storeUrlPath);
        return songMapper.updateById(update) > 0;
    }

    @Override
    public Boolean updateSongPic(MultipartFile urlFile, Integer id) {
        String fileName = System.currentTimeMillis() + urlFile.getOriginalFilename();
        String filePath = System.getProperty("user.dir") + System.getProperty("file.separator") + "img" + System.getProperty("file.separator") + "songPic";
        File file1 = new File(filePath);
        if (!file1.exists()) {
            if (!file1.mkdir()) {
                log.error("SongService updateSongPic mkdir error");
                throw new ServiceException(ResultEnum.FILE_UPLOAD_ERROR.getCode(), "图片上传失败");
            }
        }

        File dest = new File(filePath + System.getProperty("file.separator") + fileName);
        String storeUrlPath = "/resource/img/songPic/" + fileName;
        try {
            urlFile.transferTo(dest);
        } catch (IOException e) {
            log.error("SongService addSong error:{}", ExceptionUtils.getStackTraceMessage(e));
            throw new ServiceException(ResultEnum.FILE_UPLOAD_ERROR.getCode(), "图片上传失败");
        }
        Song song = new Song();
        song.setId(id);
        song.setPic(storeUrlPath);
        return songMapper.updateById(song) > 0;
    }

    @Override
    public Boolean deleteSong(Integer id) {
        return songMapper.deleteById(id) > 0;
    }

    @Override
    public List<TreeResponse> songTreeOfSingerId(Integer singerId) {
        List<TreeResponse> responseList = new ArrayList<>();
        QueryWrapper<Song> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("singer_id", singerId);
        List<Song> songs = songMapper.selectList(queryWrapper);
        if (CollectionUtils.isEmpty(songs)) {
            return responseList;
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
        return responseList;
    }
}
