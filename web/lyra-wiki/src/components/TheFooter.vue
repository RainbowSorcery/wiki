<script lang="ts">
import store from "@/store";
import { computed, defineComponent } from "vue";


export default defineComponent({
  name: "the-footer",
  setup() {
    const user = computed(() => store.state.user);

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