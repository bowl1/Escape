package com.libowen.fakecall.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.libowen.fakecall.domain.model.CallerInfo
import com.libowen.fakecall.domain.repository.SettingsRepository
import com.libowen.fakecall.domain.usecase.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class CallTab { RANDOM, CUSTOM }
enum class ScheduleState { IDLE, SCHEDULED }

data class HomeUiState(
    val activeTab: CallTab = CallTab.RANDOM,
    val scheduleState: ScheduleState = ScheduleState.IDLE,
    val delayMs: Long = 5 * 60 * 1000L,     // 默认 5 分钟
    val customName: String = "",
    val customNumber: String = "",
    val customAvatarUri: String? = null,
    val selectedContact: CallerInfo? = null,  // 从 My Contacts 选中的联系人
    val defaultCaller: CallerInfo? = null
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val generateRandomCaller: GenerateRandomCallerUseCase,
    private val scheduleFakeCall: ScheduleFakeCallUseCase,
    private val cancelFakeCall: CancelFakeCallUseCase,
    private val getCallerInfo: GetCallerInfoUseCase,
    private val saveCallerPreset: SaveCallerPresetUseCase,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        // 监听默认联系人
        viewModelScope.launch {
            getCallerInfo().collect { caller ->
                _uiState.update { it.copy(defaultCaller = caller) }
            }
        }
        // 观察持久化的 scheduled 状态（alarm 触发后 Receiver 会清除它）
        viewModelScope.launch {
            settingsRepository.getScheduled().collect { scheduled ->
                _uiState.update {
                    it.copy(scheduleState = if (scheduled) ScheduleState.SCHEDULED else ScheduleState.IDLE)
                }
            }
        }
    }

    fun setTab(tab: CallTab) {
        _uiState.update { it.copy(activeTab = tab) }
    }

    fun setDelayMs(ms: Long) {
        _uiState.update { it.copy(delayMs = ms) }
    }

    fun setCustomName(name: String) {
        _uiState.update { it.copy(customName = name, selectedContact = null) }
    }

    fun setCustomNumber(number: String) {
        _uiState.update { it.copy(customNumber = number, selectedContact = null) }
    }

    fun setCustomAvatarUri(uri: String?) {
        _uiState.update { it.copy(customAvatarUri = uri, selectedContact = null) }
    }

    /** 从 My Contacts 选中联系人，跳转回 Custom 标签并填入信息 */
    fun selectContact(caller: CallerInfo) {
        _uiState.update {
            it.copy(
                activeTab = CallTab.CUSTOM,
                selectedContact = caller,
                customName = caller.name,
                customNumber = caller.number,
                customAvatarUri = caller.avatarUri
            )
        }
    }

    /** 立即触发来电（1.5s 自然延迟） */
    fun callNow() {
        val caller = buildCaller(immediate = true)
        scheduleFakeCall(1500L, caller)
    }

    /** 按照设定延迟安排来电 */
    fun scheduleCall() {
        val caller = buildCaller(immediate = false)
        scheduleFakeCall(_uiState.value.delayMs, caller)
        viewModelScope.launch { settingsRepository.setScheduled(true) }
    }

    /** 取消定时来电 */
    fun cancelScheduledCall() {
        cancelFakeCall()
        viewModelScope.launch { settingsRepository.setScheduled(false) }
    }

    /** 将当前自定义联系人保存到 My Contacts */
    fun saveCurrentContact() {
        val state = _uiState.value
        if (state.customName.isBlank()) return
        viewModelScope.launch {
            val caller = CallerInfo(
                id = java.util.UUID.randomUUID().toString(),
                name = state.customName,
                number = state.customNumber
            )
            saveCallerPreset(caller)
        }
    }

    private fun buildCaller(immediate: Boolean): CallerInfo {
        return when (_uiState.value.activeTab) {
            CallTab.RANDOM -> generateRandomCaller()
            CallTab.CUSTOM -> {
                val state = _uiState.value
                CallerInfo(
                    id = if (immediate) "custom_now" else "custom_scheduled",
                    name = state.customName.ifEmpty { "Unknown" },
                    number = state.customNumber,
                    avatarUri = state.customAvatarUri
                )
            }
        }
    }
}
