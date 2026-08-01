import request from './request'

export const getFlows = (params) => request.get('/bill/flows', { params })
