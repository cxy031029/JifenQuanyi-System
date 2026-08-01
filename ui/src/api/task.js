import request from './request'

export const getTaskList = () => request.get('/task/list')
export const claimTask = (taskId) => request.post(`/task/claim/${taskId}`)
