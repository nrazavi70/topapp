package com.test.topapp.framework.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.test.topapp.framework.db.dao.UserDao
import com.test.topapp.framework.db.entity.UserEntity

@Database(
    entities = [UserEntity::class],
    version = 1,
    exportSchema = false
)
abstract class TopAppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}