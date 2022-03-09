<template>
  <a-layout-content :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }">
    <p>
      <a-space size="small">
        <a-input size="small" v-model:value="pagination.pageContditon" placeholder="small size" />
        <a-button type="primary" @click="queryEbookCondtionList">查询</a-button>
        <a-button type="primary" @click="addEbook">新增电子书</a-button>
      </a-space>

    </p>

    <a-table :columns="columns" :data-source="ebook"  :row-key="setRowKey" 
      :pagination="pagination"
      @change="handleTableChange"
    >
        <template #name="{ text }">
          <img v-if="text" :src="text" alt="text" />
        </template>
        <template #action="{text}" >
          <a-space size="small">
            <a-button type="primary" @click="regeditEbook(text)">编辑</a-button>
            <a-button danger>删除</a-button>
          </a-space>
        </template>
      </a-table>

    <a-modal v-model:visible="visible" title="Basic Modal" @ok="handleOk">
      <a-form :model="sumbitBook" :label-col="{ span: 4 }" :wrapper-col="{ span: 14 }">
        <a-form-item label="封面">
          <a-input v-model:value="sumbitBook.cover" />
        </a-form-item>
        <a-form-item label="名称">
          <a-input v-model:value="sumbitBook.name" />
        </a-form-item>
        <a-form-item label="分类1">
          <a-input v-model:value="sumbitBook.category1Id" />
        </a-form-item>
        <a-form-item label="分类2">
          <a-input v-model:value="sumbitBook.category2Id" />
        </a-form-item>
        <a-form-item label="分类2">
          <a-cascader v-model:value="selectCategory.selectedCategoryList" :options="options" placeholder="Please select" />
        </a-form-item>
        <a-form-item label="描述">
          <a-input v-model:value="sumbitBook.description" />
        </a-form-item>
      </a-form>
    </a-modal>
  </a-layout-content>
</template>

<script lang="ts">
import { defineComponent, ref, reactive, onMounted, toRaw, toRef,  } from "vue";
import { message } from 'ant-design-vue';
import axios from "axios";

const columns = [
  {
    dataIndex: 'cover',
    key: 'cover',
    title: '封面',
    // 表头渲染成customTitle 值渲染成customRender
    slots: { customRender: 'name' },
  },
  {
    title: '名称',
    dataIndex: 'name',
    key: 'name',
  },
  {
    title: '分类1',
    dataIndex: 'category1Id',
    key: 'category1Id',
  },
  {
    title: '分类2',
    key: 'category2Id ',
    dataIndex: 'category2Id',
  },
  {
    title: '文档数',
    key: 'docCount',
    dataIndex: 'docCount',
  },
  {
    title: '阅读数',
    key: 'viewCount',
    dataIndex: 'viewCount'
  },
  {
    title: '点赞数',
    key: 'voteCount',
    dataIndex: 'voteCount'
  },
  {
    title: 'action',
    key: 'action',
    slots: { customRender: 'action' },
  }
];


type pageObject = {
  current: number,
  pageSize: number,
  total?: number
  pageContditon?: string
}

type ebookObject = {
  id: number | undefined,
  name: string,
  category1Id: number | undefined,
  category2Id: number | undefined,
  description: string,
  cover: string,
  docCount: number | undefined,
  viewCount: number | undefined,
  voteCount: number | undefined
}

interface Option {
  value: string;
  label: string;
  children?: Option[];
}

