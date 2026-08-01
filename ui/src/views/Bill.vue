<template>
  <el-card shadow="never">
    <template #header>
      <div class="header">
        <span>积分账单</span>
        <el-select v-model="bizType" placeholder="全部类型" clearable style="width: 160px" @change="load">
          <el-option label="签到" :value="1" />
          <el-option label="任务" :value="2" />
          <el-option label="兑换" :value="3" />
          <el-option label="秒杀" :value="4" />
        </el-select>
      </div>
    </template>

    <el-table v-loading="loading" :data="flows">
      <el-table-column prop="flowNo" label="流水号" />
      <el-table-column label="类型" width="100">
        <template #default="{ row }">
          <el-tag>{{ bizTypeText(row.bizType) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="变动" width="120">
        <template #default="{ row }">
          <span :class="row.changePoint > 0 ? 'plus' : 'minus'">
            {{ row.changePoint > 0 ? '+' : '' }}{{ row.changePoint }}
          </span>
        </template>
      </el-table-column>
      <el-table-column prop="balanceAfter" label="变动后余额" width="120" />
      <el-table-column prop="createTime" label="时间" />
    </el-table>

    <el-pagination
      class="pager"
      background
      layout="total, prev, pager, next"
      :total="total"
      :page-size="size"
      :current-page="current"
      @current-change="onPage"
    />
  </el-card>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getFlows } from '@/api/bill'

const bizType = ref(null)
const flows = ref([])
const total = ref(0)
const current = ref(1)
const size = ref(10)
const loading = ref(false)

const bizTypeText = (t) => ({ 1: '签到', 2: '任务', 3: '兑换', 4: '秒杀' }[t] || t)

const load = async () => {
  loading.value = true
  try {
    const params = { current: current.value, size: size.value }
    if (bizType.value) params.bizType = bizType.value
    const { data } = await getFlows(params)
    flows.value = data.records || []
    total.value = data.total
  } finally {
    loading.value = false
  }
}

const onPage = (page) => {
  current.value = page
  load()
}

onMounted(load)
</script>

<style scoped>
.header {
  display: flex;
  align-items: center;
  justify-content: space-between;
}
.plus {
  color: #67c23a;
  font-weight: 600;
}
.minus {
  color: #f56c6c;
  font-weight: 600;
}
.pager {
  margin-top: 15px;
  justify-content: flex-end;
}
</style>
