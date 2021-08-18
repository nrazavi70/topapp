package com.test.topapp.framework.db.entity.contverter

import com.test.core.domain.User
import com.test.topapp.framework.db.entity.UserEntity

fun User.toEntity(): UserEntity = UserEntity(
    this.id,
    this.email,
    this.role,
    this.firstName,
    this.lastName,
    this.token,
    false
)

fun UserEntity.toDomain(): User = User(
    this.id,
    this.email,
    this.role,
    this.firstName,
    this.lastName,
    this.token
)