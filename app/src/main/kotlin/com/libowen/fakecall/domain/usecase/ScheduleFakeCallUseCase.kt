package com.libowen.fakecall.domain.usecase

import com.libowen.fakecall.domain.model.CallerInfo
import com.libowen.fakecall.system.AlarmHelper
import javax.inject.Inject

class ScheduleFakeCallUseCase @Inject constructor(
    private val alarmHelper: AlarmHelper
) {
    operator fun invoke(delayMs: Long, caller: CallerInfo) {
        alarmHelper.schedule(delayMs, caller)
    }
}
