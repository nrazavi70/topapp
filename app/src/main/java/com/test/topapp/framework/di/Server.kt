package com.test.topapp.framework.di

import com.test.topapp.framework.remote.api.RestaurantService
import com.test.topapp.framework.remote.api.ReviewService
import com.test.topapp.framework.remote.api.UserService
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val serverModule = module {
    single { GsonConverterFactory.create() }
    single {
        Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080")
            .addConverterFactory(get<GsonConverterFactory>())
            .build()
    }
    single { get<Retrofit>().create(UserService::class.java) }
    single { get<Retrofit>().create(RestaurantService::class.java) }
    single { get<Retrofit>().create(ReviewService::class.java) }
}