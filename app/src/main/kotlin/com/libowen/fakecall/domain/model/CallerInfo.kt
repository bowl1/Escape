package com.libowen.fakecall.domain.model

data class CallerInfo(
    val id: String,
    val name: String,
    val number: String,
    val avatarUri: String? = null
)
