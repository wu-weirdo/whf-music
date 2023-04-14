<template>
  <el-container>
    <el-aside class="album-slide">
      <div style="width: 200px">
        <el-tree
            :data="singerVideo"
            highlight-current
            default-expand-all
            @node-click="handleNodeClick"/>
      </div>
    </el-aside>
    <el-main class="album-main">
      <video-player :videoUrl="videoUrl" :picUrl="picUrl"></video-player>
    </el-main>
  </el-container>
</template>

<script lang="ts">
import {defineComponent, ref,} from "vue";
import {HttpManager} from "@/api";
import {ElTree} from "element-plus";
import VideoPlayer from "@/views/video/VideoPlayer.vue";
import { Tree } from "element-plus/es/components/tree-v2/src/types";

export default defineComponent({
  components: {
    VideoPlayer
  },
  props: ['id'],
  setup: function (props) {
    const singerVideo = ref([]);
    const videoUrl = ref("");
    const picUrl = ref("");
    const handleNodeClick = async (tree: Tree, data: any) => {
      if(data.level == 1) {
        return;
      }
      await videoDetail(data.data.value);
    }

    getData();

    // 获取视频
    async function getData() {
      const result = (await HttpManager.getSingerVideoTree()) as ResponseBody;
      singerVideo.value = result.data;
      if (props.id) {
        await videoDetail(props.id);
      } else {
        await videoDetail(result.data[0].value);
      }
    }

    async function videoDetail(id) {
      const result = (await HttpManager.getVideoDetail(id)) as ResponseBody;
      videoUrl.value = result.data.url;
      picUrl.value = result.data.pic;
    }

    return {
      singerVideo,
      handleNodeClick,
      videoUrl,
      picUrl
    };
  },
});
</script>

<style lang="scss" scoped>
@import "@/assets/css/var.scss";

.album-slide {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding-top: 20px;

  .album-info {
    width: 60%;
    padding-top: 2rem;

    li {
      width: 100%;
      height: 30px;
      line-height: 30px;
    }
  }
}

.album-main {
  p {
    color: rgba(0, 0, 0, 0.5);
    margin: 10px 0 20px 0px;
  }
}

@media screen and (min-width: $sm) {
  .album-slide {
    position: fixed;
    width: 400px;
  }
  .album-main {
    min-width: 600px;
    padding-right: 10vw;
    margin-left: 400px;
  }
}

@media screen and (max-width: $sm) {
  .album-slide {
    display: none;
  }
}
</style>
