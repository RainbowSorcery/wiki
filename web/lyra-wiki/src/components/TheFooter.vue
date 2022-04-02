<script lang="ts">
import store from "@/store";
import { computed, defineComponent, onMounted } from "vue";
import { v4 as uuidv4 } from "uuid";
import { notification } from "ant-design-vue";

export default defineComponent({
  name: "the-footer",
  setup() {
    const user = computed(() => store.state.user);

    // webSocket配置
    let webSocket: WebSocket;
    let token;

    const onOpen = () => {
      console.log("webSocket连接成功, 状态码", webSocket.readyState);
    };

    const onClose = () => {
      console.log("webSocket关闭成功, 状态码", webSocket.readyState);
    };

    const onError = () => {
      console.log("webSocket发生错误, 状态码", webSocket.readyState);
    };

    const onMessage = (event: any) => {
      notification.open({
        message: "点赞通知",
        description: event.data,
        onClick: () => {
          console.log("Notification Clicked!");
        },
      });
      console.log("webSocket收到消息:", event.data);
    };

    const initWebsocket = () => {
      webSocket.onopen = onOpen;
      webSocket.onclose = onClose;
      webSocket.onerror = onError;
      webSocket.onmessage = onMessage;
    };

    onMounted(() => {
      webSocket = new WebSocket("ws://127.0.0.1:8080/websocket/" + uuidv4());
      initWebsocket();
    });
    return {
      user,
    };
  },
});
</script>

<template>
  <a-layout-footer style="text-align: center">
    Ant Design ©2018 Created by Ant UED {{user.name}}
  </a-layout-footer>
</template>

<style scoped>
</style>