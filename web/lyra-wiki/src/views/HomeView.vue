<script lang="ts">
import {
  FileOutlined,
  UserOutlined,
} from "@ant-design/icons-vue";
import {
  LikeOutlined,
} from "@ant-design/icons-vue";
import { defineComponent, ref, reactive, toRef, onMounted } from "vue";
import axios from "axios";

export default defineComponent({
  components: {
    FileOutlined,
    LikeOutlined,
    UserOutlined,
  },
  setup() {
    const eBook = ref();
    const ebboReact = reactive({ book: [] });
    axios.get("ebook/list").then((response) => {
      eBook.value = response.data.data;
      ebboReact.book = response.data.data;
    });

    onMounted(() => {
      getCcategoryTreeList();
    });

    const titleClick = (e: Event) => {
      console.log(e);
    };
    Event;
    const handleClick = (e: Event) => {
      if (e.key !== "welcome") {
        axios
          .get("/ebook/getEbookByCategoryId?id=" + e.key)
          .then((response) => {
            isWelcome.value = false;
            eBook.value = response.data.data;
          });
      } else {
        isWelcome.value = true;
      }
    };

    const isWelcome = ref(true);

    const categoryTreeList = ref();

    const getCcategoryTreeList = () => {
      axios.get("/category/list/tree").then((response) => {
        categoryTreeList.value = response.data.data;
      });
    };

    return {
      isWelcome,
      selectedKeys2: ref<string[]>(["1"]),
      openKeys: ref<string[]>(["sub1"]),
      categoryTreeList,
      eBook,
      titleClick,
      handleClick,
    };
  },
});
</script>

<template>
  <a-layout>
    <a-layout-sider
      width="200"
      style="background: #fff"
    >
      <a-menu
        mode="inline"
        v-model:selectedKeys="selectedKeys2"
        v-model:openKeys="openKeys"
        :style="{ height: '100%', borderRight: 0 }"
        @click="handleClick"
      >
        <a-menu-item key="welcome">
          <template #icon>
            <PieChartOutlined />
          </template>
          <span>欢迎</span>
        </a-menu-item>
        <a-sub-menu
          @titleClic="titleClick"
          :key="category.id"
          v-for="category in categoryTreeList"
        >
          <template #icon>
            <MailOutlined />
          </template>
          <template #title>{{category.name}}</template>
          <a-menu-item-group :key="category.id">
            <template #icon>
              <QqOutlined />
            </template>
            <a-menu-item
              :key="child.id"
              v-for="child in category.children"
            >
              {{child.name}}
            </a-menu-item>
          </a-menu-item-group>
        </a-sub-menu>

      </a-menu>
    </a-layout-sider>
    <a-layout style="padding: 0 24px 24px">
      <a-layout-content :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }">
        <a-list
          v-if="!isWelcome"
          item-layout="vertical"
          size="large"
          :data-source="eBook"
          :grid="{ gutter: 20, column: 3 }"
        >
          <template #renderItem="{ item }">
            <a-list-item key="item.name">
              <template #actions>
                <span
                  :key="'FileOutlined'"
                >
                  <component
                    v-bind:is="'FileOutlined'"
                    style="margin-right: 8px"
                  />
                  {{ item.docCount }}
                </span>
                <span
                  :key="'LikeOutlined'"
                >
                  <component
                    v-bind:is="'LikeOutlined'"
                    style="margin-right: 8px"
                  />
                  {{ item.voteCount }}
                </span>
                <span
                  :key="'UserOutlined'"
                >
                  <component
                    v-bind:is="'UserOutlined'"
                    style="margin-right: 8px"
                  />
                  {{ item.viewCount }}
                </span>
              </template>
              <a-list-item-meta :description="item.description">
                <template #title>
                  <router-link :to="'/doc?ebookId=' + item.id">{{ item.name }}</router-link>
                </template>
                <template #avatar>
                  <a-avatar :src="item.avatar" />
                </template>
              </a-list-item-meta>
            </a-list-item>
          </template>
        </a-list>

        <div
          class="welcome"
          v-if="isWelcome"
        >
          <h1>欢迎使用wiki知识库系统</h1>
        </div>

      </a-layout-content>

    </a-layout>
  </a-layout>
</template>

<style scoped>
.ant-avatar {
  width: 50px;
  height: 50px;
  line-height: 50px;
  border-radius: 8%;
  margin: 5px 0;
}
</style>