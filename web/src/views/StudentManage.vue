<template>
  <div class="space-y-6">
    <!-- 顶部操作栏 -->
    <div class="bg-white p-6 rounded-3xl shadow-sm border border-gray-100 flex flex-wrap items-center justify-between gap-4">
      <div class="flex items-center gap-4 flex-1 min-w-[300px]">
        <el-input
          v-model="queryParams.keyword"
          placeholder="搜索姓名或学号..."
          class="!w-72 custom-search"
          :prefix-icon="Search"
          clearable
          @clear="handleSearch"
          @keyup.enter="handleSearch"
        />
        <el-button type="primary" class="!rounded-xl !px-6" @click="handleSearch">
          <el-icon class="mr-1"><Search /></el-icon> 搜索
        </el-button>
      </div>
      
      <div class="flex items-center gap-3">
        <el-dropdown trigger="click">
          <el-button type="warning" plain class="!rounded-xl">
            <el-icon class="mr-1"><Files /></el-icon> 数据备份
          </el-button>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item @click="handleExport">
                <el-icon><Download /></el-icon> 导出数据 (JSON)
              </el-dropdown-item>
              <el-dropdown-item @click="triggerImport">
                <el-icon><Upload /></el-icon> 导入数据 (JSON)
              </el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
        <!-- 隐藏的文件上传输入框 -->
        <input 
          type="file" 
          ref="fileInput" 
          style="display: none" 
          accept=".json" 
          @change="handleImport"
        />
        <el-button type="success" plain class="!rounded-xl" @click="handleAdd">
          <el-icon class="mr-1"><Plus /></el-icon> 新增学生
        </el-button>
        <el-button type="info" plain class="!rounded-xl" @click="fetchData">
          <el-icon><Refresh /></el-icon>
        </el-button>
      </div>
    </div>

    <!-- 数据表格卡片 -->
    <div class="bg-white rounded-3xl shadow-sm border border-gray-100 overflow-hidden" v-loading="loading">
      <el-table 
        :data="tableData" 
        style="width: 100%" 
        class="custom-table"
        :header-cell-style="{ background: '#f8fafc', color: '#64748b', fontWeight: '600', height: '60px' }"
        :cell-style="{ height: '70px' }"
      >
        <template #empty>
          <div class="py-20 flex flex-col items-center justify-center">
            <el-empty description="暂无学生数据" :image-size="200" />
          </div>
        </template>

        <el-table-column prop="sno" label="学号" width="120" />
        <el-table-column prop="sname" label="姓名" min-width="120">
          <template #default="{ row }">
            <div class="flex items-center gap-3">
              <div class="w-8 h-8 rounded-full bg-blue-100 text-blue-600 flex items-center justify-center font-bold text-xs">
                {{ row.sname.charAt(0) }}
              </div>
              <span class="font-medium text-gray-700">{{ row.sname }}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="ssex" label="性别" width="80">
          <template #default="{ row }">
            <el-tag 
              :type="row.ssex === '男' ? 'primary' : 'danger'" 
              effect="light" 
              class="!rounded-lg !border-none"
            >
              {{ row.ssex }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="score" label="成绩" width="100">
          <template #default="{ row }">
            <span :class="row.score >= 60 ? 'text-green-600' : 'text-red-500'" class="font-bold">
              {{ row.score }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="shouji" label="手机号" min-width="150" />
        <el-table-column prop="sbirthday" label="生日" width="150" />
        
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <div class="flex items-center gap-2">
              <el-button type="primary" link @click="handleEdit(row)">
                <el-icon><Edit /></el-icon> 编辑
              </el-button>
              <el-button type="danger" link @click="handleDelete(row)">
                <el-icon><Delete /></el-icon> 删除
              </el-button>
            </div>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="p-6 border-t border-gray-50 flex justify-end">
        <el-pagination
          v-model:current-page="queryParams.current"
          v-model:page-size="queryParams.size"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
          @size-change="fetchData"
          @current-change="fetchData"
          class="custom-pagination"
        />
      </div>
    </div>

    <!-- 弹窗组件 -->
    <StudentDialog
      v-model:visible="dialogVisible"
      :type="dialogType"
      :row-data="currentRow"
      @success="fetchData"
    />
  </div>
</template>

<script setup>
/**
 * 学生管理页面组件
 * 实现学生信息的搜索、分页显示、新增、修改、删除以及数据备份恢复功能
 */
import { ref, reactive, onMounted } from 'vue'
import { Search, Plus, Refresh, Edit, Delete, Files, Download, Upload } from '@element-plus/icons-vue'
import { getStudentList, deleteStudent, exportStudents, importStudents } from '../api/app'
import { ElMessageBox, ElMessage } from 'element-plus'
import StudentDialog from './StudentDialog.vue'
import { useNotification } from '../composables/useNotification'

// 页面加载状态
const loading = ref(false)
// 学生表格数据
const tableData = ref([])
// 总记录数
const total = ref(0)
// 引入全局通知功能
const { addNotification } = useNotification()

// 查询参数：包含当前页、每页大小和搜索关键词
const queryParams = reactive({
  current: 1,
  size: 10,
  keyword: ''
})

// 弹窗控制相关变量
const dialogVisible = ref(false) // 弹窗显示/隐藏
const dialogType = ref('add')    // 弹窗模式：'add' 为新增，'edit' 为编辑
const currentRow = ref(null)     // 当前编辑的学生行数据
const fileInput = ref(null)      // 文件上传输入框的引用

/**
 * 从后端获取学生数据列表
 */
const fetchData = async () => {
  loading.value = true
  try {
    const res = await getStudentList(queryParams)
    tableData.value = res.records || []
    total.value = res.total || 0
    // 如果是搜索操作，添加一条通知提醒
    if (queryParams.keyword) {
      const displayTotal = total.value || tableData.value.length
      addNotification('查询成功', `搜索关键词: "${queryParams.keyword}"，共找到 ${displayTotal} 条记录`, 'info')
    }
  } catch (error) {
    console.error(error)
  } finally {
    loading.value = false
  }
}

/**
 * 处理搜索操作：重置页码为 1 并刷新数据
 */
const handleSearch = () => {
  queryParams.current = 1
  fetchData()
}

/**
 * 打开新增学生弹窗
 */
const handleAdd = () => {
  dialogType.value = 'add'
  currentRow.value = null
  dialogVisible.value = true
}

/**
 * 打开编辑学生弹窗
 * @param row 当前选中的学生行数据
 */
const handleEdit = (row) => {
  dialogType.value = 'edit'
  currentRow.value = row
  dialogVisible.value = true
}

/**
 * 处理删除学生操作
 * 弹出二次确认框，确认后调用后端逻辑删除接口
 * @param row 待删除的学生行数据
 */
const handleDelete = (row) => {
  ElMessageBox.confirm(
    `确定要删除学生 ${row.sname} 吗？`,
    '警告',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
      buttonSize: 'default',
      boxType: 'confirm',
      customClass: 'custom-message-box'
    }
  ).then(async () => {
    try {
      await deleteStudent(row.id)
      ElMessage.success('删除成功')
      addNotification('删除成功', `学生 ${row.sname} (学号: ${row.sno}) 已从系统删除`, 'warning')
      fetchData()
    } catch (error) {
      console.error(error)
    }
  })
}

/**
 * 导出数据功能
 * 将当前系统内所有学生数据导出为 JSON 文件并下载到本地
 */
const handleExport = async () => {
  try {
    const data = await exportStudents()
    // 将数据转换为 JSON 字符串并创建下载链接
    const blob = new Blob([JSON.stringify(data, null, 2)], { type: 'application/json' })
    const url = window.URL.createObjectURL(blob)
    const link = document.createElement('a')
    link.href = url
    link.download = `学生数据备份_${new Date().toLocaleDateString()}.json`
    link.click()
    window.URL.revokeObjectURL(url) // 释放内存
    addNotification('导出成功', '学生数据已成功导出为 JSON 文件', 'success')
  } catch (error) {
    console.error(error)
    ElMessage.error('导出失败')
  }
}

/**
 * 触发文件导入：点击隐藏的 file input
 */
const triggerImport = () => {
  fileInput.value.click()
}

/**
 * 处理文件导入操作
 * 读取本地 JSON 文件，并将其内容批量上传至后端
 */
const handleImport = async (event) => {
  const file = event.target.files[0]
  if (!file) return
  
  const reader = new FileReader()
  reader.onload = async (e) => {
    try {
      const data = JSON.parse(e.target.result)
      if (!Array.isArray(data)) {
        throw new Error('无效的数据格式，请提供学生数组')
      }
      
      // 弹出导入确认框
      await ElMessageBox.confirm(
        `确定要从文件导入 ${data.length} 条学生数据吗？导入后数据将新增到系统。`,
        '数据恢复',
        { type: 'info' }
      )
      
      await importStudents(data)
      ElMessage.success('导入成功')
      addNotification('恢复成功', `已成功从文件恢复 ${data.length} 条学生记录`, 'success')
      fetchData() // 刷新列表
    } catch (error) {
      console.error(error)
      ElMessage.error('导入失败：' + error.message)
    } finally {
      event.target.value = '' // 清空 input 方便下次选择
    }
  }
  reader.readAsText(file)
}

// 页面挂载时初始化数据
onMounted(() => {
  fetchData()
})
</script>

<style scoped>
:deep(.custom-search .el-input__wrapper) {
  @apply !bg-gray-50/50 !shadow-none !border-none !rounded-xl !h-11;
}

:deep(.custom-table) {
  @apply !border-none;
}

:deep(.custom-pagination .el-pager li) {
  @apply !bg-transparent !border-none !rounded-lg hover:!text-blue-600;
}

:deep(.custom-pagination .el-pager li.is-active) {
  @apply !bg-blue-50 !text-blue-600 !font-bold;
}

:deep(.custom-message-box) {
  @apply !rounded-3xl !border-none !shadow-xl;
}
</style>
