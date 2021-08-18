package com.test.topapp.framework.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey
    val id: Int,
    val email: String,
    val role: Int,
    val firstName: String?,
    val lastName: String?,
    val token: String,
    val selected: Boolean
)