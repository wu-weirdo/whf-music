package com.example.yin.constant;

public class Constants {
    /* 歌曲图片，歌手图片，歌曲文件，歌单图片等文件的存放路径 */
    public static String FILE_ROOT_PATH = "/usr/";
    public static String PROJECT_PATH = System.getProperty("user.dir") + "/resource";
    public static String IMAGES_PATH = "file:" + PROJECT_PATH + "/img/";
    public static String AVATOR_IMAGES_PATH = "file:" + PROJECT_PATH + "/img/avatorImages/";
    public static String SONGLIST_PIC_PATH = "file:" + PROJECT_PATH + "/img/songListPic/";
    public static String SONG_PIC_PATH = "file:" + PROJECT_PATH + "/img/songPic/";
    public static String SONG_PATH = "file:" + PROJECT_PATH + "/song/";
    public static String SINGER_PIC_PATH = "file:" + PROJECT_PATH + "/img/singerPic/";
    public static String BANNER_PIC_PATH = "file:" + PROJECT_PATH + "/img/swiper/";
    public static String VIDEO_PATH = "file:" + PROJECT_PATH + "/video/";
    public static String VIDEO_PIC_PATH = "file:" + PROJECT_PATH + "/img/videoPic/";


    /* 盐值加密 */
    public static String SALT = "zyt";

    /**
     * 令牌前缀
     */
    public static final String TOKEN_PREFIX = "Bearer ";

    /**
     * 令牌前缀
     */
    public static final String LOGIN_USER_KEY = "login_user_key";

    /**
     * 登录用户 redis key
     */
    public static final String LOGIN_TOKEN_KEY = "login:tokens:";
}
