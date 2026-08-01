<template>
  <div>
    <el-row :gutter="16">
      <el-col :span="6" v-for="card in cards" :key="card.label">
        <el-card class="stat-card">
          <div class="stat-label">{{ card.label }}</div>
          <div class="stat-value">{{ card.value }}</div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getOverview } from '@/api/admin'

const cards = ref([
  { label: '用户总数', key: 'userTotal' },
  { label: '今日注册', key: 'userToday' },
  { label: '积分流水总数', key: 'flowTotal' },
  { label: '今日流水', key: 'flowToday' },
  { label: '商品数', key: 'productTotal' },
  { label: '兑换记录数', key: 'exchangeTotal' },
  { label: '秒杀订单数', key: 'seckillOrderTotal' },
  { label: '待处理对账', key: 'reconcilePending' }
])

onMounted(async () => {
  const { data } = await getOverview()
  cards.value.forEach((c) => {
    c.value = data[c.key] ?? 0
  })
})
</script>

<style scoped>
.stat-card {
  margin-bottom: 16px;
  text-align: center;
}
.stat-label {
  color: #909399;
  font-size: 14px;
}
.stat-value {
  margin-top: 8px;
  font-size: 28px;
  font-weight: bold;
  color: #303133;
}
</style>
