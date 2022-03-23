<template>
  <a-layout-content :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }">

    <a-row :gutter="24">
      <a-col :span="8">
        <p>
          <a-space size="small">
            <a-input
              size="small"
              v-model:value="queryCondition"
              placeholder="small size"
            />
            <a-button
              type="primary"
              @click="queryDocCondtionList"
            >查询</a-button>
            <a-button
              type="primary"
              @click="addDoc"
            >新增电子书</a-button>
          </a-space>

        </p>

        <a-table
          v-if="viewTable"
          :columns="columns"
          :data-source="Doc"
          :row-key="setRowKey"
          :pagination="false"
          :defaultExpandAllRows="true"
          @change="handleTableChange"
        >

          <template #action="{text}">
            <a-space size="small">
              <a-button
                type="primary"
                @click="regeditDoc(text)"
                :size="'small'"
              >编辑</a-button>
              <a-button
                danger
                :size="'small'"
                @click="deleteDoc(text)"
              >删除</a-button>
            </a-space>
          </template>
        </a-table>
      </a-col>
      <a-col :span="16">
        <p>
          <a-space size="small">
            <a-button
              type="primary"
              @click="handleOk"
            >添加</a-button>
          </a-space>
        </p>

        <a-form
          :model="sumbitDoc"
          :label-col="{ span: 4 }"
          :wrapper-col="{ span: 14 }"
        >
          <a-form-item>
            <a-input
              v-model:value="sumbitDoc.name"
              placeholder="名称"
            />
          </a-form-item>
          <a-form-item>
            <a-tree-select
              v-model:value="sumbitDoc.parent"
              style="width: 100%"
              :dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
              :tree-data="selectTreeData.selectedData"
              placeholder="请选择"
              tree-default-expand-all
              :replaceFields="{children:'children', title:'name', key:'id', value: 'id'}"
            >
              <template #title="{ key, value }">
                <span
                  style="color: #08c"
                  v-if="key === '0-0-1'"
                >Child Node1 {{ value }}</span>
              </template>
            </a-tree-select>
          </a-form-item>
          <a-form-item>
            <a-input
              v-model:value="sumbitDoc.sort"
              placeholder="排序"
            />
          </a-form-item>
          <a-form-item>
            <div id="content"></div>
          </a-form-item>
        </a-form>
      </a-col>
    </a-row>

    <!-- <a-modal v-model:visible="visible" title="Basic Modal" @ok="handleOk">
     
    </a-modal> -->
  </a-layout-content>
</template>

<script lang="ts">
import { defineComponent, ref, reactive, onMounted, toRaw } from "vue";
import { message } from "ant-design-vue";
import axios from "axios";
import { useRoute } from "vue-router";
import E from "wangeditor";

const columns = [
  {
    dataIndex: "name",
    key: "name",
    title: "名称",
    // 表头渲染成customTitle 值渲染成customRender
    slots: { customRender: "name" },
  },
  {
    title: "action",
    key: "action",
    slots: { customRender: "action" },
  },
];

type DocObject = {
  id: string;
  ebookId: string | undefined;
  parent: bigint | undefined;
  name: string;
  sort: number | undefined;
  disabled?: boolean;
  viewCount?: number;
  vote_count?: number;
  content?: string;
};

export default defineComponent({
  name: "AdminDoc",
  setup() {
    const Doc = ref();

    // 提交对象
    const sumbitDoc: DocObject = reactive({
      id: "",
      ebookId: undefined,
      name: "",
      parent: undefined,
      sort: undefined,
    });

    // 显示弹出框
    const visible = ref(false);

    let sumbitType = "add";

    // 分页查询
    const queryDocList = () => {
      axios.get("/doc/list/tree").then((response) => {
        Doc.value = response.data.data;
        viewTable.value = true;
      });
    };

    const getSelectedTreeDataList = (id?: any) => {
      axios.get("/doc/getSelectedTreeData?id=" + id).then((response) => {
        selectTreeData.selectedData = response.data.data;
      });
    };

    const router = useRoute();

    const regeditDoc = (parm: DocObject) => {
      getContentByDocId(toRaw(parm).id)
      selectTreeData.selectedData = [];
      console.log(parm);
      getSelectedTreeDataList(toRaw(parm).id + "");
      callBackSubmitDoc();

      visible.value = true;
      sumbitType = "update";
      axios.get("/doc/getDocById?id=" + toRaw(parm).id).then((response) => {
        sumbitDoc.id = response.data.data.id;
        sumbitDoc.parent = response.data.data.parent;
        sumbitDoc.name = response.data.data.name;
        sumbitDoc.sort = response.data.data.sort;
      });
    };

    const getContentByDocId = (docId: string) => {
      axios.get("/content/getContentById?id=" + docId).then((response) => {
        editor.txt.html(response.data.data.content);
      });
    };

    const callBackSubmitDoc = () => {
      sumbitDoc.ebookId = undefined;
      sumbitDoc.id = undefined;
      sumbitDoc.parent = undefined;
      sumbitDoc.name = "";
      sumbitDoc.sort = undefined;
    };

    const handleTableChange = () => {
      queryDocList();
    };

    let editor: any;

    onMounted(() => {
      editor = new E("#content");
      editor.config.zIndex = 0;
      editor.create();
      queryDocList();
      getParentDoc();
      getSelectedTreeDataList("");
    });

    const setRowKey = (record: DocObject) => {
      return toRaw(record).id;
    };

    const addDoc = () => {
      callBackSubmitDoc();
      getSelectedTreeDataList("");
      visible.value = true;
      sumbitType = "add";
    };

    const add = (sumbitBook: DocObject) => {
      sumbitBook.content = editor.txt.html();
      sumbitBook.ebookId = router.query.ebookId + "";
      axios.post("/doc/addDoc", sumbitBook).then(() => {
        message.success("添加成功", 10);
        queryDocList();
        f;
      });
    };

    const update = (sumbitBook: DocObject) => {
      sumbitBook.content = editor.txt.html();
      axios.post("/doc/updateDoc", sumbitBook).then((response) => {
        if (response.data.success) {
          message.success("更新成功", 10);
          queryDocList();
        } else {
          message.error("更新失败, 父分类下有子分类存在");
        }
      });
    };

    // 弹出框之后点击ok之后的回调函数
    const handleOk = () => {
      if (sumbitType === "add") {
        add(sumbitDoc);
        queryDocList();
        visible.value = false;
      } else if (sumbitType === "update") {
        update(sumbitDoc);
        queryDocList();
        visible.value = false;
      }
    };

    const queryDocCondtionList = () => {
      queryDocList();
    };

    const queryCondition = ref();

    const parentDoc = ref();

    const getParentDoc = () => {
      axios.get("/doc/getParentDoc").then((response) => {
        parentDoc.value = response.data.data;
      });
    };

    const selectTreeData = reactive({
      selectedData: Array<DocObject>(),
    });
    selectTreeData.selectedData = [];

    const deleteDoc = (doc: DocObject) => {
      const docTowRaw = toRaw(doc);
      axios.post("/doc/deleteDoc?id=" + docTowRaw.id).then(() => {
        queryDocList();
      });
    };

    // 当表中有请求到数据时 显示表格 添加的原因为 若表中无数据 则无法展开所有表格
    const viewTable = ref(false);

    return {
      viewTable,
      selectTreeData,
      deleteDoc,
      Doc,
      sumbitDoc,
      columns,
      visible,
      queryCondition,
      parentDoc,
      setRowKey,
      regeditDoc,
      handleTableChange,
      handleOk,
      addDoc,
      queryDocCondtionList,
    };
  },
});
</script>

<style scoped>
</style>
