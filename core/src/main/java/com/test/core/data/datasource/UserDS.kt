package com.test.core.data.datasource

import com.test.core.domain.User

interface UserDS {
    suspend fun addUser(user: User): Result<User>
    suspend fun validUser(user: User): Result<User>
    suspend fun getUser(admin: User, user: User): Result<User>
    suspend fun getAll(user: User, page: Int): Result<List<User>>
    suspend fun removeUser(admin: User, user: User): Result<User>
    suspend fun updateUser(admin: User, user: User): Result<User>
}