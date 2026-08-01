<template>
  <el-container class="admin-layout">
    <el-aside width="200px" class="aside">
      <div class="logo">积分权益管理后台</div>
      <el-menu :default-active="$route.path" router background-color="#182848" text-color="#c0c4cc"
               active-text-color="#ffffff">
        <el-menu-item index="/admin/dashboard">
          <el-icon><Odometer /></el-icon>
          <span>数据概览</span>
        </el-menu-item>
        <el-menu-item index="/admin/users">
          <el-icon><User /></el-icon>
          <span>用户管理</span>
        </el-menu-item>
        <el-menu-item index="/admin/flows">
          <el-icon><List /></el-icon>
          <span>积分流水</span>
        </el-menu-item>
      </el-menu>
    </el-aside>
    <el-container>
      <el-header class="header">
        <span class="welcome">欢迎，{{ adminStore.info?.nickname || adminStore.info?.username || '管理员' }}</span>
        <el-button type="danger" size="small" @click="doLogout">退出登录</el-button>
      </el-header>
      <el-main class="main">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Odometer, User, List } from '@element-plus/icons-vue'
import { useAdminStore } from '@/store/admin'
import { adminLogout } from '@/api/admin'

const router = useRouter()
const adminStore = useAdminStore()

onMounted(() => {
  adminStore.fetchInfo().catch(() => {})
})

const doLogout = async () => {
  try {
    await adminLogout()
  } catch (e) {
    // 忽略退出失败
  }
  adminStore.clear()
  ElMessage.success('已退出')
  router.push('/admin/login')
}
</script>

<style scoped>
.admin-layout {
  height: 100vh;
}
.aside {
  background: #182848;
}
.aside .el-menu {
  border-right: none;
}
.logo {
  height: 60px;
  line-height: 60px;
  text-align: center;
  color: #fff;
  font-weight: bold;
  background: #12203c;
}
.header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  background: #fff;
  border-bottom: 1px solid #e4e7ed;
}
.main {
  background: #f0f2f5;
}
</style>
