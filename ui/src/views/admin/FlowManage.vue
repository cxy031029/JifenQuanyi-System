<template>
  <el-card>
    <div class="toolbar">
      <el-input v-model="query.userId" placeholder="用户ID" clearable style="width: 140px"
                @keyup.enter="load" @clear="load" />
      <el-select v-model="query.bizType" placeholder="业务类型" clearable style="width: 140px" @change="load">
        <el-option label="签到" :value="1" />
        <el-option label="任务" :value="2" />
        <el-option label="兑换" :value="3" />
        <el-option label="秒杀" :value="4" />
      </el-select>
      <el-button type="primary" @click="load">查询</el-button>
    </div>
    <el-table :data="rows" border stripe v-loading="loading">
      <el-table-column prop="flowNo" label="流水号" min-width="180" />
      <el-table-column prop="userId" label="用户ID" width="90" />
      <el-table-column prop="bizType" label="业务" width="80">
        <template #default="{ row }">
          <el-tag size="small">{{ bizTypeText(row.bizType) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="changePoint" label="变动积分" width="100">
        <template #default="{ row }">
          <span :style="{ color: row.changePoint >= 0 ? '#67c23a' : '#f56c6c' }">
            {{ row.changePoint >= 0 ? '+' : '' }}{{ row.changePoint }}
          </span>
        </template>
      </el-table-column>
      <el-table-column prop="balanceAfter" label="变动后余额" width="110" />
      <el-table-column prop="createTime" label="时间" width="180" />
    </el-table>
    <el-pagination class="pager" background layout="total, prev, pager, next" :total="total"
                   :page-size="query.size" :current-page="query.current" @current-change="onPage" />
  </el-card>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { getFlowPage } from '@/api/admin'

const rows = ref([])
const total = ref(0)
const loading = ref(false)
const query = reactive({ current: 1, size: 10, userId: '', bizType: null })

const bizTypeText = (t) => ({ 1: '签到', 2: '任务', 3: '兑换', 4: '秒杀' }[t] || '未知')

const load = async () => {
  loading.value = true
  try {
    const params = { current: query.current, size: query.size }
    if (query.userId) params.userId = query.userId
    if (query.bizType != null) params.bizType = query.bizType
    const { data } = await getFlowPage(params)
    rows.value = data.records
    total.value = data.total
  } finally {
    loading.value = false
  }
}

const onPage = (page) => {
  query.current = page
  load()
}

onMounted(load)
</script>

<style scoped>
.toolbar {
  margin-bottom: 14px;
  display: flex;
  gap: 10px;
}
.pager {
  margin-top: 14px;
  justify-content: flex-end;
}
</style>
