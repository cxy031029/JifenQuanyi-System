import request from './request'

export const getActivities = () => request.get('/seckill/activities')
export const seckill = (activityId) => request.post(`/seckill/${activityId}`)
