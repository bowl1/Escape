package com.libowen.fakecall.data.mapper

import com.libowen.fakecall.data.db.entity.CallerEntity
import com.libowen.fakecall.domain.model.CallerInfo

fun CallerEntity.toDomain() = CallerInfo(
    id = id,
    name = name,
    number = number,
    avatarUri = avatarUri.ifEmpty { null }
)

fun CallerInfo.toEntity(isDefault: Boolean = false) = CallerEntity(
    id = id,
    name = name,
    number = number,
    avatarUri = avatarUri ?: "",
    isDefault = isDefault
)
