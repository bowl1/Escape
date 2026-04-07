package com.libowen.fakecall.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "callers")
data class CallerEntity(
    @PrimaryKey val id: String,           // UUID
    val name: String,                     // 必填
    val number: String,                   // 必填
    val avatarUri: String = "",           // 可选，空字符串 = 无头像
    val isDefault: Boolean = false,       // 是否为当前默认联系人
    val createdAt: Long = System.currentTimeMillis()
)
