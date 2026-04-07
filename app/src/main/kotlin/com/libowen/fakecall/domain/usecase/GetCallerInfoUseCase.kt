package com.libowen.fakecall.domain.usecase

import com.libowen.fakecall.domain.model.CallerInfo
import com.libowen.fakecall.domain.repository.CallerRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCallerInfoUseCase @Inject constructor(
    private val repository: CallerRepository
) {
    operator fun invoke(): Flow<CallerInfo?> = repository.getDefault()
}
