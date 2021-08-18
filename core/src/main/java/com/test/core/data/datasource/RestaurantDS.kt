package com.test.core.data.datasource

import com.test.core.domain.Restaurant
import com.test.core.domain.User

interface RestaurantDS {
    suspend fun addRestaurant(user: User, restaurant: Restaurant): Result<Restaurant>
    suspend fun getRestaurant(user: User, restaurant: Restaurant): Result<Restaurant>
    suspend fun getUserRestaurants(user: User, page: Int): Result<List<Restaurant>>
    suspend fun deleteRestaurant(user: User, restaurant: Restaurant): Result<Boolean>
    suspend fun updateRestaurant(user: User, restaurant: Restaurant): Result<Boolean>
}