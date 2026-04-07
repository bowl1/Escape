# FakeCall App — 技术方案文档

**版本**: v1.0
**平台**: Android (Kotlin)
**更新日期**: 2026-04-04

---

## 1. 技术选型

| 技术 | 选型 | 说明 |
|------|------|------|
| 语言 | Kotlin | 100% Kotlin |
| UI 框架 | Jetpack Compose | Material Design 3 |
| 架构 | MVVM + Clean Architecture | 单向数据流 |
| DI | Hilt | 依赖注入 |
| 联系人存储 | Room (SQLite) | 联系人增删改查，方便后续扩展 |
| 设置存储 | DataStore (Preferences) | 延迟时间、铃声等简单配置 |
| 定时任务 | AlarmManager + BroadcastReceiver | 精确定时触发来电 |
| 后台服务 | Foreground Service | 通话中保活 |
| 锁屏来电 | Full-Screen Intent + Notification | 穿透锁屏 |
| 音频 | MediaPlayer | 播放铃声 |
| 最低 API | 26 (Android 8.0) | 支持 Full-Screen Intent |
| 目标 API | 34 (Android 14) | 最新权限模型 |

---

## 2. 架构设计

### 2.1 整体分层

```
┌─────────────────────────────────────┐
│            UI Layer                  │
│  (Compose Screens + ViewModels)      │
├─────────────────────────────────────┤
│           Domain Layer               │
│  (UseCases, Models)                  │
├─────────────────────────────────────┤
│            Data Layer                │
│  (Repositories, DataStore)           │
├─────────────────────────────────────┤
│          System Layer                │
│  (AlarmManager, Service, Receiver)   │
└─────────────────────────────────────┘
```

### 2.2 核心模块

```
app
├── ui/           # Compose 界面 + ViewModel
├── domain/       # 业务逻辑 UseCases + 数据模型
├── data/         # Repository + DataStore
└── system/       # Service + BroadcastReceiver + Notification
```

---

## 3. 核心技术实现

### 3.1 随机来电生成（GenerateRandomCallerUseCase）

名字库分两层：**关系称呼**（优先级高，更真实）+ **普通姓名**（补充数量）。

```kotlin
// domain/usecase/GenerateRandomCallerUseCase.kt
class GenerateRandomCallerUseCase @Inject constructor() {

    // 关系称呼（丹麦语，更真实，更容易让旁人信服）
    private val relationships = listOf(
        "Mor",           // 妈妈
        "Far",           // 爸爸
        "Kæreste",       // 伴侣/男女朋友
        "Chef",          // 老板
        "Søster",        // 姐妹
        "Bror",          // 兄弟
        "Bedste",        // 奶奶/外婆
        "Kollega Mads",  // 同事 Mads
        "Kollega Sara",  // 同事 Sara
        "Tandlæge",      // 牙医（很好用的借口）
        "Læge"           // 医生（紧急借口）
    )

    // 丹麦男性名字
    private val maleNames = listOf(
        "Mikkel", "Anders", "Lars", "Søren", "Mads", "Christian",
        "Thomas", "Peter", "Henrik", "Niels", "Jakob", "Rasmus",
        "Jonas", "Kasper", "Frederik", "Emil", "Oliver", "Lucas",
        "Noah", "Victor", "Magnus", "Tobias", "Simon", "Daniel"
    )

    // 丹麦女性名字
    private val femaleNames = listOf(
        "Emma", "Sofia", "Freja", "Laura", "Anna", "Sara",
        "Maja", "Ida", "Astrid", "Sofie", "Julie", "Maria",
        "Mathilde", "Camilla", "Louise", "Katrine", "Line",
        "Pernille", "Trine", "Cecilie", "Clara", "Nora", "Ella"
    )

    // 丹麦最常见姓氏
    private val surnames = listOf(
        "Jensen", "Nielsen", "Hansen", "Pedersen", "Andersen",
        "Christensen", "Larsen", "Sørensen", "Rasmussen", "Jørgensen",
        "Petersen", "Madsen", "Kristensen", "Olsen", "Thomsen",
        "Poulsen", "Johansen", "Møller", "Knudsen", "Mortensen"
    )

    // 丹麦手机号段（8位，移动号段开头）
    private val mobilePrefixes = listOf(
        "20", "21", "22", "23", "24", "25", "26", "27", "28", "29",
        "30", "31", "40", "41", "42", "50", "51", "52", "53",
        "60", "61", "71", "81", "91", "93"
    )

    operator fun invoke(): CallerInfo {
        // 60% 概率用关系称呼，40% 用普通姓名
        val name = if (Math.random() < 0.6) {
            relationships.random()
        } else {
            val isMale = Math.random() < 0.5
            val firstName = if (isMale) maleNames.random() else femaleNames.random()
            "$firstName ${surnames.random()}"
        }

        return CallerInfo(
            id = "random",
            name = name,
            number = generateDanishNumber(),
            avatarUri = null   // 用默认头像，不保存
        )
    }

    private fun generateDanishNumber(): String {
        val prefix = mobilePrefixes.random()
        val suffix = (100000..999999).random()
        val raw = "$prefix$suffix"
        // 丹麦格式：XX XX XX XX
        return "${raw.substring(0,2)} ${raw.substring(2,4)} ${raw.substring(4,6)} ${raw.substring(6,8)}"
    }
}
```

