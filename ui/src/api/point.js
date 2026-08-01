import request from './request'

export const getBalance = () => request.get('/point/balance')
export const getSignToday = () => request.get('/signin/today')
export const signIn = () => request.post('/signin')
export const getExpireRecords = () => request.get('/expire')
