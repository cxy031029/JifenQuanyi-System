<template>
  <div class="login-page">
    <el-card class="login-card">
      <h2 class="title">积分权益运营系统</h2>
      <el-tabs v-model="active">
        <el-tab-pane label="登录" name="login">
          <el-form ref="formRef" :model="loginForm" :rules="rules" label-width="0">
            <el-form-item prop="phone">
              <el-input v-model="loginForm.phone" placeholder="手机号" clearable />
            </el-form-item>
            <el-form-item prop="password">
              <el-input v-model="loginForm.password" type="password" placeholder="密码" show-password />
            </el-form-item>
            <el-button type="primary" class="submit-btn" :loading="loading" @click="doLogin">
              登 录
            </el-button>
          </el-form>
        </el-tab-pane>
        <el-tab-pane label="注册" name="register">
          <el-form ref="regRef" :model="regForm" :rules="regRules" label-width="0">
            <el-form-item prop="phone">
              <el-input v-model="regForm.phone" placeholder="手机号" clearable />
            </el-form-item>
            <el-form-item prop="password">
              <el-input v-model="regForm.password" type="password" placeholder="密码" show-password />
            </el-form-item>
            <el-form-item prop="nickname">
              <el-input v-model="regForm.nickname" placeholder="昵称" clearable />
            </el-form-item>
            <el-button type="primary" class="submit-btn" :loading="loading" @click="doRegister">
              注 册
            </el-button>
          </el-form>
        </el-tab-pane>
      </el-tabs>
      <div class="admin-entry" @click="$router.push('/admin/login')">管理员登录</div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { login, register } from '@/api/auth'
import { useUserStore } from '@/store/user'

const router = useRouter()
const userStore = useUserStore()

const active = ref('login')
const loading = ref(false)
const formRef = ref()
const regRef = ref()

const loginForm = reactive({ phone: '', password: '' })
const regForm = reactive({ phone: '', password: '', nickname: '' })

const rules = {
  phone: [{ required: true, message: '请输入手机号', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}
const regRules = {
  ...rules,
  nickname: [{ required: true, message: '请输入昵称', trigger: 'blur' }]
}

const doLogin = async () => {
  await formRef.value.validate()
  loading.value = true
  try {
    const { data } = await login(loginForm)
    userStore.setToken(data)
    ElMessage.success('登录成功')
    router.push('/home')
  } finally {
    loading.value = false
  }
}

const doRegister = async () => {
  await regRef.value.validate()
  loading.value = true
  try {
    await register(regForm)
    ElMessage.success('注册成功，请登录')
    active.value = 'login'
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-page {
  height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #4b6cb7 0%, #182848 100%);
}
.login-card {
  width: 380px;
  padding: 10px 10px 20px;
  border-radius: 12px;
}
.title {
  text-align: center;
  color: #182848;
  margin-bottom: 10px;
}
.submit-btn {
  width: 100%;
}
.admin-entry {
  margin-top: 12px;
  text-align: center;
  color: #909399;
  font-size: 13px;
  cursor: pointer;
}
.admin-entry:hover {
  color: #4b6cb7;
}
</style>
