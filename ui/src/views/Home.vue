<template>
  <div>
    <el-row :gutter="20">
      <el-col :span="8">
        <el-card shadow="hover">
          <div class="stat-label">我的积分</div>
          <div class="stat-num primary">{{ balance }}</div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="hover">
          <div class="stat-label">连续签到</div>
          <div class="stat-num">{{ today.continueDays }} 天</div>
          <el-button
            type="primary"
            class="mt"
            :loading="signing"
            :disabled="today.todaySigned"
            @click="doSignIn"
          >
            {{ today.todaySigned ? '今日已签到' : '立即签到' }}
          </el-button>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="hover">
          <div class="stat-label">今日签到可得</div>
          <div class="stat-num">5 积分</div>
          <div class="mt tip">签到积分有效期 1 年，到期自动回收</div>
        </el-card>
      </el-col>
    </el-row>

    <el-card class="mt-card" shadow="never">
      <template #header>积分过期回收记录</template>
      <el-empty v-if="!expireList.length" description="暂无过期记录" :image-size="60" />
      <el-table v-else :data="expireList" size="small">
        <el-table-column prop="point" label="回收积分" width="120" />
        <el-table-column prop="expireTime" label="过期时间" />
        <el-table-column prop="createTime" label="回收时间" />
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getBalance, getSignToday, signIn, getExpireRecords } from '@/api/point'

const balance = ref(0)
const today = ref({ todaySigned: false, continueDays: 0 })
const expireList = ref([])
const signing = ref(false)

const load = async () => {
  const [b, t, e] = await Promise.all([getBalance(), getSignToday(), getExpireRecords()])
  balance.value = b.data
  today.value = t.data
  expireList.value = e.data || []
}

const doSignIn = async () => {
  signing.value = true
  try {
    await signIn()
    ElMessage.success('签到成功，+5 积分')
    await load()
  } finally {
    signing.value = false
  }
}

onMounted(load)
</script>

<style scoped>
.stat-label {
  color: #909399;
  font-size: 14px;
}
.stat-num {
  font-size: 28px;
  font-weight: 700;
  margin: 8px 0;
}
.stat-num.primary {
  color: #409eff;
}
.mt {
  margin-top: 10px;
}
.tip {
  color: #c0c4cc;
  font-size: 12px;
}
.mt-card {
  margin-top: 20px;
}
</style>
