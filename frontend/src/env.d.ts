/// <reference types="vite/client" />

// element-plus 2.x 的语言包只有 .mjs 产物，没有自带类型声明
declare module 'element-plus/dist/locale/zh-cn.mjs' {
  const zhCn: import('element-plus').Language
  export default zhCn
}
