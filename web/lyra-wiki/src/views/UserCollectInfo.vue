<template>
  <a-layout-content :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }">
    <a-table
      :columns="columns"
      :data-source="collectList"
    >
      <template #ebook="{text}">
        <router-link :to="'/doc?ebookId=' + text.ebookId">{{text.ebookName}}</router-link>
      </template>
      <template #action="{text}">
        <a-popconfirm
          title="确认删除该收藏吗?"
          ok-text="是"
          cancel-text="否"
          @confirm="deleteCollect(text.id)"
        >
          <a-button
            type="primary"
            danger
          >删除</a-button>
        </a-popconfirm>

      </template>
    </a-table>
  </a-layout-content>
</template>

<script lang="ts">
import store from "@/store";
import { message } from "ant-design-vue";
import axios from "axios";
import { computed, defineComponent, onMounted, ref } from "vue";

const columns = [
  {
    title: "id",
    name: "id",
    dataIndex: "id",
    key: "id",
  },
  {
    title: "用户名称",
    dataIndex: "name",
    key: "name",
  },
  {
    title: "电子书名称",
    key: "ebookName",
    slots: { customRender: "ebook" },
  },
  {
    title: "简介",
    key: "description",
    dataIndex: "description",
  },
  {
    title: "操作",
    key: "action",
    slots: { customRender: "action" },
  },
];

export default defineComponent({
  name: "userCollectInfo",
  setup() {
    const user = computed(() => store.state.user);

    const collectList = ref();

    const getCollectList = (userId: any) => {
      axios.get("/collect/getCollectList?userId=" + userId).then((response) => {
        collectList.value = response.data.data;
      });
    };

    const deleteCollect = (collectId: string) => {
      axios
        .post("/collect/deleteCollectById?collectId=" + collectId)
        .then((response) => {
          if (response.data.success) {
            message.success("删除成功");
            getCollectList(user.value.id)
          } else {
            message.error("删除失败");
          }
        });
    };

    onMounted(() => {
      getCollectList(user.value.id);
    });

    return {
      columns,
      collectList,
      deleteCollect,
    };
  },
});
</script>
