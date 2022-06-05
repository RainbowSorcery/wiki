<template>
  <a-layout-content :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }">
    <p>
      <a-space size="small">
        <a-input
          size="small"
          v-model:value="queryCondition"
          placeholder="请输入用户昵称"
        />
        <a-button
          type="primary"
          @click="queryUserCondtionList"
        >查询</a-button>
        <a-button
          type="primary"
          @click="addUser"
        >新增用户</a-button>
      </a-space>
    </p>
    <a-table
      :columns="columns"
      :data-source="users"
      :row-key="setRowKey"
      :pagination="pagination"
      @change="handleTableChange"
    >

    <template #account="{text}">
      <p v-if="text.userType === '0'">管理员</p>
      <p v-else>普通用户</p>
    </template>

      <template #action="{text}">
        <a-space size="small">
          <a-button
            type="primary"
            @click="resetUserpasswordModelView(text)"
          >重置密码</a-button>
          <a-button
            type="primary"
            @click="regeditUser(text)"
          >编辑</a-button>
          <a-popconfirm
            title="确定删除吗?"
            ok-text="是"
            cancel-text="否"
            @confirm="removeById(text)"
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
        :model="submitObject"
        :label-col="{ span: 4 }"
        :wrapper-col="{ span: 14 }"
      >
        <a-form-item label="登录名">
          <a-input
            v-model:value="submitObject.loginName"
            :disabled="!!submitObject.id"
          />
        </a-form-item>
        <a-form-item label="昵称">
          <a-input v-model:value="submitObject.name" />
        </a-form-item>
        <a-form-item
          label="密码"
          v-if="!submitObject.id"
        >
          <a-input v-model:value="submitObject.password" />
        </a-form-item>
      </a-form>
    </a-modal>

    <a-modal
      title="Title"
      v-model:visible="showResetPasswordModelView"
      :confirm-loading="resetPaswordLoading"
      @ok="resetPasswordHandleOK"
    >
      <a-form
        :model="submitResetPasswordObject.resetPaswordVO"
        :label-col="{ span: 4 }"
        :wrapper-col="{ span: 14 }"
      >
        <a-form-item label="新密码">
          <a-input v-model:value="submitResetPasswordObject.resetPaswordVO.newPassword" />
        </a-form-item>
      </a-form>
    </a-modal>
  </a-layout-content>
</template>

<script lang="ts">
import axios from "axios";
import { defineComponent, ref, reactive, onMounted, toRaw } from "vue";
import { message } from "ant-design-vue";

const columns = [
  {
    dataIndex: "id",
    key: "id",
    title: "id",
    // 表头渲染成customTitle 值渲染成customRender
    slots: { customRender: "id" },
  },
  {
    title: "登录名",
    dataIndex: "loginName",
    key: "loginName",
  },
  {
    title: "昵称",
    dataIndex: "name",
    key: "name",
  },
  {
    title: "密码",
    dataIndex: "password",
    key: "password",
  },
  {
    title: "用户类型",
    slots: { customRender: "account" },
  },
  {
    title: "action",
    key: "action",
    slots: { customRender: "action" },
  },
];

type pageObject = {
  current: number;
  pageSize: number;
  total?: number;
  pageContditon?: string;
};

type userObject = {
  id: string;
  loginName: string;
  name: string;
  password: string;
};

type resetPasswordVO = {
  id: string;
  oldPassword: string;
  newPassword: string;
};

