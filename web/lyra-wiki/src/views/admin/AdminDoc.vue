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
            >新增文档</a-button>
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
              <a-popconfirm
                title="确定删除吗?"
                ok-text="是"
                cancel-text="否"
                @confirm="deleteDoc(text)"
              >
                <a-button danger>删除</a-button>
              </a-popconfirm>
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
            >添加/修改</a-button>
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

          <p>
            <a-space size="small">
              <a-button
                v-if="sumbitDoc.id"
                type="primary"
                @click="openDrawer(sumbitDoc.id)"
              >历史记录</a-button>
            </a-space>
          </p>

          <a-drawer
            v-model:visible="drawerVisible"
            class="custom-class"
            title="历史快照"
            placement="right"
            :width="1000"
          >
            <a-row>
              <a-col :span="10">
                <div
                  v-bind:key="item.id"
                  v-for="item in contentSnapshotList.contentSnapshotList"
                >
                  <a @click="getContentSnapshotByDocIdAndDate(item.contentId, item.date)">{{item.date}}</a>
                </div>
                <p>
                  <a-space size="small">
                    <a-button
                      v-if="contentSapshotData"
                      type="primary"
                      @click="rollBackContent()"
                    >还原到这个版本</a-button>
                  </a-space>
                </p>
              </a-col>
              <a-col :span="14">
                <div v-html="contentSapshotData">
                </div>

              </a-col>
            </a-row>
          </a-drawer>
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

type ContentSnashot = {
  id?: string;
  date?: string;
  content?: string;
  contentId?: string;
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

    const route = useRoute();

    // 分页查询
    const queryDocList = () => {
      axios
        .get("/doc/list/tree?ebookId=" + route.query.ebookId)
        .then((response) => {
          Doc.value = response.data.data;
          viewTable.value = true;
        });
    };

    const getSelectedTreeDataList = (id?: any) => {
      axios
        .get(
          "/doc/getSelectedTreeData?id=" +
            id +
            "&ebookId=" +
            route.query.ebookId
        )
        .then((response) => {
          selectTreeData.selectedData = response.data.data;
        });
    };

    const router = useRoute();

    const regeditDoc = (parm: DocObject) => {
      getContentByDocId(toRaw(parm).id);
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
      sumbitDoc.id = "";
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

    const drawerVisible = ref(false);

    const contentSnapshotList = reactive({
      contentSnapshotList: Array<ContentSnashot>(),
    });

    // 打开快照框的时候向后台根据id搜索快照
    const openDrawer = (docId: string) => {
      clearContentSnapshot();
      axios
        .get("/content-snapshot/getContentSnapshotByDocId?docId=" + docId)
        .then((response) => {
          contentSnapshotList.contentSnapshotList = response.data.data;
        });
      drawerVisible.value = true;
    };

    const contentSapshotData = ref();

    // 被点击的对象
    let onClickContentSnapshotElement: ContentSnashot  = {
      contentId: '',
      date: ''
    }

    // 根据id和date获取被点击的快照
    const getContentSnapshotByDocIdAndDate = (docId: any, date: any) => {
      axios
        .get(
          "/content-snapshot/getContentSnapshotByDocIdAndDate?docId=" +
            docId +
            "&date=" +
            date
        )
        .then((response) => {
          onClickContentSnapshotElement.contentId = docId
          onClickContentSnapshotElement.date = date
          contentSapshotData.value = response.data.data.content;
          console.log(response.data);
        });
    };

    const clearContentSnapshot = () => {
      contentSnapshotList.contentSnapshotList = [];
      contentSapshotData.value = "";
    };

    const rollBackContent = () => {
      axios
        .post(
          "/content-snapshot/rollBackContent?docId=" + onClickContentSnapshotElement.contentId + "&date=" + onClickContentSnapshotElement.date
        )
        .then((response) => {
          if (response.data.success) {
            message.success("会滚成功");
          }
        });
    };

    return {
      viewTable,
      selectTreeData,
      deleteDoc,
      Doc,
      sumbitDoc,
      columns,
      visible,
      contentSapshotData,
      drawerVisible,
      queryCondition,
      contentSnapshotList,
      openDrawer,
      setRowKey,
      regeditDoc,
      handleTableChange,
      handleOk,
      addDoc,
      queryDocCondtionList,
      getContentSnapshotByDocIdAndDate,
      rollBackContent,
    };
  },
});
</script>

<style scoped>
</style>