**设计决策：**
| 决策 | 原因 |
|------|------|
| 关系称呼优先（60%） | "Mor ringer"比"Rasmus Jensen ringer"更让旁人信服 |
| Tandlæge / Læge | 医生/牙医来电是丹麦最经典的脱身借口，无人质疑 |
| 60% / 40% 比例 | 避免每次都是称呼，偶尔普通姓名更自然 |
| 不保存随机结果 | `id = "random"` 固定，不写入 DataStore，用完即弃 |
| 丹麦8位号码格式 | `XX XX XX XX` 符合丹麦手机号规范，看起来真实 |

---

### 3.2 定时触发来电

**方案**: `AlarmManager.setExactAndAllowWhileIdle()` + `BroadcastReceiver`

```kotlin
// 设置精确定时闹钟
fun scheduleFakeCall(delayMs: Long) {
    val triggerTime = System.currentTimeMillis() + delayMs
    val intent = Intent(context, FakeCallReceiver::class.java)
    val pendingIntent = PendingIntent.getBroadcast(
        context, REQUEST_CODE, intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )
    alarmManager.setExactAndAllowWhileIdle(
        AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent
    )
}
```

**BroadcastReceiver 接收**:
```kotlin
class FakeCallReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        // 启动来电 Activity / Service
        IncomingCallActivity.launch(context)
    }
}
```

### 3.3 锁屏来电 (Full-Screen Intent)

**方案**: 发送带 `fullScreenIntent` 的高优先级通知，Android 系统会在锁屏状态弹出对应 Activity。

```kotlin
fun showIncomingCallNotification(caller: CallerInfo) {
    val fullScreenIntent = Intent(context, IncomingCallActivity::class.java)
    val fullScreenPendingIntent = PendingIntent.getActivity(
        context, 0, fullScreenIntent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    val notification = NotificationCompat.Builder(context, CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_call)
        .setContentTitle(caller.name)
        .setContentText(caller.number)
        .setPriority(NotificationCompat.PRIORITY_MAX)
        .setCategory(NotificationCompat.CATEGORY_CALL)
        .setFullScreenIntent(fullScreenPendingIntent, true)
        .setAutoCancel(false)
        .setOngoing(true)
        .build()

    notificationManager.notify(INCOMING_CALL_NOTIFICATION_ID, notification)
}
```

**IncomingCallActivity** 需设置 window flags 亮屏：
```kotlin
override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    // 亮屏 + 解锁屏幕显示
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
        setShowWhenLocked(true)
        setTurnScreenOn(true)
    } else {
        window.addFlags(
            WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
            WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON or
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
        )
    }
}
```

### 3.4 通话中保活 (Foreground Service)

**方案**: 接听后启动 `FakeCallService`（Foreground Service），保证进程不被杀死，维护通话计时。

```kotlin
class FakeCallService : Service() {
    private var callDurationSeconds = 0
    private val timer = Timer()

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground(NOTIFICATION_ID, buildCallNotification())
        startTimer()
        return START_STICKY
    }

    private fun startTimer() {
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                callDurationSeconds++
                // 通过 BroadcastReceiver 或 StateFlow 更新 UI
            }
        }, 0, 1000)
    }
}
```

### 3.5 铃声与振动

```kotlin
class RingtoneHelper(private val context: Context) {
    private var mediaPlayer: MediaPlayer? = null
    private var vibrator: Vibrator? = null

    fun startRinging(ringtoneUri: Uri?) {
        val uri = ringtoneUri ?: android.media.RingtoneManager.getDefaultUri(
            android.media.RingtoneManager.TYPE_RINGTONE
        )
        mediaPlayer = MediaPlayer().apply {
            setDataSource(context, uri)
            isLooping = true
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
                    .build()
            )
            prepare()
            start()
        }
        startVibration()
    }

    fun stopRinging() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
        vibrator?.cancel()
    }

    private fun startVibration() {
        val pattern = longArrayOf(0, 1000, 1000) // 震动 1s，停 1s，循环
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vm = context.getSystemService(VibratorManager::class.java)
            vm.defaultVibrator.vibrate(VibrationEffect.createWaveform(pattern, 0))
        } else {
            @Suppress("DEPRECATION")
            vibrator = context.getSystemService(Vibrator::class.java)
            vibrator?.vibrate(VibrationEffect.createWaveform(pattern, 0))
        }
    }
}
```