export default defineComponent({
  name: "AdminUser",
  setup() {
    const users = ref();
    const pagination: pageObject = reactive({
      current: 0,
      pageSize: 5,
      pageContditon: "",
    });

    const submitObject: userObject = reactive({
      id: "",
      loginName: "",
      name: "",
      password: "",
    });

    const visible = ref(false);
    const queryCondition = ref("");

    let submitType = "";

    const selectUserList = (
      current: number,
      pageSize: number,
      conditon: string
    ) => {
      axios
        .get(
          "/user/list?current=" +
            current +
            "&pageSize=" +
            pageSize +
            "&condition=" +
            conditon
        )
        .then((response) => {
          users.value = response.data.data.records;
          pagination.current = parseInt(response.data.data.current);
          pagination.pageSize = parseInt(response.data.data.size);
          pagination.total = parseInt(response.data.data.total);
        });
    };

    const handleTableChange = (data: pageObject) => {
      selectUserList(data.current, data.pageSize, queryCondition.value);
    };

    const addUser = () => {
      clearSubmitObject();
      visible.value = true;
      submitType = "add";
    };

    const showResetPasswordModelView = ref(false);
    const resetPaswordLoading = ref(false);

    const submitResetPasswordObject = reactive({
      resetPaswordVO: {
        id: "",
        oldPassword: "",
        newPassword: "",
      },
    });

    const add = (submitObject: userObject) => {
      axios.post("/user/add", submitObject).then((response) => {
        if (response.data.success) {
          message.success("添加成功", 10);
        } else {
          message.error(response.data.message, 10);
        }
      });
    };

    const getUserById = (id: string) => {
      clearSubmitObject();
      axios.get("/user/selectById?id=" + id).then((response) => {
        submitObject.id = response.data.data.id;
        submitObject.name = response.data.data.name;
        submitObject.loginName = response.data.data.loginName;
        submitObject.password = response.data.data.password;
      });
    };

    const regeditUser = (parm: userObject) => {
      submitType = "update";
      getUserById(toRaw(parm).id);
      visible.value = true;
    };

    const update = (submitObject: userObject) => {
      axios.post("/user/update", submitObject).then((response) => {
        if (response.data.success) {
          message.success("修改成功", 10);
        } else {
          message.error(response.data.message, 10);
        }
      });
    };

    const handleOk = () => {
      if (submitType === "add") {
        add(submitObject);
        selectUserList(0, 10, "");
        visible.value = false;
      } else if (submitType === "update") {
        update(submitObject);
        selectUserList(0, 10, "");
        visible.value = false;
      }
    };

    const clearSubmitObject = () => {
      submitObject.id = "";
      submitObject.name = "";
      submitObject.loginName = "";
      submitObject.password = "";
    };

    const removeById = (user: userObject) => {
      axios.post("/user/remove?id=" + toRaw(user).id).then(() => {
        message.success("删除成功", 10);
      });
    };

    const queryUserCondtionList = () => {
      selectUserList(0, 10, queryCondition.value);
    };

    const resetUserpasswordModelView = (submitObject: userObject) => {
      submitResetPasswordObject.resetPaswordVO = {
        id: '',
        oldPassword: '',
        newPassword: ''
      }
      console.log(toRaw(submitObject).id);
      submitResetPasswordObject.resetPaswordVO.id = toRaw(submitObject).id;
      showResetPasswordModelView.value = true;
    };

    const resetPassword = (parm: resetPasswordVO) => {
      axios.post("/user/resetPassword", parm).then((response) => {
        if (response.data.success) {
          message.success("修改成功", 10);
        } else {
          message.error(response.data.message, 10);
        }
        console.log(response.data);
      });
    };

    const resetPasswordHandleOK = () => {
      resetPassword(submitResetPasswordObject.resetPaswordVO);
    };

    const setRowKey = (record: any) => {
      return  record.id
    }

    onMounted(() => {
      selectUserList(0, 10, "");
    });


    return {
      columns,
      users,
      pagination,
      visible,
      submitObject,
      queryCondition,
      showResetPasswordModelView,
      resetPaswordLoading,
      submitResetPasswordObject,
      handleTableChange,
      addUser,
      handleOk,
      regeditUser,
      removeById,
      queryUserCondtionList,
      resetUserpasswordModelView,
      resetPasswordHandleOK,
      setRowKey
    };
  },
});
</script>

<style>
</style>