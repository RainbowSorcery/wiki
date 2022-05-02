<template>
  <a-layout-header class="header">
    <div class="logo" />
    <a-menu
      theme="dark"
      mode="horizontal"
      :style="{ lineHeight: '64px' }"
    >
      <a-menu-item key="/">
        <router-link to="/">首页</router-link>
      </a-menu-item>
      <a-menu-item
        key="/admin/user"
        v-if="user.userType === '0'"
      >
        <router-link to="/admin/user">用户管理</router-link>
      </a-menu-item>

      <a-menu-item
        key="/userCollectInfo"
        v-if="user.loginName"
      >
        <router-link to="/userCollectInfo">收藏管理</router-link>
      </a-menu-item>
      <a-menu-item
        key="/admin/ebook"
        v-if="user.userType === '0'"
      >
        <router-link to="/admin/ebook">电子书管理</router-link>
      </a-menu-item>
      <a-menu-item
        key="/admin/category"
        v-if="user.userType === '0'"
      >
        <router-link to="/admin/category">分类管理</router-link>
      </a-menu-item>

      <a-menu-item
        key="/login"
        v-if="!user.loginName"
      >
        <a @click="login">
          <span>登录</span>
        </a>
      </a-menu-item>

      <a-menu-item
        key="/register"
        v-if="!user.loginName"
      >
        <a @click="viewRegisterModel">
          <span>注册</span>
        </a>
      </a-menu-item>

      <a-menu-item
        key="/hello"
        v-if="user.loginName"
      >
        <a>
          <span>欢迎您, {{user.name}}</span>
        </a>
      </a-menu-item>
      <a-menu-item
        key="/logout"
        v-if="user.loginName"
      >
        <a @click="logout">
          <span>退出登录</span>
        </a>
      </a-menu-item>
      <a-menu-item key="/about">
        <router-link to="/about">关于我们</router-link>
      </a-menu-item>
    </a-menu>

    <a-modal
      v-model:visible="showLoginView"
      title="登录"
      @ok="loginOkHandle"
    >
      <a-form :model="loginSubmitObject">
        <a-form-item label="登录名">
          <a-input v-model:value="loginSubmitObject.loginName" />
        </a-form-item>
        <a-form-item label="密码">
          <a-input
            type="password"
            v-model:value="loginSubmitObject.password"
          />
        </a-form-item>

        <a-form-item label="用户类型">
          <a-radio-group v-model:value="loginSubmitObject.loginType">
            <a-radio :value="1">普通用户</a-radio>
            <a-radio :value="0">管理员</a-radio>
          </a-radio-group>
        </a-form-item>

        <a-form-item label="验证码">
          <a-input
            type="text"
            v-model:value="loginSubmitObject.captcha"
          />

          <img
            :src="captchaUri"
            @click="flushCaptch"
          />
        </a-form-item>
      </a-form>
    </a-modal>

    <a-modal
      v-model:visible="showRegisterView"
      title="注册"
      @ok="register"
    >
      <a-form :model="registerObject">
        <a-form-item label="登录名">
          <a-input v-model:value="registerObject.loginName" />
        </a-form-item>
        <a-form-item label="昵称">
          <a-input v-model:value="registerObject.name" />
        </a-form-item>
        <a-form-item label="密码">
          <a-input
            type="password"
            v-model:value="registerObject.password"
          />
        </a-form-item>
        <a-form-item label="再次输入密码">
          <a-input
            type="password"
            v-model:value="registerObject.repeatPassword"
          />
        </a-form-item>

        <a-form-item label="验证码">
          <a-input
            type="text"
            v-model:value="registerObject.captcha"
          />

          <img
            :src="captchaUri"
            @click="flushCaptch"
          />
        </a-form-item>
      </a-form>
    </a-modal>

  </a-layout-header>
</template>

<script lang="ts">
import store from "@/store";
import { message } from "ant-design-vue";
import axios from "axios";
import { defineComponent, ref, computed } from "vue";

export default defineComponent({
  name: "the-header",
  setup() {
    const showLoginView = ref(false);

    const loginSubmitObject = ref({
      loginName: "Lyra",
      password: "365373011",
      captcha: "",
      loginType: 0,
    });

    const user = computed(() => store.state.user);

    const login = () => {
      showLoginView.value = true;
    };

    const loginOkHandle = () => {
      axios.post("/user/login", loginSubmitObject.value).then((response) => {
        if (response.data.success) {
          showLoginView.value = false;
          store.commit("setUser", response.data.data);
        } else {
          // 弹出登录失败消息并刷新验证码
          message.error(response.data.message);
          flushCaptch();
        }
      });
    };

    const logout = () => {
      axios.post("/user/logout/" + store.state.user.token).then((response) => {
        if (response.data.success) {
          message.success("退出登录成功");
          store.commit("setUser", {});
        } else {
          message.error("退出登录失败");
        }
      });
    };

    const captchaUri = ref("http://127.0.0.1:8080/captcha");

    // 刷新验证码
    const flushCaptch = () => {
      axios.get(captchaUri.value).then(() => {
        captchaUri.value =
          "http://127.0.0.1:8080/captcha?id=" + Math.random() * 10;
        loginSubmitObject.value.captcha = "";
      });
    };

    const showRegisterView = ref(false);

    const viewRegisterModel = () => {
      showRegisterView.value = true;
    };

    const registerObject = ref({
      loginName: "",
      name: "",
      password: "",
      repeatPassword: "",
      captcha: "",
    });

    const register = () => {
      if (
        registerObject.value.password === registerObject.value.repeatPassword
      ) {
        axios.post("/user/register", registerObject.value).then((response) => {
          if (response.data.success) {
            message.success("注册成功");
            showRegisterView.value = false;
          } else {
            message.error("注册失败," + response.data.message);
          }
        });
      } else {
        message.error("请输入重复密码");
      }
    };

    return {
      showLoginView,
      loginSubmitObject,
      user,
      captchaUri,
      showRegisterView,
      registerObject,
      flushCaptch,
      logout,
      loginOkHandle,
      login,
      viewRegisterModel,
      register,
    };
  },
});
</script>

<style scoped>
.login-menu {
  float: right !important;
}
</style>