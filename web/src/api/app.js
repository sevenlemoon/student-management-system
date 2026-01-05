import request from './index'

/**
 * 用户身份验证相关接口
 */
// 登录接口：提交用户名和密码
export const login = (data) => request.post('/user/login', data)
// 注册接口：创建新管理员账号
export const register = (data) => request.post('/user/register', data)

/**
 * 学生信息管理相关接口
 */
// 获取学生分页列表：支持关键词搜索和分页参数
export const getStudentList = (params) => request.get('/student/list', { params })
// 新增学生信息
export const addStudent = (data) => request.post('/student/add', data)
// 修改学生信息：根据 ID 更新记录
export const updateStudent = (data) => request.put('/student/update', data)
// 删除学生信息：逻辑删除指定 ID 的记录
export const deleteStudent = (id) => request.delete(`/student/delete/${id}`)
// 根据 ID 获取单个学生详细信息
export const getStudentById = (id) => request.get(`/student/get/${id}`)

/**
 * 数据备份与恢复接口
 */
// 导出学生数据：获取系统中所有学生记录的 JSON 格式
export const exportStudents = () => request.get('/student/export')
// 导入学生数据：批量恢复学生记录
export const importStudents = (data) => request.post('/student/import', data)
