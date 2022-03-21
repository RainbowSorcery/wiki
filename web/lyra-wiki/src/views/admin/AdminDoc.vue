<template>
  <a-layout-content :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }">
    <p>
      <a-space size="small">
        <a-input size="small" v-model:value="queryCondition" placeholder="small size" />
        <a-button type="primary" @click="queryDocCondtionList">查询</a-button>
        <a-button type="primary" @click="addDoc">新增电子书</a-button>
      </a-space>

    </p>

    <a-table :columns="columns" :data-source="Doc"  :row-key="setRowKey" 
      :pagination="false"
      @change="handleTableChange"
    >

        <template #action="{text}" >
          <a-space size="small">
            <a-button type="primary" @click="regeditDoc(text)">编辑</a-button>
            <a-button danger @click="deleteDoc(text)">删除</a-button>
          </a-space>
        </template>
      </a-table>

    <a-modal v-model:visible="visible" title="Basic Modal" @ok="handleOk">
      <a-form :model="sumbitDoc" :label-col="{ span: 4 }" :wrapper-col="{ span: 14 }">
        <a-form-item label="名称">
          <a-input v-model:value="sumbitDoc.name" />
        </a-form-item>
        <a-form-item label="父分类">
          <a-select
                ef="select"
                v-model:value="sumbitDoc.parent"
                @focus="focus"
              >
                <a-select-option value="0">
                  无
                </a-select-option>
                <a-select-option v-for="doc in parentDoc" :key="doc.id" :value="doc.id" :disabled="sumbitDoc.id == doc.id">{{doc.name}}</a-select-option>

          </a-select>
        </a-form-item>
        <a-form-item label="父分类">
          <a-tree-select
            v-model:value="sumbitDoc.parent"
            style="width: 100%"
            :dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
            :tree-data="selectTreeData.selectedData"
            placeholder="Please select"
            tree-default-expand-all
            :replaceFields="{children:'children', title:'name', key:'id', value: 'id'}"
          >
            <template #title="{ key, value }">
              <span style="color: #08c" v-if="key === '0-0-1'">Child Node1 {{ value }}</span>
            </template>
          </a-tree-select>
        </a-form-item>
        <a-form-item label="排序">
          <a-input v-model:value="sumbitDoc.sort" />
        </a-form-item>
        <a-form-item label="内容">
          <div id="content"></div>
        </a-form-item>
      </a-form>
    </a-modal>
  </a-layout-content>
</template>

<script lang="ts">
import { defineComponent, ref, reactive, onMounted, toRaw,  } from "vue";
import { message } from 'ant-design-vue';
import axios from "axios";
import { useRoute } from "vue-router";
import E from "wangeditor"

const columns = [
  {
    dataIndex: 'name',
    key: 'name',
    title: '名称',
    // 表头渲染成customTitle 值渲染成customRender
    slots: { customRender: 'name' },
  },
  {
    title: '父分类',
    dataIndex: 'parent',
    key: 'parent',
  },
  {
    title: '顺序',
    dataIndex: 'sort',
    key: 'sort',
  },
  {
    title: 'action',
    key: 'action',
    slots: { customRender: 'action' },
  }
];



type DocObject = {
  id: bigint | undefined,
  ebookId: string | undefined,
  parent: bigint | undefined,
  name: string,
  sort: number | undefined,
  disabled?: boolean,
  viewCount?: number,
  vote_count?: number
}



export default defineComponent({
  name: "AdminDoc",
  setup() {
    const Doc = ref()

    // 提交对象
    const sumbitDoc: DocObject = reactive({
      id: undefined,
      ebookId: undefined,
      name: '',
      parent: undefined,
      sort: undefined
    })



    // 显示弹出框
    const visible = ref(false)

    let sumbitType = ''

    // 分页查询
    const queryDocList = () => {
      axios.get("/doc/list/tree").then((response) => {
        Doc.value = response.data.data
      })
    }

  const getSelectedTreeDataList = (id?: any) => {
    axios.get("/doc/getSelectedTreeData?id=" + id).then((response) => {
      selectTreeData.selectedData = response.data.data
    })
  }

  const router = useRoute()


    const regeditDoc = (parm: DocObject) => {
      selectTreeData.selectedData = []
      getSelectedTreeDataList(toRaw(parm).id)
      callBackSubmitDoc()

      visible.value = true
      sumbitType = 'update'
      axios.get("/doc/getDocById?id=" + toRaw(parm).id).then((response) => {
        sumbitDoc.id = response.data.data.id
        sumbitDoc.parent = response.data.data.parent
        sumbitDoc.name = response.data.data.name
        sumbitDoc.sort = response.data.data.sort
      })

      const editor = new E("#content")
      editor.create()
    }

    const callBackSubmitDoc = () => {
        sumbitDoc.ebookId = undefined
        sumbitDoc.id = undefined
        sumbitDoc.parent = undefined
        sumbitDoc.name = ''
        sumbitDoc.sort = undefined
    }

    const handleTableChange = () => {
      queryDocList()
    }

    onMounted(() => {
      queryDocList()
      getParentDoc()
    })

    const setRowKey = (record: DocObject) => {
      return toRaw(record).id
    }

    const addDoc = () => {
      callBackSubmitDoc()
      getSelectedTreeDataList('')
      visible.value = true
      sumbitType = 'add'
    }

    const add = (sumbitBook: DocObject) => {
      sumbitBook.ebookId = router.query.ebookId + ""
      axios.post("/doc/addDoc", sumbitBook).then(() => {
        message.success(
          '添加成功',
          10,
        );
        queryDocList()
      })
    }

    const update = (sumbitBook: DocObject) => {
      axios.post('/doc/updateDoc', sumbitBook).then((response) => {
        if (response.data.success) {
          message.success(
            '更新成功',
            10,
          );
          queryDocList()
        } else {
           message.error('更新失败, 父分类下有子分类存在');
        }

      })
    }

    // 弹出框之后点击ok之后的回调函数
    const handleOk = () => {
      if (sumbitType === 'add') {
        add(sumbitDoc)
        queryDocList()
      visible.value = false
      } else if (sumbitType === 'update') {
        update(sumbitDoc)
        queryDocList()
        visible.value = false
      }
    }

    const queryDocCondtionList = () => {
      queryDocList()
    }

    const queryCondition = ref()

    const parentDoc = ref() 

    const getParentDoc = () => {
      axios.get("/doc/getParentDoc").then((response) => {
        parentDoc.value = response.data.data
      })
    }

    const selectTreeData = reactive({
      selectedData: Array<DocObject>()
    })
    selectTreeData.selectedData = []

    const deleteDoc = (doc: DocObject) => {
      const docTowRaw = toRaw(doc)
      axios.post("/doc/deleteDoc?id=" + docTowRaw.id).then(() => {
        queryDocList()
      })
    }

    return {
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
    }
  },
});
</script>

<style scoped>
</style>
