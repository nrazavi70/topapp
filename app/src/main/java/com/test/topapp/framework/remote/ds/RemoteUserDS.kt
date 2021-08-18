package com.test.topapp.framework.remote.ds

import com.test.core.data.datasource.UserDS
import com.test.core.domain.User
import com.test.topapp.framework.remote.api.UserService
import com.test.topapp.framework.remote.response.converter.toDomain
import com.test.topapp.framework.remote.util.handleError

class RemoteUserDS(private val userService: UserService) : UserDS {
    // register
    override suspend fun addUser(user: User) = try {
        Result.success(
            userService.register(
                user.email,
                user.password,
                user.role,
                user.firstName,
                user.lastName
            ).toDomain()
        )
    } catch (e: Throwable) {
        Result.failure(handleError(e))
    }

    // login
    override suspend fun validUser(user: User) = try {
        Result.success(userService.login(user.email, user.password).toDomain())
    } catch (e: Throwable) {
        Result.failure(handleError(e))
    }

    override suspend fun getUser(admin: User, user: User): Result<User> = try {
        Result.success(userService.getUser(admin.id, admin.token, user.id).toDomain())
    } catch (e: Throwable) {
        Result.failure(handleError(e))
    }

    override suspend fun getAll(user: User, page: Int) = try {
        Result.success(userService.getUsers(user.id, user.token, page).map { it.toDomain() })
    } catch (e: Throwable) {
        Result.failure(handleError(e))
    }

    override suspend fun removeUser(admin: User, user: User) = try {
        Result.success(userService.deleteUser(admin.id, admin.token, user.id).toDomain())
    } catch (e: Throwable) {
        Result.failure(handleError(e))
    }

    override suspend fun updateUser(admin: User, user: User) = try {
        Result.success(
            userService.updateUser(
                admin.id,
                admin.token,
                user.id,
                user.email,
                user.role,
                user.firstName,
                user.lastName
            ).toDomain()
        )
    } catch (e: Throwable) {
        Result.failure(handleError(e))
    }
}

