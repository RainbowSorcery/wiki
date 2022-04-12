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
          <div>
            <h2>{{selectContentNode.name}}</h2>

            <div>
              <span>点赞数:{{selectContentNode.viewCount}}</span> &nbsp; &nbsp;
              <span>阅读数:{{selectContentNode.voteCount}}</span>
            </div>
            <a-divider></a-divider>
          </div>

          <div v-html="html">
          </div>

          <div class="vote-div">
            <a-space>
              <a-button
                type="primary"
                shape="round"
                size="large"
                @click="increaseVoteCount(selectContentNode.id)"
              >
                <template #icon>
                  <LikeTwoTone />
                </template>
                点赞: {{selectContentNode.voteCount}}
              </a-button>
              <a-button
                type="primary"
                shape="round"
                size="large"
                @click="collectDoc(selectContentNode.id)"
              >
                <template #icon>
                  <LikeTwoTone />
                </template>
                收藏
              </a-button>
            </a-space>

          </div>
        </a-col>
      </a-row>
    </a-layout-content>
  </a-layout>
</template>

<script lang="ts">
import axios from "axios";
import { defineComponent, ref, reactive, onMounted, computed } from "vue";
import { useRoute } from "vue-router";
import { LikeTwoTone } from "@ant-design/icons-vue";
import { message } from "ant-design-vue";
import store from "@/store";

export default defineComponent({
  name: "doc-view",
  components: {
    LikeTwoTone,
  },
  setup() {
    const docTreeData = ref();
    const viewDocTreeData = ref(false);
    const html = ref();
    const selectedKeys = reactive({
      selectedKeys: Array<string>(),
    });

    const selectContentNode = ref({
      id: "",
      name: "",
      voteCount: 0,
      viewCount: 0,
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
          selectContentNode.value = response.data.data[0];
        }
        viewDocTreeData.value = response.data.data.length > 0;
      });
    };

    // 查询被选中节点的内容
    const selectTree = (data: Array<string>, info: any) => {
      console.log(selectContentNode.value);
      selectContentNode.value = info.selectedNodes[0].props;

      if (data.length > 0) {
        selectedKeys.selectedKeys = [data[0]];
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

    const increaseVoteCount = (docId: string) => {
      axios.post("/doc/increaseVoteCount/?id=" + docId).then((response) => {
        if (response.data.success) {
          selectContentNode.value.voteCount++;
        } else {
          message.error(response.data.message);
        }
      });
    };

    const user = computed(() => store.state.user);

    const collectDoc = (docId: any) => {
      if (
        user.value.token === null ||
        user.value.token === "" ||
        user.value.token === undefined
      ) {
        message.error("请登录");
      } else {
        axios
          .get(
            "/collect/getCollectByDocIdAndUserId?docId=" +
              docId +
              "&userId=" +
              user.value.id
          )
          .then((response) => {
            // 判断文章是否被收藏过
            if (response.data.data) {
              axios
                .post("/collect/addCollect", {
                  docId: docId,
                  userId: user.value.id,
                })
                .then((response) => {
                  if (response.data.success) {
                    message.success("收藏成功");
                  }
                });
            } else {
              message.error("该文档已被用户收藏过");
            }
          });
      }
    };

    onMounted(() => {
      selectDocTreeData(route.query.ebookId + "");
    });

    return {
      docTreeData,
      viewDocTreeData,
      html,
      selectedKeys,
      selectContentNode,
      selectTree,
      increaseVoteCount,
      collectDoc,
    };
  },
});
</script>

<style scoped>
.vote-div {
  padding: 15px;
  text-align: center;
}
</style>