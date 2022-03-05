<template>
  <a-layout>
    <the-header></the-header>
    <router-view />
    <the-footer></the-footer>
  </a-layout>
</template>
<script lang="ts">
import { defineComponent, ref } from 'vue';
import TheHeader from '@/components/TheHeader.vue'
import TheFooter from '@/components/TheFooter.vue'

export default defineComponent({
  components: {
    TheHeader,
    TheFooter
  },
  setup() {

    return {
      selectedKeys1: ref<string[]>(['2']),
    };
  },
});
</script>
<style>
#components-layout-demo-top-side-2 .logo {
  float: left;
  width: 120px;
  height: 31px;
  margin: 16px 24px 16px 0;
  background: rgba(255, 255, 255, 0.3);
}

.ant-row-rtl #components-layout-demo-top-side-2 .logo {
  float: right;
  margin: 16px 0 16px 24px;
}

.site-layout-background {
  background: #fff;
}
</style>