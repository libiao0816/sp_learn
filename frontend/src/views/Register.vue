<template>
  <div class="auth-wrap">
    <el-card class="auth-card">
      <template #header>
        <div class="card-title">注册</div>
      </template>

      <el-form :model="form" label-width="90px" @submit.prevent>
        <el-form-item label="用户名">
          <el-input v-model="form.username" placeholder="请输入用户名" />
        </el-form-item>
        <el-form-item label="密码">
          <el-input
            v-model="form.password"
            type="password"
            placeholder="请输入密码"
            show-password
          />
        </el-form-item>
        <el-form-item label="确认密码">
          <el-input
            v-model="form.confirmPassword"
            type="password"
            placeholder="请再次输入密码"
            show-password
            @keyup.enter="handleRegister"
          />
        </el-form-item>
      </el-form>

      <div class="btn-group">
        <el-button type="primary" :loading="loading" @click="handleRegister">注册</el-button>
      </div>

      <div class="tip">
        已有账号？
        <el-link type="primary" :underline="false" @click="router.push('/login')">去登录</el-link>
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { register } from '../api/auth'

const router = useRouter()
const loading = ref(false)

const form = reactive({
  username: '',
  password: '',
  confirmPassword: ''
})

// 注册：调用后端接口，成功后跳转登录页
async function handleRegister() {
  if (!form.username || !form.password) {
    ElMessage.warning('请输入用户名和密码')
    return
  }
  if (form.password !== form.confirmPassword) {
    ElMessage.warning('两次输入的密码不一致')
    return
  }
  loading.value = true
  try {
    await register({ username: form.username, password: form.password })
    ElMessage.success('注册成功，请登录')
    router.push('/login')
  } catch {
    // 错误提示已在响应拦截器统一处理
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.auth-wrap {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  padding: 20px;
  background-color: #f5f7fa;
}

.auth-card {
  width: 460px;
}

.card-title {
  font-size: 16px;
  font-weight: bold;
}

.btn-group {
  display: flex;
  justify-content: center;
  gap: 12px;
  margin-top: 8px;
}

.tip {
  margin-top: 16px;
  text-align: center;
  font-size: 13px;
  color: #909399;
}
</style>