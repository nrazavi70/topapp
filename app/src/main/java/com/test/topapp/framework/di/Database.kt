package com.test.topapp.framework.di

import androidx.room.Room
import com.test.topapp.framework.db.TopAppDatabase
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val databaseModule = module {
    single {
        Room.databaseBuilder(
            androidApplication(),
            TopAppDatabase::class.java,
            "app_room.db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }
    single { get<TopAppDatabase>().userDao() }
}