import request from './request'

export const getUserInfo = () => request.get('/user/info')
export const updateUserInfo = (data) => request.put('/user/info', data)
