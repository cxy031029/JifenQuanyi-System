<template>
  <el-card shadow="never">
    <template #header>每日任务</template>
    <el-table v-loading="loading" :data="tasks">
      <el-table-column prop="taskName" label="任务名称" />
      <el-table-column prop="point" label="奖励积分" width="120">
        <template #default="{ row }">
          <el-tag type="success">+{{ row.point }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="dailyLimit" label="每日可领" width="120" />
      <el-table-column label="操作" width="140">
        <template #default="{ row }">
          <el-button type="primary" size="small" @click="claim(row)">领取</el-button>
        </template>
      </el-table-column>
    </el-table>
  </el-card>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getTaskList, claimTask } from '@/api/task'

const tasks = ref([])
const loading = ref(false)

const load = async () => {
  loading.value = true
  try {
    const { data } = await getTaskList()
    tasks.value = data || []
  } finally {
    loading.value = false
  }
}

const claim = async (row) => {
  await claimTask(row.id)
  ElMessage.success(`任务完成，+${row.point} 积分`)
}

onMounted(load)
</script>
