import { defineStore } from 'pinia'
import { getUserInfo } from '@/api/user'

export const useUserStore = defineStore('user', {
  state: () => ({
    token: localStorage.getItem('token') || '',
    info: null
  }),
  actions: {
    setToken(token) {
      this.token = token
      localStorage.setItem('token', token)
    },
    clear() {
      this.token = ''
      this.info = null
      localStorage.removeItem('token')
    },
    async fetchInfo() {
      const { data } = await getUserInfo()
      this.info = data
      return data
    }
  }
})