export default defineComponent({
  name: "AdminEbook",
  setup() {
    const ebook = ref()

    // 分页对象
    const pagination: pageObject = reactive({
      current: 0,
      pageSize: 5,
      pageContditon: ''
    })

    // 提交对象
    const sumbitBook: ebookObject = reactive({
      id: undefined,
      name: '',
      category1Id: undefined,
      category2Id: undefined,
      description: '',
      cover: '',
      docCount: undefined,
      viewCount: undefined,
      voteCount: undefined
    })



    // 显示弹出框
    const visible = ref(false)

    let sumbitType = ''

    // 分页查询
    const queryEbookList = (pageParm: pageObject) => {
      axios.get("/ebook/pageList?pageSize=" + pageParm.pageSize + "&current=" + pageParm.current + "&name=" + pageParm.pageContditon).then((response) => {
        ebook.value = response.data.data.records
        pagination.current = response.data.data.current
        pagination.pageSize = response.data.data.size
        pagination.total = response.data.data.total
      })
    }


    const regeditEbook = (parm: ebookObject) => {
      visible.value = true
      sumbitType = 'update'
      axios.get("/ebook/getEbookById?id=" + toRaw(parm).id).then((response) => {
        selectCategory.selectedCategoryList[0] = response.data.data.category1Id + ""
        selectCategory.selectedCategoryList[1] = response.data.data.category2Id + ""

        sumbitBook.name = response.data.data.name
        sumbitBook.id = response.data.data.id
        sumbitBook.cover = response.data.data.cover
        sumbitBook.description = response.data.data.description
      })
    }

    const handleTableChange = (pageParm: pageObject) => {
      queryEbookList(pageParm)
    }

    onMounted(() => {
      queryEbookList({
        current: 0,
        pageSize: 5,
        pageContditon: ''
      })
      queryCategoryTreeList()
    })

    const setRowKey = (record: ebookObject) => {
      return toRaw(record).id
    }

    const addEbook = () => {
      visible.value = true
      sumbitType = 'add'
    }

    const add = (sumbitBook: ebookObject) => {
      sumbitBook.category1Id = Number(selectCategory.selectedCategoryList[0])
      sumbitBook.category2Id = Number(selectCategory.selectedCategoryList[1])
      axios.post("/ebook/addEbook", sumbitBook).then(() => {
        message.success(
          '添加成功',
          10,
        );
      })
    }

    const update = (sumbitBook: ebookObject) => {
      sumbitBook.category1Id = Number(selectCategory.selectedCategoryList[0])
      sumbitBook.category2Id = Number(selectCategory.selectedCategoryList[1])
      axios.post('/ebook/updateEbook', sumbitBook).then(() => {
        message.success(
          '更新成功',
          10,
        );
      })
    }


    // 弹出框之后点击ok之后的回调函数
    const handleOk = () => {
      if (sumbitType === 'add') {
        add(sumbitBook)
        queryEbookList({
          current: 0,
          pageSize: 5,
          pageContditon: ''
        })
      visible.value = false
      } else if (sumbitType === 'update') {
        update(sumbitBook)
        queryEbookList({
          current: 0,
          pageSize: 5,
          pageContditon: ''
        })
        visible.value = false
      }
    }

    const queryEbookCondtionList = () => {
      queryEbookList(pagination)
    }

    const options: Option[] = reactive([])

    const queryCategoryTreeList = () => {
      axios.get("/category/list/tree").then((response) => {
        const categorys = response.data.data
        
        for (let i = 0; i < categorys.length; i++) {
          const optionsChildren: Option[] = [];
          const categoryChildren = categorys[i].children
          
          for (let i = 0; i < categoryChildren.length; i++) {
            optionsChildren.push({
                label: categoryChildren[i].name,
                value: categoryChildren[i].id,
                children: undefined
              })
          }

          options.push({
            label: categorys[i].name,
            value: categorys[i].id,
            children: optionsChildren
          })
        }

      })
    }

    // 被选中分类列表 Array<string>表示数组
    const selectCategory = reactive({
      "selectedCategoryList": Array<string>()
    })
    
    return {
      ebook,
      sumbitBook,
      columns,
      pagination,
      visible,
      options,
      selectCategory,
      setRowKey,
      regeditEbook,
      handleTableChange,
      handleOk,
      addEbook,
      queryEbookCondtionList,
    }
  },
});
</script>

<style scoped>
</style>
