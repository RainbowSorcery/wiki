<template>
  <a-layout-content :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }">
    <p>
      <a-space size="small">
        <a-input
          size="small"
          v-model:value="queryCondition"
          placeholder="small size"
        />
        <a-button
          type="primary"
          @click="queryCategoryCondtionList"
        >查询</a-button>
        <a-button
          type="primary"
          @click="addCategory"
        >新增分类</a-button>
      </a-space>

    </p>

    <a-table
      :columns="columns"
      :data-source="Category"
      :row-key="setRowKey"
      :pagination="false"
      @change="handleTableChange"
    >

      <template #action="{text}">
        <a-space size="small">
          <a-button
            type="primary"
            @click="regeditCategory(text)"
          >编辑</a-button>

          <a-popconfirm
            title="确认删除分类吗？"
            ok-text="确认"
            @confirm="deleteCategory(text)"
            cancel-text="取消"
          >
            <a-button danger>删除</a-button>

          </a-popconfirm>
        </a-space>
      </template>
    </a-table>

    <a-modal
      v-model:visible="visible"
      title="Basic Modal"
      @ok="handleOk"
    >
      <a-form
        :model="sumbitCategory"
        :label-col="{ span: 4 }"
        :wrapper-col="{ span: 14 }"
      >
        <a-form-item label="名称">
          <a-input v-model:value="sumbitCategory.name" />
        </a-form-item>
        <a-form-item label="父分类">
          <a-select
            ef="select"
            v-model:value="sumbitCategory.parent"
            @focus="focus"
          >
            <a-select-option value="0">
              无
            </a-select-option>
            <a-select-option
              v-for="category in parentCategory"
              :key="category.id"
              :value="category.id"
              :disabled="sumbitCategory.id == category.id"
            >{{category.name}}</a-select-option>

          </a-select>
        </a-form-item>
        <a-form-item label="排序">
          <a-input v-model:value="sumbitCategory.sort" />
        </a-form-item>
      </a-form>
    </a-modal>
  </a-layout-content>
</template>

<script lang="ts">
import { defineComponent, ref, reactive, onMounted, toRaw } from "vue";
import { message } from "ant-design-vue";
import axios from "axios";
import useRoute from "@/router";

const columns = [
  {
    dataIndex: "name",
    key: "name",
    title: "名称",
    // 表头渲染成customTitle 值渲染成customRender
    slots: { customRender: "name" },
  },
  {
    title: "父分类",
    dataIndex: "parent",
    key: "parent",
  },
  {
    title: "顺序",
    dataIndex: "sort",
    key: "sort",
  },
  {
    title: "action",
    key: "action",
    slots: { customRender: "action" },
  },
];

type CategoryObject = {
  id: bigint | undefined;
  parent: bigint | undefined;
  name: string;
  sort: number | undefined;
};

export default defineComponent({
  name: "AdminCategory",
  setup() {
    const Category = ref();

    // 提交对象
    const sumbitCategory: CategoryObject = reactive({
      id: undefined,
      name: "",
      parent: undefined,
      sort: undefined,
    });

    // 显示弹出框
    const visible = ref(false);

    let sumbitType = "";

    // 分页查询
    const queryCategoryList = () => {
      axios.get("/category/list/tree").then((response) => {
        Category.value = response.data.data;
      });
    };

    const regeditCategory = (parm: CategoryObject) => {
      callBackSubmitCategory();
      visible.value = true;
      sumbitType = "update";
      axios
        .get("/category/getCategoryById?id=" + toRaw(parm).id)
        .then((response) => {
          sumbitCategory.id = response.data.data.id;
          sumbitCategory.parent = response.data.data.parent;
          sumbitCategory.name = response.data.data.name;
          sumbitCategory.sort = response.data.data.sort;
        });
    };

    const callBackSubmitCategory = () => {
      sumbitCategory.id = undefined;
      sumbitCategory.parent = undefined;
      sumbitCategory.name = "";
      sumbitCategory.sort = undefined;
    };

    const handleTableChange = () => {
      queryCategoryList();
    };

    onMounted(() => {
      queryCategoryList();
      getParentCategory();
    });

    const setRowKey = (record: CategoryObject) => {
      return toRaw(record).id;
    };

    const addCategory = () => {
      callBackSubmitCategory();
      visible.value = true;
      sumbitType = "add";
    };

    const add = (sumbitBook: CategoryObject) => {
      axios.post("/category/addCategory", sumbitBook).then(() => {
        message.success("添加成功", 10);
        queryCategoryList();
      });
    };

    const update = (sumbitBook: CategoryObject) => {
      axios.post("/category/updateCategory", sumbitBook).then((response) => {
        if (response.data.success) {
          message.success("更新成功", 10);
          queryCategoryList();
        } else {
          message.error("更新失败, 父分类下有子分类存在");
        }
      });
    };

    // 弹出框之后点击ok之后的回调函数
    const handleOk = () => {
      if (sumbitType === "add") {
        add(sumbitCategory);
        queryCategoryList();
        visible.value = false;
      } else if (sumbitType === "update") {
        update(sumbitCategory);
        queryCategoryList();
        visible.value = false;
      }
    };

    const queryCategoryCondtionList = () => {
      queryCategoryList();
    };

    const queryCondition = ref();

    const parentCategory = ref();

    const getParentCategory = () => {
      axios.get("/category/getParentCategory").then((response) => {
        parentCategory.value = response.data.data;
      });
    };


    const deleteCategory = (parm: any) => {
      axios.post("/category/delete?categoryId=" + toRaw(parm).id)
      // 刷新页面
      location.reload()
    }

    return {
      Category,
      sumbitCategory,
      columns,
      visible,
      queryCondition,
      parentCategory,
      deleteCategory,
      setRowKey,
      regeditCategory,
      handleTableChange,
      handleOk,
      addCategory,
      queryCategoryCondtionList,
    };
  },
});
</script>

<style scoped>
</style>
