<template>
  <div>
    <el-card shadow="never">
      <div class="toolbar">
        <span class="title">用户详情</span>
        <div>
          <el-button @click="loadDetail">刷新</el-button>
          <el-button type="danger" :loading="loggingOut" @click="handleLogout">退出登录</el-button>
        </div>
      </div>
    </el-card>

    <el-card shadow="never" style="margin-top: 16px">
      <el-descriptions v-loading="loading" :column="1" border>
        <el-descriptions-item label="用户ID">{{ detail.id ?? '-' }}</el-descriptions-item>
        <el-descriptions-item label="用户名">{{ detail.username || '-' }}</el-descriptions-item>
        <el-descriptions-item label="昵称">{{ detail.nickname || '-' }}</el-descriptions-item>
        <el-descriptions-item label="邮箱">{{ detail.email || '-' }}</el-descriptions-item>
        <el-descriptions-item label="手机号">{{ detail.phone || '-' }}</el-descriptions-item>
        <el-descriptions-item label="注册时间">{{ detail.createTime || '-' }}</el-descriptions-item>
      </el-descriptions>
    </el-card>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getSysUserDetail } from '../api/sysUser'
import { logout } from '../api/auth'

const router = useRouter()
const loading = ref(false)
const loggingOut = ref(false)
const detail = ref({})

// 管理端用户详情：后端返回 Result<SysUser>，取 data 即用户信息
async function loadDetail() {
  loading.value = true
  try {
    const res = await getSysUserDetail()
    detail.value = res?.data || {}
  } catch (e) {
    detail.value = {}
  } finally {
    loading.value = false
  }
}

async function handleLogout() {
  try {
    await ElMessageBox.confirm('确定要退出登录吗？', '提示', { type: 'warning' })
  } catch (e) {
    return // 用户取消
  }

  loggingOut.value = true
  try {
    await logout()
    // 退出成功后清除双 token，并回到登录页
    localStorage.removeItem('accessToken')
    localStorage.removeItem('refreshToken')
    ElMessage.success('已退出登录')
    router.push('/login')
  } catch (e) {
    // 错误提示已在响应拦截器统一处理
  } finally {
    loggingOut.value = false
  }
}

onMounted(loadDetail)
</script>

<style scoped>
.toolbar {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.title {
  font-size: 16px;
  font-weight: bold;
}
</style>
