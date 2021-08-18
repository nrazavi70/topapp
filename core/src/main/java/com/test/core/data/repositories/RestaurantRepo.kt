package com.test.core.data.repositories

import com.test.core.data.datasource.RestaurantDS
import com.test.core.domain.Restaurant
import com.test.core.domain.User

class RestaurantRepo(private val restaurantRemoteDS: RestaurantDS) {
    suspend fun getUserRestaurants(user: User, page: Int) =
        restaurantRemoteDS.getUserRestaurants(user, page)

    suspend fun getRestaurant(user: User, restaurant: Restaurant) =
        restaurantRemoteDS.getRestaurant(user, restaurant)

    suspend fun addRestaurants(user: User, restaurant: Restaurant) =
        restaurantRemoteDS.addRestaurant(user, restaurant)

    suspend fun deleteRestaurant(user: User, restaurant: Restaurant) =
        restaurantRemoteDS.deleteRestaurant(user, restaurant)

    suspend fun updateRestaurant(user: User, restaurant: Restaurant) =
        restaurantRemoteDS.updateRestaurant(user, restaurant)
}