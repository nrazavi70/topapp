package com.test.core.data.repositories

import com.test.core.data.datasource.UserDS
import com.test.core.domain.User

class UserRepo(private val userLocalDS: UserDS, private val userRemoteDS: UserDS) {
    suspend fun login(user: User): Result<User> {
        userLocalDS.getUser(User(-1, ""), user).onSuccess {
            return Result.failure(Exception("You are before login with this account"))
        }
        userRemoteDS.validUser(user).onSuccess {
            userLocalDS.addUser(it)
            return Result.success(it)
        }.onFailure {
            return Result.failure(it)
        }
        return Result.failure(Exception("Login failed"))
    }

    suspend fun register(user: User): Result<User> {
        userRemoteDS.addUser(user).onSuccess {
            userLocalDS.addUser(it)
            return Result.success(it)
        }.onFailure {
            return Result.failure(it)
        }
        return Result.failure(Exception("Register failed"))
    }

    suspend fun getLocalUsers() = userLocalDS.getAll(User(-1, ""), -1)

    suspend fun getRemoteUser(admin: User, user: User) = userRemoteDS.getUser(admin, user)

    suspend fun getRemoteUsers(user: User, page: Int) = userRemoteDS.getAll(user, page)

    suspend fun getUser(user: User) = userLocalDS.getUser(User(-1, ""), user)

    suspend fun updateUser(admin: User, user: User): Result<User> {
        userRemoteDS.updateUser(admin, user).onSuccess {
            userLocalDS.updateUser(admin, user)
            return Result.success(it)
        }.onFailure {
            return Result.failure(it)
        }
        return Result.failure(Exception("Update user failed"))
    }

    suspend fun removeUser(admin: User, user: User): Result<User> {
        userRemoteDS.removeUser(admin, user).onSuccess {
            userLocalDS.removeUser(admin, user)
            return Result.success(it)
        }.onFailure {
            return Result.failure(it)
        }
        return Result.failure(Exception("Update user failed"))
    }
}