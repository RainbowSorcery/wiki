<template>
  <a-layout>
    <a-layout-content :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }">
      <a-row>
        <a-col :span="6">
          <div v-if="!viewDocTreeData">
            <p>对不起,找到不到当前文档</p>
          </div>
          <a-tree
            v-if="viewDocTreeData"
            :defaultExpandAll="true"
            :tree-data="docTreeData"
            :replaceFields="{children: 'children', title: 'name', key: 'id'}"
            :selectedKeys="selectedKeys.selectedKeys"
            @select="selectTree"
          >
          </a-tree>
        </a-col>
        <a-col :span="18">
          <div v-html="html">

          </div>
        </a-col>
      </a-row>
    </a-layout-content>
  </a-layout>
</template>

<script lang="ts">
import axios from "axios";
import { defineComponent, ref, reactive, toRef, onMounted } from "vue";
import { useRoute } from "vue-router";

export default defineComponent({
  name: "doc-view",
  setup() {
    const docTreeData = ref();
    const viewDocTreeData = ref(false);
    const html = ref();
    const selectedKeys = reactive({
      selectedKeys: Array<string>(),
    });

    const route = useRoute();

    // 获取文档树
    const selectDocTreeData = (ebookiId: string) => {
      axios.get("/doc/list/tree?ebookId=" + ebookiId).then((response) => {
        docTreeData.value = response.data.data;
        // 若文档树为空 则不需要选中首个节点
        if (response.data.data != null) {
          selectedKeys.selectedKeys = [response.data.data[0].id];
          selectContentById(response.data.data[0].id);
        }
        viewDocTreeData.value = response.data.data.length > 0;
      });
    };

    // 查询被选中节点的内容
    const selectTree = (data: Array<string>) => {
      if (data.length > 0) {
        selectContentById(data[0]);
      }
    };

    const selectContentById = (docId: string) => {
      axios.get("/content/getContentById?id=" + docId).then((response) => {
        if (response.data.data != null) {
          html.value = response.data.data.content;
        } else {
          html.value = "";
        }
      });
    };

    onMounted(() => {
      selectDocTreeData(route.query.ebookId + "");
    });

    return {
      docTreeData,
      viewDocTreeData,
      html,
      selectedKeys,
      selectTree,
    };
  },
});
</script>

<style scoped>
</style>