<template>
  <div v-if="user">
    <a-card title="统计数据">
      <a-row>
        <a-col :span="6">
          <a-statistic
            title="总阅读量"
            :value="statistic.viewCount"
            style="margin-right: 50px"
          />
        </a-col>
        <a-col :span="6">
          <a-statistic
            title="总点赞数"
            :value="statistic.voteCount"
            style="margin-right: 50px"
          />
        </a-col>
        <a-col :span="6">
          <a-statistic
            title="今日相较于昨日浏览数增量"
            :value="statistic.viewIncrease"
            style="margin-right: 50px"
          />
        </a-col>
        <a-col :span="6">
          <a-statistic
            title="今日相较于昨日点赞数增量"
            :value="statistic.voteIncrease"
            style="margin-right: 50px"
          />
        </a-col>
      </a-row>
    </a-card>

    <a-card title="统计数据趋势图">
      <a-row>
        <a-col :span="24">
          <div
            id="main"
            :style="{ width: '100%', height: '500px' }"
          ></div>

        </a-col>
      </a-row>
    </a-card>

  </div>

  <div v-else>
    欢迎使用wiki系统
  </div>
</template>

<script lang="ts">
import store from "@/store";
import axios from "axios";
import { defineComponent, onMounted, ref, computed } from "vue";

declare let echarts: any;

export default defineComponent({
  name: "the-welcome",
  setup() {
    const statistic = ref({
      viewCount: 0,
      voteCount: 0,
      voteIncrease: 0,
      viewIncrease: 0,
    });

    const user = computed(() => store.state.user);

    const getStatisticData = () => {
      axios.get("/ebook-snapshot/getSnapshotStatistic").then((response) => {
        console.log(response.data);
        statistic.value.viewCount = response.data.data[1].viewCount;
        statistic.value.voteCount = response.data.data[1].voteCount;
        statistic.value.viewIncrease = response.data.data[1].viewIncrease;
        statistic.value.voteIncrease = response.data.data[1].voteIncrease;
      });
    };

    const get30DayStatisticData = () => {
      axios.get("/ebook-snapshot/get-30Statistic").then((response) => {
        let dataList = [];
        let viewList = [];
        let voteList = [];

        // 将30天数据保存至list中 然后插入这先数据
        for (let i = 0; i < response.data.data.length; i++) {
          dataList.push(response.data.data[i].date);
          voteList.push(response.data.data[i].voteCount);
          viewList.push(response.data.data[i].viewCount);
        }

        var myChart = echarts.init(document.getElementById('main'));

        const option = {
          title: {
            text: "统计数据趋势图",
          },
          tooltip: {
            trigger: "axis",
          },
          legend: {
            data: ["浏览数", "点赞数"],
          },
          grid: {
            left: "3%",
            right: "4%",
            bottom: "3%",
            containLabel: true,
          },
          xAxis: {
            type: "category",
            boundaryGap: false,
            data: dataList,
          },
          yAxis: {
            type: "value",
          },
          series: [
            {
              name: "浏览数",
              type: "line",
              stack: "Total",
              data: viewList,
            },
            {
              name: "点赞数",
              type: "line",
              stack: "Total",
              data: voteList,
            },
          ],
        };

        myChart.setOption(option)
      });
    };

    const initchart = () => {
      // 绘制图表
    };

    onMounted(() => {
      getStatisticData();
      initchart();
      get30DayStatisticData();
    });

    return {
      statistic,
      user,
    };
  },
});
</script>

<style>
</style>