<template>
  <div class="wrapper">
    <div class="container">
      <div class=" login-left">
        <el-image fit="contain" :src="require('@/assets/images/lizhi.jpg')"/>
      </div>
      <div class="login-right">
        <el-form ref="signInForm" status-icon :model="registerForm" :rules="SignInRules" size="large">
          <h3 class="heading">保持理智</h3>
          <el-form-item prop="userName" style="width: 250px">
            <el-input placeholder="用户名" v-model="registerForm.userName"></el-input>
          </el-form-item>
          <el-form-item prop="password">
            <el-input type="password" placeholder="密码" v-model="registerForm.password"
                      @keyup.enter="handleLoginIn"></el-input>
          </el-form-item>
          <el-form-item class="sign-btn">
            <el-button @click="handleSignUp">注册</el-button>
            <el-button type="primary" @click="handleLoginIn">登录</el-button>
          </el-form-item>
        </el-form>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import {defineComponent, reactive, getCurrentInstance} from "vue";
import mixin from "@/mixins/mixin";
import {HttpManager} from "@/api";
import {NavName, RouterName, SignInRules} from "@/enums";

export default defineComponent({
  components: {},
  setup() {
    const {proxy} = getCurrentInstance();
    const {routerManager, changeIndex} = mixin();

    // 登录用户名密码
    const registerForm = reactive({
      userName: "",
      password: "",
    });

    async function handleLoginIn() {
      let canRun = true;
      (proxy.$refs["signInForm"] as any).validate((valid) => {
        if (!valid) return (canRun = false);
      });
      if (!canRun) return;


      try {
        const userName = registerForm.userName;
        const password = registerForm.password;
        const result = (await HttpManager.signIn({userName, password})) as ResponseBody;
        (proxy as any).$message({
          message: result.message,
          type: result.type,
        });

        if (result.success) {
          proxy.$store.commit("setUserId", result.data.user.id);
          proxy.$store.commit("setUsername", result.data.user.userName);
          proxy.$store.commit("setUserPic", result.data.user.avator);
          proxy.$store.commit("setUserIntroduction", result.data.user.introduction);
          proxy.$store.commit("setToken", result.data.token);
          changeIndex(NavName.Home);
          routerManager(RouterName.Home, {path: RouterName.Home});
        }
      } catch (error) {
        console.error(error);
      }
    }

    function handleSignUp() {
      routerManager(RouterName.SignUp, {path: RouterName.SignUp});
    }

    return {
      registerForm,
      SignInRules,
      handleLoginIn,
      handleSignUp,
    };
  },
});
</script>

<style lang="scss" scoped>
@import "@/assets/css/sign.scss";
@import "@/assets/css/login.scss";
</style>
