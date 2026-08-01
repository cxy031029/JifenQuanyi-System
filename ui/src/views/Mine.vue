<template>
  <div>
    <el-card shadow="never" class="mine-card">
      <template #header>个人信息</template>
      <el-form v-if="info" label-width="80px" style="max-width: 420px">
        <el-form-item label="手机号">
          <el-input :model-value="info.phone" disabled />
        </el-form-item>
        <el-form-item label="昵称">
          <el-input v-model="form.nickname" placeholder="昵称" />
        </el-form-item>
        <el-form-item label="头像">
          <el-input v-model="form.avatar" placeholder="头像地址" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :loading="saving" @click="save">保存</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card shadow="never">
      <el-button type="danger" plain @click="logout">退出登录</el-button>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { updateUserInfo } from '@/api/user'
import { useUserStore } from '@/store/user'

const router = useRouter()
const userStore = useUserStore()

const info = ref(null)
const saving = ref(false)
const form = reactive({ nickname: '', avatar: '' })

const load = async () => {
  const data = await userStore.fetchInfo()
  info.value = data
  form.nickname = data.nickname || ''
  form.avatar = data.avatar || ''
}

const save = async () => {
  saving.value = true
  try {
    await updateUserInfo({ nickname: form.nickname, avatar: form.avatar })
    ElMessage.success('保存成功')
    await load()
  } finally {
    saving.value = false
  }
}

const logout = async () => {
  await ElMessageBox.confirm('确定退出登录吗？', '提示', { type: 'warning' })
  userStore.clear()
  router.push('/login')
}

onMounted(load)
</script>

<style scoped>
.mine-card {
  margin-bottom: 20px;
}
</style>
