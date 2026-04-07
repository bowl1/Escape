package com.libowen.fakecall.domain.usecase

import com.libowen.fakecall.domain.model.CallerInfo
import com.libowen.fakecall.domain.repository.CallerRepository
import javax.inject.Inject

class SaveCallerPresetUseCase @Inject constructor(
    private val repository: CallerRepository
) {
    suspend operator fun invoke(caller: CallerInfo) {
        repository.save(caller)
    }
}