### 3.6 数据持久化

存储分两层：**Room**（联系人）+ **DataStore**（App 设置）

---

#### Room — 联系人存储

**Entity（表结构）**

```kotlin
// data/db/entity/CallerEntity.kt
@Entity(tableName = "callers")
data class CallerEntity(
    @PrimaryKey val id: String,           // UUID
    val name: String,                     // 必填
    val number: String,                   // 必填
    val avatarUri: String,                // 可选，空字符串 = 无头像
    val isDefault: Boolean = false,       // 是否为当前选中的联系人
    val createdAt: Long = System.currentTimeMillis()  // 创建时间，方便排序
)
```

**DAO（数据访问）**

```kotlin
// data/db/dao/CallerDao.kt
@Dao
interface CallerDao {

    // 查询所有联系人，按创建时间倒序
    @Query("SELECT * FROM callers ORDER BY createdAt DESC")
    fun getAll(): Flow<List<CallerEntity>>

    // 查询当前默认联系人
    @Query("SELECT * FROM callers WHERE isDefault = 1 LIMIT 1")
    fun getDefault(): Flow<CallerEntity?>

    // 插入或更新（冲突时替换）
    @Upsert
    suspend fun upsert(caller: CallerEntity)

    // 删除单个联系人
    @Delete
    suspend fun delete(caller: CallerEntity)

    // 把所有联系人设为非默认
    @Query("UPDATE callers SET isDefault = 0")
    suspend fun clearDefault()

    // 设置某个联系人为默认
    @Query("UPDATE callers SET isDefault = 1 WHERE id = :id")
    suspend fun setDefault(id: String)
}
```

**Database**

```kotlin
// data/db/AppDatabase.kt
@Database(
    entities = [CallerEntity::class],
    version = 1,
    exportSchema = true   // 导出 schema，方便后续迁移
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun callerDao(): CallerDao
}
```

**Hilt 注入**

```kotlin
// system/di/DatabaseModule.kt
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "fakecall.db")
            .fallbackToDestructiveMigration()  // 开发阶段，正式上线前改为 Migration
            .build()

    @Provides
    fun provideCallerDao(db: AppDatabase): CallerDao = db.callerDao()
}
```

---

#### DataStore — App 设置存储

只存与联系人无关的全局配置：

```kotlin
// data/datastore/SettingsDataStore.kt
object SettingsKeys {
    val DEFAULT_DELAY_MS  = longPreferencesKey("default_delay_ms")   // 默认延迟
    val RINGTONE_URI      = stringPreferencesKey("ringtone_uri")      // 铃声
    val FAKE_AUDIO_ON     = booleanPreferencesKey("fake_audio_on")    // 模拟音频开关
}
```

---

#### 存储职责分工

| 数据 | 存储方案 | 原因 |
|------|---------|------|
| 联系人列表 | **Room** | 有增删改查需求，后续可加搜索、通话记录关联 |
| 当前默认联系人 | **Room**（isDefault 字段） | 和联系人数据在一起，保持一致性 |
| 延迟时间、铃声、设置 | **DataStore** | 简单 key-value，无查询需求 |
| 随机来电的联系人 | **不存储** | 内存中生成，用完即丢 |

---

## 4. 界面路由

```
MainActivity (Compose NavHost)
  ├── HomeScreen          — 主界面（随机/自定义来电 + 定时触发）
  └── ContactsScreen      — 联系人列表（My Contacts）
        └── AddContactScreen — 新增/编辑联系人

IncomingCallActivity      — 独立 Activity（锁屏来电，不走 NavHost）
  └── IncomingCallScreen  — Compose

InCallActivity            — 独立 Activity（通话中）
  └── InCallScreen        — Compose

CallEndedActivity         — 独立 Activity（通话结束）
  └── CallEndedScreen     — Compose
```

> **为何来电相关界面用独立 Activity？**
> Full-Screen Intent 必须启动 Activity，且需要独立设置 window flags（亮屏/锁屏显示），无法复用 NavHost 的 Activity。

---

## 5. 关键数据流

