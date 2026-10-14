<template>
  <div>
    <!-- 搜索条件 -->
    <el-card shadow="never">
      <el-form :inline="true" :model="query">
        <el-form-item label="订单状态">
          <el-select
            v-model="query.status"
            placeholder="全部状态"
            clearable
            style="width: 160px"
          >
            <el-option label="待支付" :value="0" />
            <el-option label="已支付" :value="1" />
            <el-option label="已发货" :value="2" />
            <el-option label="已完成" :value="3" />
            <el-option label="已取消" :value="4" />
          </el-select>
        </el-form-item>
        <el-form-item label="订单ID">
          <el-input
            v-model="query.orderId"
            placeholder="精确查询订单ID"
            clearable
            style="width: 160px"
          />
        </el-form-item>
        <el-form-item label="用户ID">
          <el-input
            v-model="query.userId"
            placeholder="精确查询用户ID"
            clearable
            style="width: 140px"
          />
        </el-form-item>
        <el-form-item label="排序">
          <el-radio-group v-model="query.createTimeOrderBy">
            <el-radio-button :label="1">最新在前</el-radio-button>
            <el-radio-button :label="0">最早在前</el-radio-button>
          </el-radio-group>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 订单列表 -->
    <el-card shadow="never" style="margin-top: 16px">
      <el-table :data="tableData" v-loading="loading" border stripe>
        <el-table-column prop="id" label="ID" width="70" />
        <el-table-column prop="orderNo" label="订单号" min-width="200" show-overflow-tooltip />
        <el-table-column prop="userId" label="用户ID" width="90" />
        <el-table-column label="总金额(元)" width="120">
          <template #default="{ row }">¥{{ Number(row.totalAmount).toFixed(2) }}</template>
        </el-table-column>
        <el-table-column label="状态" width="110">
          <template #default="{ row }">
            <el-tag :type="statusTagType(row.status)" effect="light">
              {{ statusLabel(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="remark" label="备注" min-width="160" show-overflow-tooltip>
          <template #default="{ row }">{{ row.remark || '-' }}</template>
        </el-table-column>
        <el-table-column prop="createTime" label="下单时间" width="170" />
        <el-table-column label="操作" width="160" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="openDetail(row)">详情</el-button>
            <el-button link type="danger" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
        class="pager"
        v-model:current-page="query.pageNum"
        v-model:page-size="query.pageSize"
        :page-sizes="[5, 10, 20, 50]"
        :total="total"
        layout="total, sizes, prev, pager, next, jumper"
        @current-change="loadOrders"
        @size-change="handleSearch"
      />
    </el-card>

    <!-- 订单详情抽屉 -->
    <el-drawer
      v-model="detailVisible"
      :title="detailDrawerTitle"
      size="640px"
      direction="rtl"
    >
      <template v-if="detail">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="订单号" :span="2">{{ detail.orderNo }}</el-descriptions-item>
          <el-descriptions-item label="订单ID">{{ detail.id }}</el-descriptions-item>
          <el-descriptions-item label="用户ID">{{ detail.userId }}</el-descriptions-item>
          <el-descriptions-item label="总金额">¥{{ Number(detail.totalAmount).toFixed(2) }}</el-descriptions-item>
          <el-descriptions-item label="状态">
            <el-tag :type="statusTagType(detail.status)" effect="light">
              {{ statusLabel(detail.status) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="备注" :span="2">{{ detail.remark || '-' }}</el-descriptions-item>
          <el-descriptions-item label="下单时间">{{ detail.createTime }}</el-descriptions-item>
          <el-descriptions-item label="更新时间">{{ detail.updateTime }}</el-descriptions-item>
        </el-descriptions>

        <h3 class="section-title">订单明细（共 {{ detail.orderItems?.length || 0 }} 项，总数量 {{ totalQuantity }}）</h3>
        <el-table :data="detail.orderItems || []" border size="small">
          <el-table-column prop="productName" label="商品名称" min-width="200" show-overflow-tooltip />
          <el-table-column label="单价(元)" width="100">
            <template #default="{ row }">¥{{ Number(row.productPrice).toFixed(2) }}</template>
          </el-table-column>
          <el-table-column prop="productQuantity" label="数量" width="70" align="center" />
          <el-table-column label="小计(元)" width="100">
            <template #default="{ row }">¥{{ Number(row.subtotal).toFixed(2) }}</template>
          </el-table-column>
        </el-table>
      </template>
    </el-drawer>
  </div>
</template>

<script setup>
import { computed, onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { pageOrders, deleteOrder } from '../api/order'

const STATUS_MAP = {
  0: { label: '待支付', tag: 'warning' },
  1: { label: '已支付', tag: 'primary' },
  2: { label: '已发货', tag: '' },
  3: { label: '已完成', tag: 'success' },
  4: { label: '已取消', tag: 'info' }
}
const statusLabel = (s) => STATUS_MAP[s]?.label ?? '未知'
const statusTagType = (s) => STATUS_MAP[s]?.tag ?? 'info'

const loading = ref(false)
const tableData = ref([])
const total = ref(0)

const query = reactive({
  pageNum: 1,
  pageSize: 5,
  status: null,
  userId: null,
  orderId: null,
  // 后端: 0=升序, 1=倒序; 不设则默认倒序
  createTimeOrderBy: 1
})

// 详情抽屉：直接用列表里的原始行（已经带 orderItems）
const detailVisible = ref(false)
const detail = ref(null)
const detailDrawerTitle = computed(() =>
  detail.value ? `订单 ${detail.value.orderNo} 详情` : '订单详情'
)
const totalQuantity = computed(() =>
  (detail.value?.orderItems || []).reduce((s, it) => s + (it.productQuantity || 0), 0)
)

async function loadOrders() {
  loading.value = true
  try {
    // OrderPageDTO 里 orderId / userId / status 是精确匹配, 传 null 后端忽略
    const payload = {
      pageNum: query.pageNum,
      pageSize: query.pageSize,
      status: query.status ?? null,
      userId: query.userId ? Number(query.userId) : null,
      orderId: query.orderId ? Number(query.orderId) : null,
      createTimeOrderBy: query.createTimeOrderBy ?? null
    }
    const data = await pageOrders(payload)
    tableData.value = data?.data?.list || []
    total.value = Number(data?.data?.total || 0)
  } catch (e) {
    tableData.value = []
    total.value = 0
  } finally {
    loading.value = false
  }
}

function handleSearch() {
  query.pageNum = 1
  loadOrders()
}

function handleReset() {
  query.status = null
  query.userId = null
  query.orderId = null
  query.createTimeOrderBy = 1
  handleSearch()
}

function openDetail(row) {
  // 直接用列表数据，后端一次返回已带 orderItems
  detail.value = row
  detailVisible.value = true
}

async function handleDelete(row) {
  try {
    await ElMessageBox.confirm(
      `确定删除订单 ${row.orderNo} 吗？删除后不可恢复`,
      '提示',
      { type: 'warning' }
    )
  } catch (e) {
    return // 用户取消
  }

  try {
    await deleteOrder(row.id)
    ElMessage.success('删除成功')
    // 删除后若当前页已空，回退一页
    if (tableData.value.length === 1 && query.pageNum > 1) {
      query.pageNum -= 1
    }
    loadOrders()
  } catch (e) {
    // 错误提示已在响应拦截器统一处理
  }
}

onMounted(loadOrders)
</script>

<style scoped>
.pager {
  margin-top: 16px;
  justify-content: flex-end;
}

.section-title {
  margin-top: 20px;
  margin-bottom: 12px;
  font-size: 15px;
  font-weight: bold;
}
</style>
