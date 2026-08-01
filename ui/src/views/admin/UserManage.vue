<template>
  <el-card>
    <div class="toolbar">
      <el-input v-model="query.keyword" placeholder="手机号/昵称搜索" clearable style="width: 220px"
                @keyup.enter="load" @clear="load" />
      <el-button type="primary" @click="load">查询</el-button>
    </div>
    <el-table :data="rows" border stripe v-loading="loading">
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="phone" label="手机号" width="140" />
      <el-table-column prop="nickname" label="昵称" min-width="120" />
      <el-table-column prop="status" label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="row.status === 1 ? 'success' : 'danger'">
            {{ row.status === 1 ? '正常' : '禁用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="注册时间" width="180" />
      <el-table-column label="操作" width="120">
        <template #default="{ row }">
          <el-button v-if="row.status === 1" type="danger" size="small" @click="toggle(row)">
            禁用
          </el-button>
          <el-button v-else type="success" size="small" @click="toggle(row)">
            启用
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination class="pager" background layout="total, prev, pager, next" :total="total"
                   :page-size="query.size" :current-page="query.current" @current-change="onPage" />
  </el-card>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getUserPage, changeUserStatus } from '@/api/admin'

const rows = ref([])
const total = ref(0)
const loading = ref(false)
const query = reactive({ current: 1, size: 10, keyword: '' })

const load = async () => {
  loading.value = true
  try {
    const { data } = await getUserPage(query)
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

const toggle = async (row) => {
  const status = row.status === 1 ? 0 : 1
  await changeUserStatus(row.id, status)
  ElMessage.success('操作成功')
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