```
用户点击"立即触发"
  → HomeViewModel.triggerImmediately()
  → ScheduleFakeCallUseCase(delay = 1500ms)
  → AlarmRepository.schedule(triggerTime)
  → AlarmManager.setExactAndAllowWhileIdle()
  → [1.5s 后] FakeCallReceiver.onReceive()
  → NotificationHelper.showIncomingCallNotification()
  → IncomingCallActivity 弹出（锁屏 / 前台均生效）

用户接听
  → IncomingCallActivity finish
  → 启动 InCallActivity + FakeCallService
  → FakeCallService 维护计时，通过 StateFlow 推送给 InCallViewModel

用户挂断
  → InCallViewModel.hangUp()
  → 停止 FakeCallService
  → 跳转 CallEndedActivity
  → 2s 后 finish → 返回 MainActivity
```

---

## 6. AndroidManifest 关键配置

```xml
<!-- 权限 -->
<uses-permission android:name="android.permission.VIBRATE" />
<uses-permission android:name="android.permission.USE_FULL_SCREEN_INTENT" />
<uses-permission android:name="android.permission.WAKE_LOCK" />
<uses-permission android:name="android.permission.POST_NOTIFICATIONS" />
<uses-permission android:name="android.permission.SCHEDULE_EXACT_ALARM" />
<uses-permission android:name="android.permission.FOREGROUND_SERVICE" />
<uses-permission android:name="android.permission.FOREGROUND_SERVICE_PHONE_CALL" />
<uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED" />

<!-- 来电 Activity：锁屏显示 -->
<activity
    android:name=".ui.call.IncomingCallActivity"
    android:exported="false"
    android:showOnLockScreen="true"
    android:turnScreenOn="true"
    android:excludeFromRecents="true"
    android:launchMode="singleTask" />

<!-- 通话中 Activity -->
<activity
    android:name=".ui.call.InCallActivity"
    android:exported="false"
    android:excludeFromRecents="true"
    android:launchMode="singleTask" />

<!-- 通话结束 Activity -->
<activity
    android:name=".ui.call.CallEndedActivity"
    android:exported="false"
    android:excludeFromRecents="true" />

<!-- 定时触发 Receiver -->
<receiver
    android:name=".system.FakeCallReceiver"
    android:exported="false" />

<!-- 开机恢复定时任务 -->
<receiver
    android:name=".system.BootReceiver"
    android:exported="true">
    <intent-filter>
        <action android:name="android.intent.action.BOOT_COMPLETED" />
    </intent-filter>
</receiver>

<!-- 通话中 Foreground Service -->
<service
    android:name=".system.FakeCallService"
    android:exported="false"
    android:foregroundServiceType="phoneCall" />
```

---

## 7. 依赖 (build.gradle.kts)

```kotlin
dependencies {
    // Compose BOM
    implementation(platform("androidx.compose:compose-bom:2024.02.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.activity:activity-compose:1.8.2")

    // Navigation
    implementation("androidx.navigation:navigation-compose:2.7.6")

    // Lifecycle / ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.7.0")

    // Hilt
    implementation("com.google.dagger:hilt-android:2.59.2")
    ksp("com.google.dagger:hilt-android-compiler:2.59.2")
    implementation("androidx.hilt:hilt-navigation-compose:1.1.0")

    // Room
    implementation("androidx.room:room-runtime:2.8.4")
    implementation("androidx.room:room-ktx:2.8.4")       // Flow / suspend 支持
    ksp("androidx.room:room-compiler:2.8.4")

    // DataStore（仅用于 App 设置）
    implementation("androidx.datastore:datastore-preferences:1.0.0")

    // Coil (图片加载，头像)
    implementation("io.coil-kt:coil-compose:2.5.0")

    // Material Icons Extended
    implementation("androidx.compose.material:material-icons-extended")

    // Lifecycle Service（FakeCallService 继承 LifecycleService）
    implementation("androidx.lifecycle:lifecycle-service:2.7.0")

    // Material3 XML 主题
    implementation("com.google.android.material:material:1.12.0")

    // Core
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
}
```

---

## 8. 注意事项

| 问题 | 方案 |
|------|------|
| Android 12+ 精确闹钟需要用户授权 | 引导用户在"闹钟和提醒"中授权 `SCHEDULE_EXACT_ALARM` |
| Android 13+ 通知权限 | 首次启动时请求 `POST_NOTIFICATIONS` |
| Android 14 Full-Screen Intent 受限 | 需要引导用户在设置中开启（或使用通话类通知绕过） |
| 部分厂商 ROM 限制后台弹窗 | 提示用户在电池优化中添加白名单 |
| 前台 Service 类型 | 使用 `phoneCall` 类型，需声明 `FOREGROUND_SERVICE_PHONE_CALL` |
