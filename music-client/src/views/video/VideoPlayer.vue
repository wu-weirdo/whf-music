<template>
  <video-player
      class="video-player vjs-big-play-centered"
      :src="attachImageUrl(videoInfo.url)"
      :poster="attachImageUrl(videoInfo.pic)"
      crossorigin="anonymous"
      playsinline
      controls
      :volume="0.6"
      :height="730"
      :width="1280"
      :playback-rates="[0.7, 1.0, 1.5, 2.0]"
      @mounted="handleEvent($event)"
      @ready="handleEvent($event)"
      @play="handleEvent($event)"
      @pause="handleEvent($event)"
      @ended="handleEvent($event)"
      @loadeddata="handleEvent($event)"
      @waiting="handleEvent($event)"
      @playing="handleEvent($event)"
      @canplay="handleEvent($event)"
      @canplaythrough="handleEvent($event)"
      @timeupdate="handleEvent($event)"
  />
</template>

<script>
import 'video.js/dist/video-js.css'
import {HttpManager} from "@/api";
import {reactive, watch} from "vue";

export default {
  props: ['videoUrl', 'picUrl'],

  setup(props) {
    const videoInfo = reactive({
      url: props.videoUrl,
      pic: props.picUrl
    })
    watch(() => props.videoUrl, ()=> {videoInfo.url = props.videoUrl; videoInfo.pic = props.picUrl})

    //可打印出当前所触发的事件
    const handleEvent = (log) => {
      console.log('Basic player event', log)
    }

    return {
      handleEvent,
      attachImageUrl: HttpManager.attachImageUrl,
      videoInfo,
    }
  }
}
</script>

<style scoped>
.video-player {
  background-color: black;
}
</style>