<template>
  <el-breadcrumb class="crumbs" separator="/">
    <el-breadcrumb-item v-for="item in breadcrumbList" :key="item.name" :to="{ path: item.path, query: item.query }">
      {{ item.name }}
    </el-breadcrumb-item>
  </el-breadcrumb>

  <div class="container">
    <div class="handle-box">
      <el-button @click="deleteAll">批量删除</el-button>
      <el-input v-model="searchWord" placeholder="筛选关键词"></el-input>
      <el-button type="primary" @click="centerDialogVisible = true">添加视频</el-button>
    </div>
    <el-table height="680px" border size="16" :data="data" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="40"></el-table-column>
      <el-table-column label="ID" prop="id" width="50" align="center"></el-table-column>
      <el-table-column label="视频" align="center" width="400">
        <template v-slot="scope">
          <div style="width: 400px; height: 200px; overflow: hidden">
            <img :src="attachImageUrl(scope.row.pic)" style="width: 100%"/>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="视频名称" prop="name" width="200"></el-table-column>
      <el-table-column label="视频简介" prop="introduction"></el-table-column>
      <el-table-column label="资源更新" width="120" align="center">
        <template v-slot="scope">
          <el-upload :action="updateVideoImg(scope.row.id)" :show-file-list="false" :on-success="handleImgSuccess" :before-upload="beforeImgUpload">
            <el-button>更新图片</el-button>
          </el-upload>
          <br />
          <el-upload :action="updateVideoUrl(scope.row.id)" :show-file-list="false" :on-success="handleVideoSuccess" :before-upload="beforeVideoUpload">
            <el-button>更新视频</el-button>
          </el-upload>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="160" align="center">
        <template v-slot="scope">
          <el-button @click="editRow(scope.row)">编辑</el-button>
          <el-button type="danger" @click="deleteRow(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      class="pagination"
      background
      layout="prev, pager, next"
      :current-page="currentPage"
      :page-size="pageSize"
      :total="tableData.length"
      @current-change="handleCurrentChange"
    >
    </el-pagination>
  </div>

  <!--添加歌曲-->
  <el-dialog title="添加视频" v-model="centerDialogVisible" width="600px">
    <el-form id="add-video" label-width="120px" :model="registerForm">
      <el-form-item label="视频名称">
        <el-input type="text" name="name" v-model="registerForm.name"></el-input>
      </el-form-item>
      <el-form-item label="视频简介">
        <el-input v-model="editForm.introduction"></el-input>
      </el-form-item>
      <el-form-item label="视频上传">
        <input type="file" name="file" id="file"/>
      </el-form-item>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="centerDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="addVideo">确 定</el-button>
      </span>
    </template>
  </el-dialog>

  <!-- 编辑弹出框 -->
  <el-dialog title="编辑" v-model="editVisible" width="600px">
    <el-form :model="editForm">
      <el-form-item label="视频名称">
        <el-input v-model="editForm.name"></el-input>
      </el-form-item>
      <el-form-item label="视频简介">
        <el-input v-model="editForm.introduction"></el-input>
      </el-form-item>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="editVisible = false">取 消</el-button>
        <el-button type="primary" @click="saveEdit">确 定</el-button>
      </span>
    </template>
  </el-dialog>

  <!-- 删除提示框 -->
  <yin-del-dialog :delVisible="delVisible" @confirm="confirm" @cancelRow="delVisible = $event"></yin-del-dialog>
</template>

<script lang="ts">
import { defineComponent, getCurrentInstance, watch, ref, reactive, computed } from "vue";
import { useStore } from "vuex";
import mixin from "@/mixins/mixin";
import { HttpManager } from "@/api";
import { parseLyric } from "@/utils";
import YinDelDialog from "@/components/dialog/YinDelDialog.vue";

