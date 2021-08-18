package com.test.topapp.framework.db.dao

import androidx.room.*
import com.test.topapp.framework.db.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addUser(userEntity: UserEntity): Long

    @Query("select * from users")
    suspend fun getAllUsers(): List<UserEntity>

    @Query("select * from users where id = :id")
    suspend fun getUserById(id: Int): UserEntity?

    @Query("select * from users where email = :email")
    suspend fun getUserByEmail(email: String): UserEntity?

    @Delete
    suspend fun removeUser(userEntity: UserEntity)

    @Update
    suspend fun updateUser(userEntity: UserEntity)

    @Query("update users set selected = 0")
    suspend fun deselectAll()

    @Query("update users set selected = 1 where id = :id")
    suspend fun selectProfile(id: Int)
}