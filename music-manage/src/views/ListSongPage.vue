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
      <el-button type="primary" @click="centerDialogVisible = true">添加歌曲</el-button>
    </div>
    <el-table height="680px" border size="16" :data="tableData" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="40" align="center"></el-table-column>
      <el-table-column label="ID" prop="id" width="50" align="center"></el-table-column>
      <el-table-column label="歌手-歌曲" prop="name"></el-table-column>
      <el-table-column label="操作" width="90" align="center">
        <template v-slot="scope">
          <el-button type="danger" @click="deleteRow(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>

  <!--添加歌曲-->
  <el-dialog title="添加歌曲" v-model="centerDialogVisible" width="30%">
    <el-form label-width="180px" :model="registerForm">
      <el-form-item prop="singerName" label="歌手名字">
        <el-select v-model="registerForm.singerName" placeholder="请选择" @change="chooseSinger">
          <el-option
              v-for="item in singerList"
              :key="item.value"
              :label="item.label"
              :value="item.value">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item prop="songName" label="歌曲名字">
        <el-cascader
            v-model="registerForm.songIds"
            :options="songList"
            :props="{ expandTrigger: 'hover', multiple: true }"
            placeholder="请选择"
            :show-all-levels="false"
            @change="chooseSong"
        ></el-cascader>
      </el-form-item>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="centerDialogVisible = false;
        registerForm.singerName = '';
        registerForm.songIds = [];
        songList.length = 0">取 消</el-button>
        <el-button type="primary" @click="addSong">确 定</el-button>
      </span>
    </template>
  </el-dialog>

  <!-- 删除提示框 -->
  <yin-del-dialog :delVisible="delVisible" @confirm="confirm" @cancelRow="delVisible = $event"></yin-del-dialog>
</template>

<script lang="ts">
import {defineComponent, getCurrentInstance, watch, ref, reactive, computed} from "vue";
import {useStore} from "vuex";
import {HttpManager} from "@/api";
import YinDelDialog from "@/components/dialog/YinDelDialog.vue";

export default defineComponent({
  components: {
    YinDelDialog,
  },
  setup() {
    const {proxy} = getCurrentInstance();
    const store = useStore();

    const tableData = ref([]); // 记录歌曲，用于显示
    const tempDate = ref([]); // 记录歌曲，用于搜索时能临时记录一份歌曲列表
    const breadcrumbList = computed(() => store.getters.breadcrumbList);

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

    getData();

    // 获取歌单
    async function getData() {
      tableData.value = [];
      tempDate.value = [];
      const result = (await HttpManager.getListSongOfSongId(proxy.$route.query.id)) as ResponseBody;
      for (let item of result.data) {
        const result = await HttpManager.getSongOfId(item.songId) as ResponseBody;
        tableData.value.push(result.data[0]);
        tempDate.value.push(result.data[0]);
      }
    }

    /**
     * 添加
     */
    const centerDialogVisible = ref(false);
    const registerForm = reactive({
      singerName: "",
      songIds: [],
    });

    async function addSong() {
      let songId = null;
      let songIds = registerForm.songIds;
      let songListId = proxy.$route.query.id as string;

      const result = (await HttpManager.setListSong({songId, songIds, songListId})) as ResponseBody;
      (proxy as any).$message({
        message: result.message,
        type: result.type,
      });

      if (result.success) getData();
      registerForm.singerName = '';
      registerForm.songIds = [];
      songList.length = 0;
      centerDialogVisible.value = false;
    }

    /**
     * 删除
     */
    const idx = ref(-1); // 记录当前要删除的行
    const multipleSelection = ref([]); // 记录当前要删除的列表
    const delVisible = ref(false); // 显示删除框

    async function confirm() {
      const result = await HttpManager.deleteListSong(idx.value) as ResponseBody;
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

    const singerList = []; // 歌手列表
    getSingerList();

    async function getSingerList() {
      const result = (await HttpManager.getAllSinger()) as ResponseBody;
      for (let item of result.data) {
        let singer = {"value": item.id, "label": item.name};
        singerList.push(singer);
      }
    }

    function chooseSinger(value) {
      getSongList(value);
    }

    const songList = reactive([]);//歌曲列表
    function getSongList(id) {
      songList.length = 0;
      HttpManager.getSongTreeOfSingerId(id).then((res) => {
        const result = res as ResponseBody;
        for (let item of result.data) {
          songList.push(item);
        }
      });
    }

    function chooseSong(value: Array<number|string>) {
      registerForm.songIds = [];
      for (let item of value) {
        registerForm.songIds.push(item[1]);
      }
    }

    return {
      searchWord,
      tableData,
      delVisible,
      centerDialogVisible,
      registerForm,
      breadcrumbList,
      deleteAll,
      handleSelectionChange,
      deleteRow,
      confirm,
      singerList,
      songList,
      chooseSinger,
      addSong,
      chooseSong,
    };
  },
});
</script>

<style scoped>
  ul, li {
    list-style: none;
    display: block;
  }
</style>
