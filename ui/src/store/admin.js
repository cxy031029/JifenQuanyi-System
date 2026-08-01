import { defineStore } from 'pinia'
import { getAdminInfo } from '@/api/admin'

export const useAdminStore = defineStore('admin', {
  state: () => ({
    token: localStorage.getItem('admin_token') || '',
    info: null
  }),
  actions: {
    setToken(token) {
      this.token = token
      localStorage.setItem('admin_token', token)
    },
    clear() {
      this.token = ''
      this.info = null
      localStorage.removeItem('admin_token')
    },
    async fetchInfo() {
      const { data } = await getAdminInfo()
      this.info = data
      return data
    }
  }
})
