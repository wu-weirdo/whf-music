import {deletes, get, getBaseURL, post} from './request'

const HttpManager = {
    // 获取图片信息
    attachImageUrl: (url) => `${getBaseURL()}${url}`,
    // =======================> 管理员 API 完成
    // 登录
    signIn: ({userName,password}) => post(`login`, {userName,password}),

    // =======================> 用户 API 完成
    // 返回所有用户
    getAllUser: () => get(`user`),
    // 返回指定ID的用户
    getUserOfId: (id) => get(`user/detail?id=${id}`),
    // 删除用户
    deleteUser: (id) => get(`user/delete?id=${id}`),

    // =======================> 收藏列表 API 完成
    // 返回的指定用户ID收藏列表
    getCollectionOfUser: (userId) => get(`collection/detail?userId=${userId}`),
    // 删除收藏的歌曲
    deleteCollection: (userId, songId) => deletes(`collection/delete?userId=${userId}&&songId=${songId}`),

    // =======================> 评论列表 API 完成
    // 获得指定歌曲ID的评论列表
    getCommentOfSongId: (songId) => get(`comment/song/detail?songId=${songId}`),
    // 获得指定歌单ID的评论列表
    getCommentOfSongListId: (songListId) => get(`comment/songList/detail?songListId=${songListId}`),
    // 删除评论
    deleteComment: (id) => get(`comment/delete?id=${id}`),

    // =======================> 歌手 API 完成
    // 返回所有歌手
    getAllSinger: () => get(`singer/list`),
    // 添加歌手
    setSinger: ({name, sex, birth, location, introduction}) => post(`singer/add`, {
        name,
        sex,
        birth,
        location,
        introduction
    }),
    // 更新歌手信息
    updateSingerMsg: ({id, name, sex, birth, location, introduction}) => post(`singer/update`, {
        id,
        name,
        sex,
        birth,
        location,
        introduction
    }),
    // 删除歌手
    deleteSinger: (id) => deletes(`singer/delete?id=${id}`),
    // 更新歌手图片
    updateSingerPic:(id) => post(`/singer/avatar/update?id=${id}`),

    // =======================> 歌曲 API  完成
    // 返回所有歌曲
    getAllSong: (id, name, singerId, songListId) => post(`song/list`, {id, name, singerId, songListId}),
    // 返回指定歌手ID的歌曲
    getSongOfSingerId: (id) => get(`song/singer/detail?singerId=${id}`),
    // 返回指定歌手ID的歌曲树
    getSongTreeOfSingerId: (id) => get(`song/singer/detail/tree?singerId=${id}`),
    // 返回的指定用户ID收藏列表
    getSongOfId: (id) => get(`song/detail?id=${id}`),
    // 返回指定歌手名的歌曲
    getSongOfSingerName: (id) => get(`song/singerName/detail?name=${id}`),
    // 更新歌曲信息
    updateSongMsg: ({id, singerId, name, introduction, lyric}) => post(`song/update`, {
        id,
        singerId,
        name,
        introduction,
        lyric
    }),
    updateSongUrl: (id) => `${getBaseURL()}/song/url/update?id=${id}`,
    updateSongImg: (id) => `${getBaseURL()}/song/img/update?id=${id}`,
    // 删除歌曲
    deleteSong: (id) => deletes(`song/delete?id=${id}`),

    // =======================> 歌单 API 完成
    // 添加歌单t
    setSongList: ({title, introduction, style, singerId}) => post(`songList/add`, {title, introduction, style, singerId}),
    // 获取全部歌单
    getSongList: (singerId, singerName) => post(`songList/list`, {singerId, singerName}),
    // 更新歌单信息
    updateSongListMsg: ({id, title, introduction, style}) => post(`songList/update`, {id, title, introduction, style}),
    // 删除歌单
    deleteSongList: (id) => get(`songList/delete?id=${id}`),

    // =======================> 歌单歌曲 API 完成
    // 给歌单添加歌曲
    setListSong: ({songId, songIds, songListId}) => post(`listSong/add`, {songId, songIds, songListId}),
    // 返回歌单里指定歌单ID的歌曲
    getListSongOfSongId: (songListId) => get(`listSong/detail?songListId=${songListId}`),
    // 删除歌单里的歌曲
    deleteListSong: (songId) => get(`listSong/delete?songId=${songId}`),

    // 获取视频列表
    getVideoList: ({singerId, name}) => post(`video/list`, {
        singerId,
        name,
    }),
    updateVideoImg: (id) => `${getBaseURL()}/video/img/update?id=${id}`,
    // 更新视频信息
    updateVideoMsg: ({id, singerId, name, introduction}) => post(`video/update`, {
        id,
        singerId,
        name,
        introduction
    }),
    // 删除视频
    deleteVideo: (id) => deletes(`video/delete?id=${id}`),
    updateVideoUrl: (id) => `${getBaseURL()}/video/url/update?id=${id}`,
}

export {HttpManager}
