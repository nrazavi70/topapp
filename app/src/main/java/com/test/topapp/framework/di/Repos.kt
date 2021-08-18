package com.test.topapp.framework.di

import com.test.core.data.repositories.RestaurantRepo
import com.test.core.data.repositories.ReviewRepo
import com.test.core.data.repositories.UserRepo
import com.test.topapp.framework.db.ds.LocalUserDS
import com.test.topapp.framework.remote.ds.RemoteResturauntDS
import com.test.topapp.framework.remote.ds.RemoteReviewDS
import com.test.topapp.framework.remote.ds.RemoteUserDS
import org.koin.dsl.module

val repos = module {
    single { LocalUserDS(get()) }
    single { RemoteUserDS(get()) }
    single { UserRepo(get<LocalUserDS>(), get<RemoteUserDS>()) }

    single { RemoteResturauntDS(get()) }
    single { RestaurantRepo(get<RemoteResturauntDS>()) }

    single { RemoteReviewDS(get()) }
    single { ReviewRepo(get<RemoteReviewDS>()) }
}