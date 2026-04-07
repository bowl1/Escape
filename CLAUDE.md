# FakeCall — Claude 项目记忆

## 项目概述
FakeCall 是一款 Android 假来电应用，帮助用户优雅脱身于尴尬场合。

## 技术栈
- 语言：Kotlin 1.9.22
- UI：Jetpack Compose + Material Design 3
- 架构：MVVM + Clean Architecture（3层：UI / Domain / Data）
- DI：Hilt（使用 KSP）
- 数据库：Room 2.8.4（联系人存储）
- 设置存储：DataStore Preferences（仅存延迟时间、铃声等）
- 定时任务：AlarmManager + BroadcastReceiver
- 锁屏来电：Full-Screen Intent + Notification
- 后台保活：Foreground Service（phoneCall 类型）
- 音频：MediaPlayer + Vibrator
- 图片加载：Coil

## 包名
com.libowen.fakecall

## SDK 版本
- 最低：API 26 (Android 8.0)
- 目标：API 34 (Android 14)

## 字体
Plus Jakarta Sans（需下载 ttf 放入 res/font/）

## 本地化
- 目标市场：丹麦
- 手机号格式：XX XX XX XX（8位）
- 内置丹麦名字库：关系称呼优先 60%，普通姓名 40%

## 架构规则
- UI 层只能调用 ViewModel
- ViewModel 只能调用 UseCase
- UseCase 只能调用 Repository / System Helper
- Repository 实现在 data 层，接口定义在 domain 层
- System 层（Service/Receiver）通过 Hilt @AndroidEntryPoint 注入

## 关键设计决策
- 随机来电：内存生成，不保存（id = "random"）
- 联系人：Room，isDefault 字段标记当前默认联系人
- NavHost 托管 Home + Contacts 两个页面；来电/通话界面用独立 Activity，需要锁屏 flag
- 通话计时：FakeCallService（Foreground）通过 StateFlow 推给 InCallScreen
- 定时：AlarmManager.setExactAndAllowWhileIdle()，extras 传 caller 信息
- 开机恢复：BootReceiver 监听 BOOT_COMPLETED 恢复未触发的定时任务

## 注意事项
- Android 12+ 需要用户授权 SCHEDULE_EXACT_ALARM
- Android 13+ 需要请求 POST_NOTIFICATIONS 权限
- Android 14 Full-Screen Intent 受限，需引导用户设置
- 部分厂商 ROM 限制后台弹窗，提示用户设置白名单
