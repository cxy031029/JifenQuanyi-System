import axios from 'axios'
import { ElMessage } from 'element-plus'
import router from '@/router'

const adminRequest = axios.create({
  baseURL: '/api',
  timeout: 10000
})

adminRequest.interceptors.request.use((config) => {
  const token = localStorage.getItem('admin_token')
  if (token) config.headers.Authorization = token
  return config
})

adminRequest.interceptors.response.use(
  (res) => {
    const r = res.data
    if (r.code === 200) return r
    if (r.code === 401) {
      localStorage.removeItem('admin_token')
      router.push('/admin/login')
    }
    ElMessage.error(r.msg || '操作失败')
    return Promise.reject(new Error(r.msg || 'error'))
  },
  (err) => {
    ElMessage.error(err.response?.data?.msg || '网络异常')
    return Promise.reject(err)
  }
)

export default adminRequest
