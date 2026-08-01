import axios from 'axios'
import { ElMessage } from 'element-plus'
import router from '@/router'

const request = axios.create({
  baseURL: '/api',
  timeout: 10000
})

request.interceptors.request.use((config) => {
  const token = localStorage.getItem('token')
  if (token) config.headers.Authorization = token
  return config
})

request.interceptors.response.use(
  (res) => {
    const r = res.data
    if (r.code === 200) return r
    if (r.code === 401) {
      localStorage.removeItem('token')
      router.push('/login')
    }
    ElMessage.error(r.msg || '操作失败')
    return Promise.reject(new Error(r.msg || 'error'))
  },
  (err) => {
    ElMessage.error(err.response?.data?.msg || '网络异常')
    return Promise.reject(err)
  }
)

export default request
