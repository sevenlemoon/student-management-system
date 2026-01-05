import { ref, readonly } from 'vue'
import { ElNotification } from 'element-plus'

/**
 * 全局通知状态管理容器
 * 使用响应式 ref 存储通知列表，实现在不同组件间共享通知数据
 */
const notifications = ref([])

/**
 * 组合式函数：useNotification
 * 提供添加通知、清除通知的功能
 */
export function useNotification() {
  /**
   * 添加一条新通知
   * @param title 通知标题
   * @param message 通知内容
   * @param type 通知类型 (success | warning | info | error)
   */
  const addNotification = (title, message, type = 'info') => {
    // 1. 将新通知插入到列表最前面（时间倒序显示）
    notifications.value.unshift({
      id: Date.now(),
      title,
      message,
      type,
      time: new Date().toLocaleTimeString()
    })
    
    // 2. 调用 Element Plus 的全局通知组件，在页面右上角弹出提示
    ElNotification({
      title: title,
      message: message,
      type: type,
      duration: 3000,
      position: 'top-right'
    })

    // 3. 性能优化：最多只在内存中保留最近的 10 条通知
    if (notifications.value.length > 10) {
      notifications.value.pop()
    }
  }

  /**
   * 清空所有通知记录
   */
  const clearNotifications = () => {
    notifications.value = []
  }

  return {
    // 暴露为只读属性，防止外部组件直接修改列表
    notifications: readonly(notifications),
    addNotification,
    clearNotifications
  }
}
