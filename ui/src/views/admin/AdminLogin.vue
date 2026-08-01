<template>
  <div class="admin-login-page">
    <el-card class="admin-login-card">
      <h2 class="title">积分权益管理后台</h2>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="0">
        <el-form-item prop="username">
          <el-input v-model="form.username" placeholder="管理员账号" clearable />
        </el-form-item>
        <el-form-item prop="password">
          <el-input v-model="form.password" type="password" placeholder="密码" show-password />
        </el-form-item>
        <el-button type="primary" class="submit-btn" :loading="loading" @click="doLogin">
          登 录
        </el-button>
      </el-form>
      <div class="back-link" @click="$router.push('/login')">返回用户端</div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { adminLogin } from '@/api/admin'
import { useAdminStore } from '@/store/admin'

const router = useRouter()
const adminStore = useAdminStore()

const loading = ref(false)
const formRef = ref()
const form = reactive({ username: '', password: '' })

const rules = {
  username: [{ required: true, message: '请输入管理员账号', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

const doLogin = async () => {
  await formRef.value.validate()
  loading.value = true
  try {
    const { data } = await adminLogin(form)
    adminStore.setToken(data.token)
    ElMessage.success('登录成功')
    router.push('/admin/dashboard')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.admin-login-page {
  height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}
.admin-login-card {
  width: 380px;
  padding: 10px 10px 20px;
  border-radius: 12px;
}
.title {
  text-align: center;
  color: #4a3f8a;
  margin-bottom: 10px;
}
.submit-btn {
  width: 100%;
}
.back-link {
  margin-top: 12px;
  text-align: center;
  color: #909399;
  font-size: 13px;
  cursor: pointer;
}
.back-link:hover {
  color: #4a3f8a;
}
</style>
