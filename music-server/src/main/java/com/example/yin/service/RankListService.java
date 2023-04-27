package com.example.yin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.yin.common.R;
import com.example.yin.model.domain.RankList;
import com.example.yin.model.request.RankListRequest;

public interface RankListService extends IService<RankList> {

    /**
     * 添加排名
     *
     * @param rankListAddRequest 排名列表添加请求
     * @return {@code Boolean}
     */
    Boolean addRank(RankListRequest rankListAddRequest);

    /**
     * 获取指定歌单的评分
     *
     * @param songListId 歌曲列表id
     * @return {@code Long}
     */
    Long rankOfSongListId(Long songListId);

    /**
     * 获取指定用户的歌单评分
     *
     * @param consumerId 消费者id
     * @param songListId 歌曲列表id
     * @return {@code Integer}
     */
    Integer getUserRank(Long consumerId, Long songListId);

}
