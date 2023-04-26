export default {
  state: {
    userId: "", // ID
    username: "", // 名字
    userPic: "", // 图片
    token: "" //token
  },
  getters: {
    userId: (state) => state.userId,
    username: (state) => state.username,
    userPic: (state) => state.userPic,
    token: (state) => state.token,
  },
  mutations: {
    setUserId: (state, userId) => {
      state.userId = userId;
    },
    setUsername: (state, username) => {
      state.username = username;
    },
    setUserPic: (state, userPic) => {
      state.userPic = userPic;
    },
    setToken: (state, token) => {
      state.token = token;
    },
  },
};
