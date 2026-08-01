<template>
  <div>
    <el-row :gutter="20">
      <el-col :span="6" v-for="p in products" :key="p.id">
        <el-card class="product-card" shadow="hover" :body-style="{ padding: '0' }">
          <div class="cover">{{ p.name.charAt(0) }}</div>
          <div class="info">
            <div class="name">{{ p.name }}</div>
            <div class="price"><el-tag type="warning">{{ p.pointPrice }} 积分</el-tag></div>
            <div class="stock">库存 {{ p.stock }}</div>
            <el-button
              type="primary"
              class="exchange-btn"
              :disabled="p.stock < 1"
              @click="openExchange(p)"
            >
              {{ p.stock < 1 ? '已兑完' : '兑换' }}
            </el-button>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-card class="mt-card" shadow="never">
      <template #header>我的兑换记录</template>
      <el-table v-loading="loading" :data="records">
        <el-table-column prop="exchangeNo" label="兑换单号" />
        <el-table-column prop="productName" label="商品" />
        <el-table-column prop="pointCost" label="消耗积分" width="120" />
        <el-table-column prop="createTime" label="兑换时间" />
      </el-table>
    </el-card>

    <el-dialog v-model="dialogVisible" title="确认兑换" width="380px">
      <div class="dialog-body">
        <div>商品：{{ current?.name }}</div>
        <div>所需积分：{{ current?.pointPrice }} / 件</div>
        <el-input-number v-model="quantity" :min="1" :max="current?.stock || 1" />
      </div>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="exchanging" @click="doExchange">确认兑换</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getProductList, exchange, getExchangeRecords } from '@/api/product'

const products = ref([])
const records = ref([])
const loading = ref(false)
const dialogVisible = ref(false)
const current = ref(null)
const quantity = ref(1)
const exchanging = ref(false)

const load = async () => {
  loading.value = true
  try {
    const [p, r] = await Promise.all([
      getProductList({ current: 1, size: 12 }),
      getExchangeRecords({ current: 1, size: 10 })
    ])
    products.value = p.data.records || []
    records.value = r.data.records || []
  } finally {
    loading.value = false
  }
}

const openExchange = (p) => {
  current.value = p
  quantity.value = 1
  dialogVisible.value = true
}

const doExchange = async () => {
  exchanging.value = true
  try {
    await exchange({ productId: current.value.id, quantity: quantity.value })
    ElMessage.success('兑换成功')
    dialogVisible.value = false
    await load()
  } finally {
    exchanging.value = false
  }
}

onMounted(load)
</script>

<style scoped>
.product-card {
  margin-bottom: 20px;
}
.cover {
  height: 120px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 48px;
  color: #fff;
  background: linear-gradient(135deg, #6a11cb, #2575fc);
}
.info {
  padding: 12px;
  text-align: center;
}
.name {
  font-weight: 600;
  margin-bottom: 8px;
}
.stock {
  color: #909399;
  font-size: 12px;
  margin: 6px 0;
}
.exchange-btn {
  width: 100%;
}
.mt-card {
  margin-top: 20px;
}
.dialog-body {
  line-height: 2.2;
}
</style>
