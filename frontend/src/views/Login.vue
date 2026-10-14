<template>
  <div class="auth-wrap">
    <el-card class="auth-card">
      <template #header>
        <div class="card-title">登录</div>
      </template>

      <el-form :model="form" label-width="70px" @submit.prevent>
        <el-form-item label="用户名">
          <el-input v-model="form.username" placeholder="请输入用户名" />
        </el-form-item>
        <el-form-item label="密码">
          <el-input
            v-model="form.password"
            type="password"
            placeholder="请输入密码"
            show-password
            @keyup.enter="handleLogin"
          />
        </el-form-item>
      </el-form>

      <div class="btn-group">
        <el-button type="primary" :loading="loading" @click="handleLogin">登录</el-button>
      </div>

      <div class="tip">
        还没有账号？
        <el-link type="primary" :underline="false" @click="router.push('/register')">去注册</el-link>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { login } from '../api/auth'

const router = useRouter()
const loading = ref(false)

const form = reactive({
  username: '',
  password: ''
})

// 登录：调用后端接口，成功后写入 token
async function handleLogin() {
  if (!form.username || !form.password) {
    ElMessage.warning('请输入用户名和密码')
    return
  }
  loading.value = true
  try {
    const res = await login({ username: form.username, password: form.password })
    // 后端返回 { code, data: { accessToken, refreshToken }, msg }
    localStorage.setItem('accessToken', res.data.accessToken)
    localStorage.setItem('refreshToken', res.data.refreshToken)
    ElMessage.success('登录成功')
    router.push('/product')
  } catch (e) {
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