package com.example.yin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.yin.common.R;
import com.example.yin.mapper.CollectMapper;
import com.example.yin.mapper.SongMapper;
import com.example.yin.model.domain.Collect;
import com.example.yin.model.domain.Song;
import com.example.yin.model.request.CollectRequest;
import com.example.yin.service.CollectService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CollectServiceImpl extends ServiceImpl<CollectMapper, Collect> implements CollectService {
    @Autowired
    private CollectMapper collectMapper;
    @Autowired
    private SongMapper songMapper;

    @Override
    public Boolean addCollect(CollectRequest addCollectRequest) {
        //作者用type来判断收藏的是歌还是歌单
        Collect collect = new Collect();
        BeanUtils.copyProperties(addCollectRequest, collect);
        Song song = songMapper.selectById(addCollectRequest.getSongId());
        collect.setSongListId(song.getSongListId());
        return collectMapper.insert(collect) > 0;
    }

    @Override
    public Boolean existSongId(CollectRequest isCollectRequest) {
        QueryWrapper<Collect> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",isCollectRequest.getUserId());
        queryWrapper.eq("song_id",isCollectRequest.getSongId());
        return collectMapper.selectCount(queryWrapper) > 0;
    }

    @Override
    public Boolean deleteCollect(Integer userId, Integer songId) {
        QueryWrapper<Collect> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",userId);
        queryWrapper.eq("song_id",songId);
       return collectMapper.delete(queryWrapper) > 0;
    }

    @Override
    public List<Collect> collectionOfUser(Integer userId) {
        QueryWrapper<Collect> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",userId);
        return collectMapper.selectList(queryWrapper);
    }
}
