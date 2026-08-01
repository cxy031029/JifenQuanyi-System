import adminRequest from './adminRequest'

export const adminLogin = (data) => adminRequest.post('/admin/auth/login', data)
export const adminLogout = () => adminRequest.post('/admin/auth/logout')
export const getAdminInfo = () => adminRequest.get('/admin/auth/info')

export const getOverview = () => adminRequest.get('/admin/overview')

export const getUserPage = (params) => adminRequest.get('/admin/users', { params })
export const changeUserStatus = (id, status) => adminRequest.put(`/admin/users/${id}/status`, null, { params: { status } })

export const getFlowPage = (params) => adminRequest.get('/admin/flows', { params })
