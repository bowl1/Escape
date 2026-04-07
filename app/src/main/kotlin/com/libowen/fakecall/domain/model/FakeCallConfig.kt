package com.libowen.fakecall.domain.model

data class FakeCallConfig(
    val delayMs: Long,
    val caller: CallerInfo
)
