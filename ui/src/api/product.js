import request from './request'

export const getProductList = (params) => request.get('/product/list', { params })
export const exchange = (data) => request.post('/exchange', data)
export const getExchangeRecords = (params) => request.get('/exchange/records', { params })
