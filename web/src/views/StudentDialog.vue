<template>
  <el-dialog
    v-model="dialogVisible"
    :title="type === 'add' ? '新增学生' : (type === 'edit' ? '编辑学生' : '学生详情')"
    width="500px"
    class="custom-dialog"
    :before-close="handleClose"
    destroy-on-close
  >
    <el-form
      ref="formRef"
      :model="form"
      :rules="rules"
      label-position="top"
      class="p-2"
      :disabled="type === 'detail'"
    >
      <div class="grid grid-cols-2 gap-4">
        <el-form-item label="学号" prop="sno" class="col-span-1">
          <el-input v-model="form.sno" placeholder="请输入学号" :disabled="type === 'edit'" />
        </el-form-item>
        <el-form-item label="姓名" prop="sname" class="col-span-1">
          <el-input v-model="form.sname" placeholder="请输入姓名" />
        </el-form-item>
      </div>

      <div class="grid grid-cols-2 gap-4">
        <el-form-item label="性别" prop="ssex" class="col-span-1">
          <el-select v-model="form.ssex" placeholder="请选择性别" class="w-full">
            <el-option label="男" value="男" />
            <el-option label="女" value="女" />
            <el-option label="其他" value="其他" />
          </el-select>
        </el-form-item>
        <el-form-item label="成绩" prop="score" class="col-span-1">
          <el-input-number v-model="form.score" :min="0" :max="100" :precision="2" class="!w-full" />
        </el-form-item>
      </div>

      <el-form-item label="出生日期" prop="sbirthday">
        <el-date-picker
          v-model="form.sbirthday"
          type="date"
          placeholder="选择日期"
          class="!w-full"
          value-format="YYYY-MM-DD"
        />
      </el-form-item>

      <el-form-item label="手机号码" prop="shouji">
        <el-input v-model="form.shouji" placeholder="请输入手机号码" />
      </el-form-item>
    </el-form>

    <template #footer v-if="type !== 'detail'">
      <div class="flex gap-3 justify-end px-2 pb-2">
        <el-button @click="handleClose" class="!rounded-xl">取消</el-button>
        <el-button 
          type="primary" 
          :loading="loading" 
          @click="handleSubmit"
          class="!rounded-xl !px-8 shadow-lg shadow-blue-100"
        >
          确定
        </el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup>
/**
 * 学生表单弹窗组件
 * 用于新增或编辑学生信息的表单输入
 */
import { ref, watch, computed } from 'vue'
import { addStudent, updateStudent } from '../api/app'
import { ElMessage } from 'element-plus'
import { useNotification } from '../composables/useNotification'

// 定义 Props：接收父组件传递的显示状态、操作类型和当前行数据
const props = defineProps({
  visible: Boolean, // 是否显示弹窗
  type: String,    // 'add' 或 'edit'
  rowData: Object  // 编辑模式下的学生数据对象
})

// 定义事件：用于向父组件通信（更新可见性、操作成功回调）
const emit = defineEmits(['update:visible', 'success'])
// 引入通知功能
const { addNotification } = useNotification()

// 计算属性：实现 v-model 绑定的双向同步
const dialogVisible = computed({
  get: () => props.visible,
  set: (val) => emit('update:visible', val)
})

// 表单引用和加载状态
const formRef = ref(null)
const loading = ref(false)

// 表单响应式数据对象
const form = ref({
  sno: '',
  sname: '',
  ssex: '男',
  score: 0,
  sbirthday: '',
  shouji: ''
})

// 表单验证规则
const rules = {
  sno: [{ required: true, message: '请输入学号', trigger: 'blur' }],
  sname: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
  shouji: [
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号码', trigger: 'blur' }
  ]
}

/**
 * 监听 visible 属性变化
 * 当弹窗打开时，根据 type 类型决定是清空表单（新增）还是回显数据（编辑）
 */
watch(() => props.visible, (val) => {
  if (val) {
    if (props.type !== 'add' && props.rowData) {
      // 编辑模式：回显数据
      form.value = { ...props.rowData }
    } else {
      // 新增模式：重置表单
      form.value = {
        sno: '',
        sname: '',
        ssex: '男',
        score: 0,
        sbirthday: '',
        shouji: ''
      }
    }
  }
})

/**
 * 关闭弹窗
 */
const handleClose = () => {
  emit('update:visible', false)
}

/**
 * 提交表单
 * 执行表单校验，通过后根据 type 调用新增或修改 API
 */
const handleSubmit = async () => {
  if (!formRef.value) return
  
  await formRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        if (props.type === 'add') {
          // 调用新增 API
          await addStudent(form.value)
          ElMessage.success('添加成功')
          addNotification('新增成功', `已成功添加学生: ${form.value.sname}`, 'success')
        } else {
          // 调用更新 API
          await updateStudent(form.value)
          ElMessage.success('更新成功')
          addNotification('修改成功', `已更新学生 ${form.value.sname} 的信息`, 'success')
        }
        emit('success') // 通知父组件操作成功
        handleClose()   // 关闭弹窗
      } catch (error) {
        console.error(error)
      } finally {
        loading.value = false
      }
    }
  })
}
</script>

<style>
.custom-dialog {
  @apply !rounded-3xl overflow-hidden;
}
.custom-dialog .el-dialog__header {
  @apply !p-6 !mb-0 border-b border-gray-50;
}
.custom-dialog .el-dialog__title {
  @apply !text-xl !font-bold !text-gray-800;
}
.custom-dialog .el-dialog__body {
  @apply !p-6;
}
.custom-dialog .el-form-item__label {
  @apply !text-gray-500 !font-medium !mb-1;
}
.custom-dialog .el-input__wrapper,
.custom-dialog .el-select .el-input__wrapper {
  @apply !bg-gray-50/50 !shadow-none !border-none !rounded-xl !h-11;
}
</style>
