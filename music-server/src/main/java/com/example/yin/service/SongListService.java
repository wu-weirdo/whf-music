package com.example.yin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.yin.common.R;
import com.example.yin.model.domain.SongList;
import com.example.yin.model.request.SongListRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface SongListService extends IService<SongList> {

    Boolean addSongList(SongListRequest addSongListRequest);

    Boolean updateSongListMsg(SongListRequest updateSongListRequest);

    Boolean updateSongListImg(MultipartFile avatorFile, int id);

    Boolean deleteSongList(Integer id);

    List<SongList> getSongList(SongListRequest request);
}
