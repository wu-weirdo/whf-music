export default {
  state: {
    userId: "", // ID
    username: "", // 名字
    userPic: "", // 图片
    userIntroduction: "",// 描述
  },
  getters: {
    userId: (state) => state.userId,
    username: (state) => state.username,
    userPic: (state) => state.userPic,
    userIntroduction: (state) => state.userIntroduction,
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
    setUserIntroduction: (state, userIntroduction) => {
      state.userIntroduction = userIntroduction;
    },
  },
};
