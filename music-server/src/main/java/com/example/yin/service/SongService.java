package com.example.yin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.yin.common.R;
import com.example.yin.model.domain.Song;
import com.example.yin.model.reponse.TreeResponse;
import com.example.yin.model.request.SongRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface SongService extends IService<Song> {

    Boolean addSong(SongRequest addSongRequest, MultipartFile mpfile);

    Boolean updateSongMsg(SongRequest updateSongRequest);

    Boolean updateSongUrl(MultipartFile urlFile, Integer id);

    Boolean updateSongPic(MultipartFile urlFile, Integer id);

    Boolean deleteSong(Integer id);

    List<Song> allSong(SongRequest request);

    List<TreeResponse> songTreeOfSingerId(Integer singerId);
}