export default defineComponent({
  components: {
    YinDelDialog,
  },
  setup: function () {
    const {proxy} = getCurrentInstance();
    const {routerManager, beforeImgUpload, beforeVideoUpload} = mixin();
    const store = useStore();

    const tableData = ref([]); // 记录歌曲，用于显示
    const tempDate = ref([]); // 记录歌曲，用于搜索时能临时记录一份歌曲列表
    const pageSize = ref(5); // 页数
    const currentPage = ref(1); // 当前页
    const singerId = ref("");
    const singerName = ref("");
    const breadcrumbList = computed(() => store.getters.breadcrumbList);
    // 计算当前表格中的数据
    const data = computed(() => {
      return tableData.value.slice((currentPage.value - 1) * pageSize.value, currentPage.value * pageSize.value);
    });

    const searchWord = ref(""); // 记录输入框输入的内容
    watch(searchWord, () => {
      if (searchWord.value === "") {
        tableData.value = tempDate.value;
      } else {
        tableData.value = [];
        for (let item of tempDate.value) {
          if (item.name.includes(searchWord.value)) {
            tableData.value.push(item);
          }
        }
      }
    });

    singerId.value = proxy.$route.query.id as string;
    singerName.value = proxy.$route.query.name as string;
    getData();

    // 获取视频
    async function getData() {
      tableData.value = [];
      tempDate.value = [];
      let id = singerId.value
      const result = (await HttpManager.getVideoList({"singerId" : id, "name" : null})) as ResponseBody;
      tableData.value = result.data;
      tempDate.value = result.data;
      currentPage.value = 1;
    }

    // 更新图片
    function updateVideoImg(id) {
      return HttpManager.updateVideoImg(id);
    }

    function updateVideoUrl(id) {
      return HttpManager.updateVideoUrl(id);
    }

    // 获取当前页
    function handleCurrentChange(val) {
      currentPage.value = val;
    }

    function handleVideoSuccess(res) {
      (proxy as any).$message({
        message: res.message,
        type: res.type,
      });
      if (res.success) getData();
    }

    // 更新图片
    function handleImgSuccess(res, file) {
      (proxy as any).$message({
        message: res.message,
        type: res.type,
      });
      if (res.success) getData();
    }

    /**
     * 添加
     */
    const centerDialogVisible = ref(false);
    const registerForm = reactive({
      name: "",
      singerName: "",
      introduction: "",
      lyric: "",
    });

    function addVideo() {
      const addSongForm = new FormData(document.getElementById("add-video") as HTMLFormElement);
      addSongForm.append("singerId", singerId.value);
      addSongForm.set("name", singerName.value + "-" + addSongForm.get("name"));

      const req = new XMLHttpRequest();
      req.onreadystatechange = () => {
        if (req.readyState === 4 && req.status === 200) {
          let res = JSON.parse(req.response);
          (proxy as any).$message({
            message: res.message,
            type: res.type,
          });
          if (res.success) {
            getData();
            registerForm.name = "";
            registerForm.singerName = "";
            registerForm.introduction = "";
          }
        }
      };
      req.open("post", HttpManager.attachImageUrl(`/video/add`), false);
      req.send(addSongForm);
      (document.getElementById("file") as HTMLFormElement).value = "";
      centerDialogVisible.value = false;
    }

    /**
     * 编辑
     */
    const editVisible = ref(false);
    const editForm = reactive({
      id: "",
      singerId: "",
      name: "",
      introduction: "",
      createTime: "",
      updateTime: "",
      pic: "",
      url: "",
    });

    function editRow(row) {
      idx.value = row.id;
      editForm.id = row.id;
      editForm.singerId = row.singerId;
      editForm.name = row.name;
      editForm.introduction = row.introduction;
      editForm.createTime = row.createTime;
      editForm.updateTime = row.updateTime;
      editForm.pic = row.pic;
      editForm.url = row.url;
      editVisible.value = true;
    }

    async function saveEdit() {
      let id = editForm.id;
      let singerId = editForm.singerId;
      let name = editForm.name;
      let introduction = editForm.introduction;
      const result = (await HttpManager.updateVideoMsg({id, singerId, name, introduction})) as ResponseBody;
      (proxy as any).$message({
        message: result.message,
        type: result.type,
      });
      if (result.success) getData();
      editVisible.value = false;
    }

    /**
     * 删除
     */
    const idx = ref(-1); // 记录当前要删除的行
    const multipleSelection = ref([]); // 记录当前要删除的列表
    const delVisible = ref(false); // 显示删除框

    async function confirm() {
      const result = (await HttpManager.deleteVideo(idx.value)) as ResponseBody;
      (proxy as any).$message({
        message: result.message,
        type: result.type,
      });
      if (result.success) getData();
      delVisible.value = false;
    }

    function deleteRow(id) {
      idx.value = id;
      delVisible.value = true;
    }

    function handleSelectionChange(val) {
      multipleSelection.value = val;
    }

    function deleteAll() {
      for (let item of multipleSelection.value) {
        deleteRow(item.id);
        confirm();
      }
      multipleSelection.value = [];
    }

    return {
      searchWord,
      data,
      editForm,
      registerForm,
      tableData,
      centerDialogVisible,
      editVisible,
      delVisible,
      pageSize,
      currentPage,
      breadcrumbList,
      deleteAll,
      handleSelectionChange,
      handleCurrentChange,
      handleImgSuccess,
      beforeImgUpload,
      parseLyric,
      saveEdit,
      updateVideoImg,
      updateVideoUrl,
      deleteRow,
      confirm,
      attachImageUrl: HttpManager.attachImageUrl,
      addVideo,
      editRow,
      handleVideoSuccess,
      beforeVideoUpload,
    };
  },
});
</script>

<style scoped>
.play {
  position: absolute;
  z-index: 100;
  width: 80px;
  height: 80px;
  top: 18px;
  left: 15px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
}
.icon {
  width: 2em;
  height: 2em;
  color: white;
  fill: currentColor;
  overflow: hidden;
}
</style>
