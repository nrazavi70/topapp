package com.test.topapp.framework.di

import com.test.topapp.presentation.main.MainActivityViewModel
import com.test.topapp.presentation.restaurant.manage.RestaurantManageActivityViewModel
import com.test.topapp.presentation.restaurant.page.RestaurantPageActivityViewModel
import com.test.topapp.presentation.review.manage.ReviewManageActivityViewModel
import com.test.topapp.presentation.review.response.ReviewResponseActivityViewModel
import com.test.topapp.presentation.user.admin.UserAdminActivityViewModel
import com.test.topapp.presentation.user.login.LoginActivityViewModel
import com.test.topapp.presentation.user.owner.UserOwnerActivityViewModel
import com.test.topapp.presentation.user.register.RegisterActivityViewModel
import com.test.topapp.presentation.user.regular.UserRegularActivityViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { MainActivityViewModel(get()) }

    viewModel { LoginActivityViewModel(get()) }
    viewModel { RegisterActivityViewModel(get(), get(), get(), get(), get()) }

    viewModel { UserAdminActivityViewModel(get(), get(), get()) }
    viewModel { UserOwnerActivityViewModel(get(), get(), get()) }
    viewModel { UserRegularActivityViewModel(get(), get()) }

    viewModel { RestaurantManageActivityViewModel(get(), get(), get(), get(), get()) }
    viewModel { RestaurantPageActivityViewModel(get(), get(), get()) }

    viewModel { ReviewManageActivityViewModel(get(), get(), get(), get(), get()) }
    viewModel { ReviewResponseActivityViewModel(get(), get(), get()) }
}