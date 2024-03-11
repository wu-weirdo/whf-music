import { getBaseURL, get, post, deletes } from "./request";

const HttpManager = {
  // 获取图片信息
  attachImageUrl: (url) => url ? `${getBaseURL()}${url}` : "https://cube.elemecdn.com/e/fd/0fc7d20532fdaf769a25683617711png.png",
  // =======================> 用户 API 完成
  // 登录
  signIn: ({userName,password}) => post(`login`, {userName,password}),
  // 注册
  SignUp: ({userName,password,sex,phoneNum,email,birth,introduction,location}) => post(`register`, {userName,password,sex,phoneNum,email,birth,introduction,location}),
  // 删除用户
  deleteUser: (id) => get(`user/delete?id=${id}`),
  // 更新用户信息
  updateUserMsg: ({id,userName,sex,phoneNum,email,birth,introduction,location}) => post(`user/update`, {id,userName,sex,phoneNum,email,birth,introduction,location}),
  updateUserPassword: ({id,userName,oldPassword,password}) => post(`user/updatePassword`, {id,userName,oldPassword,password}),
  // 返回指定ID的用户
  getUserOfId: (id) => get(`user/detail?id=${id}`),
  // 更新用户头像
  uploadUrl: (userId) => `${getBaseURL()}/user/avatar/update?id=${userId}`,

  // =======================> 歌单 API 完成
  // 获取全部歌单
  getSongList: () => post("songList/list"),
  // 获取歌单类型
  getSongListOfStyle: (style) => get(`songList/style/detail?style=${style}`),
  // 返回标题包含文字的歌单
  getSongListOfLikeTitle: (keywords) => get(`songList/likeTitle/detail?title=${keywords}`),
  // 返回歌单里指定歌单ID的歌曲
  getListSongOfSongId: (songListId) => post(`song/list`, {songListId: songListId}),

  // =======================> 歌手 API  完成
  // 返回所有歌手
  getAllSinger: () => get("singer/list"),
  // 通过性别对歌手分类
  getSingerOfSex: (sex) => get(`singer/sex/detail?sex=${sex}`),

  // =======================> 收藏 API 完成
  // 返回的指定用户ID的收藏列表
  getCollectionOfUser: (userId) => get(`collection/detail?userId=${userId}`),
  // 添加收藏的歌曲 type: 0 代表歌曲， 1 代表歌单
  setCollection: ({userId,type,songId}) => post(`collection/add`,{userId,type,songId}),

  deleteCollection: (userId, songId) => deletes(`collection/delete?userId=${userId}&&songId=${songId}`),

  isCollection: ({userId, type, songId}) => post(`collection/status`, {userId, type, songId}),

  // =======================> 评分 API 完成
  // 提交评分
  setRank: ({songListId,consumerId,score}) => post(`rankList/add`, {songListId,consumerId,score}),
  // 获取指定歌单的评分
  getRankOfSongListId: (songListId) => get(`rankList/list?songListId=${songListId}`),
  // 获取指定用户的歌单评分
  getUserRank: (consumerId, songListId) => get(`/rankList/user?consumerId=${consumerId}&songListId=${songListId}`),

  // =======================> 评论 API 完成
  // 添加评论
  setComment: ({userId,content,songId,songListId,nowType}) => post(`comment/add`, {userId,content,songId,songListId,nowType}),
  // 删除评论
  deleteComment: (id) => get(`comment/delete?id=${id}`),
  // 点赞
  setSupport: ({id,up}) => post(`comment/like`, {id,up}),
  // 返回所有评论
  getAllComment: (type, id) => {
    let url = "";
    if (type === 1) {
      url = `comment/songList/detail?songListId=${id}`;
    } else if (type === 0) {
      url = `comment/song/detail?songId=${id}`;
    }
    return get(url);
  },

  // =======================> 歌曲 API
  getAllSong:({id, singerId, songListId}) => post(`song/list`, {id, singerId, songListId}),
  // 返回指定歌曲ID的歌曲
  getSongOfId: (id) => post(`song/list`, {id: id}),
  // 返回指定歌手ID的歌曲
  getSongOfSingerId: (id) => post(`song/list`, {singerId: id}),
  // 返回指定歌手名的歌曲
  getSongOfSingerName: (keywords) =>  post(`song/list`, {name: keywords}),
  // 下载音乐
  downloadMusic: (songId) => get(`download/song?songId=${songId}`, { responseType: "blob" }),

  //======================> 点赞api的优化 避免有些是重复的点赞！新增数据表了得

  testAlreadySupport:({commentId,userId}) => post(`userSupport/test`, {commentId,userId}),

  deleteUserSupport:({commentId,userId}) => post(`userSupport/delete`, {commentId,userId}),

  insertUserSupport:({commentId,userId}) => post(`userSupport/insert`, {commentId,userId}),

  //获取所有的海报
  getBannerList: () => get("banner/list"),

  //歌手视频树
  getSingerVideoTree: () => get("video/tree"),

  //视频详情
  getVideoDetail:(id) => get(`video/detail?id=${id}`),

  // 返回所有歌手
  getAllVideo: () => post("video/list"),
};



export { HttpManager };
