package com.test.topapp.framework.di

import com.test.core.interactor.*
import org.koin.dsl.module

val useCases = module {
    single { LoginUC(get()) }
    single { RegisterUC(get()) }
    single { UserEditUC(get()) }
    single { UserDeleteUC(get()) }
    single { GetLocalUserUC(get()) }
    single { GetLocalUsersUC(get()) }
    single { GetRemoteUserUC(get()) }
    single { GetRemoteUsersUC(get()) }

    single { GetRestaurantUC(get()) }
    single { RestaurantEditUC(get()) }
    single { RestaurantDeleteUC(get()) }
    single { GetUserRestaurantsUC(get()) }
    single { CreateRestaurantUC(get()) }

    single { CreateReviewUC(get()) }
    single { GetReviewUC(get()) }
    single { ReviewEditUC(get()) }
    single { ReviewDeleteUC(get()) }
    single { GetReviewsUC(get()) }
    single { GetRestaurantReviewsUC(get()) }
    single { GetPendingReviewsUC(get()) }
    single { ResponseReviewUC(get()) }
}