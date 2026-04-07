package com.libowen.fakecall.domain.usecase

import com.libowen.fakecall.system.AlarmHelper
import javax.inject.Inject

class CancelFakeCallUseCase @Inject constructor(
    private val alarmHelper: AlarmHelper
) {
    operator fun invoke() {
        alarmHelper.cancel()
    }
}
