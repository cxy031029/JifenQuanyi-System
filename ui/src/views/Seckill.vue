<template>
  <el-card shadow="never">
    <template #header>限时抢购</template>
    <el-empty v-if="!activities.length" description="当前暂无进行中的活动" />
    <el-table v-else v-loading="loading" :data="activities">
      <el-table-column prop="activityName" label="活动名称" />
      <el-table-column prop="pointCost" label="消耗积分" width="120">
        <template #default="{ row }">
          <el-tag type="danger">{{ row.pointCost }} 积分</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="totalStock" label="总库存" width="120" />
      <el-table-column label="活动时间" min-width="220">
        <template #default="{ row }">
          {{ row.startTime }} ~ {{ row.endTime }}
        </template>
      </el-table-column>
      <el-table-column label="操作" width="140">
        <template #default="{ row }">
          <el-button type="danger" :loading="buying === row.id" @click="doSeckill(row)">
            立即抢购
          </el-button>
        </template>
      </el-table-column>
    </el-table>
  </el-card>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getActivities, seckill } from '@/api/seckill'

const activities = ref([])
const loading = ref(false)
const buying = ref(null)

const load = async () => {
  loading.value = true
  try {
    const { data } = await getActivities()
    activities.value = data || []
  } finally {
    loading.value = false
  }
}

const doSeckill = async (row) => {
  buying.value = row.id
  try {
    const { data } = await seckill(row.id)
    ElMessage.success(`抢购成功，订单号：${data.orderNo}`)
  } finally {
    buying.value = null
  }
}

onMounted(load)
</script>
