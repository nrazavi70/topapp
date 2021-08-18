package com.test.topapp.framework.db.ds

import com.test.core.data.datasource.UserDS
import com.test.core.domain.User
import com.test.topapp.framework.db.dao.UserDao
import com.test.topapp.framework.db.entity.contverter.toDomain
import com.test.topapp.framework.db.entity.contverter.toEntity

class LocalUserDS(private val userDao: UserDao) : UserDS {
    override suspend fun addUser(user: User) =
        Result.success(userDao.getUserById(userDao.addUser(user.toEntity()).toInt())!!.toDomain())

    override suspend fun getUser(admin: User, user: User): Result<User> {
        if (user.email.isNotEmpty()) userDao.getUserByEmail(user.email)?.let {
            return Result.success(it.toDomain())
        }
        if (user.id > 0) userDao.getUserById(user.id)?.let {
            return Result.success(it.toDomain())
        }
        return Result.failure(Exception("User not found"))
    }

    override suspend fun validUser(user: User) = getUser(User(-1, ""), user)

    override suspend fun getAll(user: User, page: Int) =
        Result.success(userDao.getAllUsers().map { it.toDomain() })

    override suspend fun removeUser(admin: User, user: User) = userDao.removeUser(user.toEntity())
        .run { return@run Result.success(user) }

    override suspend fun updateUser(admin:User, user: User) = userDao.updateUser(user.toEntity())
        .run { return@run Result.success(user) }
}