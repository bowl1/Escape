# FakeCall App — 项目文件结构

```
fakecall/
├── app/
│   ├── build.gradle.kts
│   └── src/
│       └── main/
│           ├── AndroidManifest.xml
│           ├── res/
│           │   ├── drawable/
│           │   │   └── ic_call.xml               # 通话图标
│           │   ├── font/                          # Plus Jakarta Sans 字体
│           │   ├── mipmap-anydpi-v26/             # 启动图标
│           │   ├── raw/                           # 预留（模拟通话音频）
│           │   └── values/
│           │       ├── strings.xml
│           │       └── themes.xml
│           │
│           └── kotlin/com/libowen/fakecall/
│               │
│               ├── FakeCallApp.kt                # Application 类，Hilt 入口
│               │
│               ├── ui/
│               │   ├── MainActivity.kt           # 主 Activity，托管 NavHost（Home + Contacts）
│               │   │
│               │   ├── home/
│               │   │   ├── HomeScreen.kt         # 主界面（随机/自定义来电 + 定时触发）
│               │   │   └── HomeViewModel.kt
│               │   │
│               │   ├── contacts/
│               │   │   ├── ContactsScreen.kt     # 联系人列表界面
│               │   │   ├── ContactsViewModel.kt
│               │   │   └── AddContactScreen.kt   # 新增/编辑联系人界面
│               │   │
│               │   ├── call/
│               │   │   ├── IncomingCallActivity.kt   # 来电界面 Activity（锁屏弹出）
│               │   │   ├── IncomingCallScreen.kt     # 来电 Compose 界面
│               │   │   │
│               │   │   ├── InCallActivity.kt         # 通话中 Activity
│               │   │   ├── InCallScreen.kt           # 通话中 Compose 界面
│               │   │   │
│               │   │   ├── CallEndedActivity.kt      # 通话结束 Activity
│               │   │   └── CallEndedScreen.kt        # 通话结束 Compose 界面
│               │   │
│               │   ├── components/
│               │   │   ├── CallerAvatar.kt           # 头像组件（带默认头像 fallback）
│               │   │   ├── CallActionButton.kt       # 通话操作圆形按钮
│               │   │   └── TimerText.kt              # 通话计时文字组件
│               │   │
│               │   └── theme/
│               │       ├── Color.kt
│               │       ├── Theme.kt
│               │       └── Type.kt
│               │
│               ├── domain/
│               │   ├── model/
│               │   │   ├── CallerInfo.kt             # 来电者信息（name, number, avatarUri）
│               │   │   └── FakeCallConfig.kt         # 触发配置（delayMs, callerId）
│               │   │
│               │   ├── repository/
│               │   │   ├── CallerRepository.kt       # 接口
│               │   │   └── SettingsRepository.kt     # 接口
│               │   │
│               │   └── usecase/
│               │       ├── GenerateRandomCallerUseCase.kt  # 随机生成丹麦来电者信息
│               │       ├── ScheduleFakeCallUseCase.kt      # 安排定时来电
│               │       ├── CancelFakeCallUseCase.kt        # 取消定时来电
│               │       ├── GetCallerInfoUseCase.kt         # 获取当前来电者信息
│               │       └── SaveCallerPresetUseCase.kt      # 保存来电者预设
│               │
│               ├── data/
│               │   ├── repository/
│               │   │   ├── CallerRepositoryImpl.kt   # 调用 CallerDao 实现联系人增删改查
│               │   │   └── SettingsRepositoryImpl.kt # 调用 SettingsDataStore 实现设置读写
│               │   │
│               │   ├── db/
│               │   │   ├── AppDatabase.kt            # Room 数据库入口
│               │   │   ├── entity/
│               │   │   │   └── CallerEntity.kt       # 联系人表结构
│               │   │   └── dao/
│               │   │       └── CallerDao.kt          # 联系人增删改查
│               │   │
│               │   ├── datastore/
│               │   │   └── SettingsDataStore.kt      # 仅存 App 设置（延迟、铃声等）
│               │   │
│               │   └── mapper/
│               │       └── CallerMapper.kt           # CallerEntity ↔ CallerInfo 转换
│               │
│               └── system/
│                   ├── FakeCallReceiver.kt           # BroadcastReceiver，接收闹钟触发
│                   ├── FakeCallService.kt            # Foreground Service，通话中保活 + 计时
│                   ├── BootReceiver.kt               # 开机恢复定时任务
│                   ├── NotificationHelper.kt         # 通知创建（来电通知 / 通话中通知）
│                   ├── AlarmHelper.kt                # AlarmManager 封装
│                   ├── RingtoneHelper.kt             # MediaPlayer + Vibrator 铃声封装
│                   └── di/
│                       ├── DatabaseModule.kt         # Hilt 提供 AppDatabase 和 CallerDao
│                       └── RepositoryModule.kt       # Hilt 绑定 Repository 接口
│
├── build.gradle.kts                                  # 根级 Gradle
├── settings.gradle.kts
├── gradle.properties
│
├── PRD.md                                            # 产品需求文档
├── TECH_SPEC.md                                      # 技术方案文档
├── FILE_STRUCTURE.md                                 # 本文件
└── README.md
```

---

## 关键文件说明

### `FakeCallReceiver.kt`
接收 `AlarmManager` 广播，调用 `NotificationHelper` 弹出来电通知（含 Full-Screen Intent），触发 `IncomingCallActivity`。

### `BootReceiver.kt`
监听 `BOOT_COMPLETED`，设备重启后恢复之前未触发的定时来电任务。

### `IncomingCallActivity.kt`
- 设置 `setShowWhenLocked(true)` + `setTurnScreenOn(true)`
- 启动 `RingtoneHelper` 播放铃声 + 振动
- 用户接听 → 跳转 `InCallActivity`，停止铃声
- 用户拒接 → 清除通知，返回主屏幕

### `InCallActivity.kt`
- 启动 `FakeCallService`（Foreground Service）
- 绑定 Service 获取计时 `StateFlow`
- 用户挂断 → 停止 Service，跳转 `CallEndedActivity`

### `FakeCallService.kt`
- 维护通话时长计时（每秒 +1）
- 通过 `StateFlow<Int>` 暴露给 `InCallScreen`
- 显示"通话中"持续通知

### `HomeViewModel.kt`
- `triggerImmediately()` → `ScheduleFakeCallUseCase(delay = 1500ms)`，用随机或自定义来电者
- `scheduleLater(delayMs)` → `ScheduleFakeCallUseCase(delay = delayMs)`
- `cancelScheduled()` → `CancelFakeCallUseCase()`

### `NotificationHelper.kt`
- `CHANNEL_INCOMING_CALL`: 来电通知渠道（优先级 MAX，CATEGORY_CALL）
- `CHANNEL_IN_CALL`: 通话中通知渠道（持续显示）
- `showIncomingCallNotification(caller)`: 带 Full-Screen Intent
- `showInCallNotification(duration)`: 通话中常驻通知
- `clearIncomingCall()` / `clearAll()`: 清除通知
```
