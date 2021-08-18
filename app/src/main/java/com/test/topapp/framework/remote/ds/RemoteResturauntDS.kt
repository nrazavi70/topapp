package com.test.topapp.framework.remote.ds

import com.test.core.data.datasource.RestaurantDS
import com.test.core.domain.Restaurant
import com.test.core.domain.User
import com.test.topapp.framework.remote.api.RestaurantService
import com.test.topapp.framework.remote.response.converter.toDomain
import com.test.topapp.framework.remote.util.handleError

class RemoteResturauntDS(private val restaurantService: RestaurantService) : RestaurantDS {
    override suspend fun addRestaurant(user: User, restaurant: Restaurant) = try {
        Result.success(
            restaurantService.create(
                user.id,
                user.token,
                restaurant.name,
                restaurant.locLAT,
                restaurant.locLNG,
                restaurant.address
            ).toDomain()
        )
    } catch (e: Throwable) {
        Result.failure(handleError(e))
    }

    override suspend fun getRestaurant(user: User, restaurant: Restaurant) = try {
        Result.success(
            restaurantService.getRestaurant(
                user.id,
                user.token,
                restaurant.id
            ).toDomain()
        )
    } catch (e: Throwable) {
        Result.failure(handleError(e))
    }

    override suspend fun getUserRestaurants(user: User, page: Int) = try {
        Result.success(
            restaurantService.getUserRestaurants(user.id, user.token, page).map {
                it.toDomain()
            })
    } catch (e: Throwable) {
        Result.failure(handleError(e))
    }

    override suspend fun updateRestaurant(user: User, restaurant: Restaurant) = try {
        restaurantService.update(
            user.id,
            user.token,
            restaurant.id,
            restaurant.name,
            restaurant.locLAT,
            restaurant.locLNG,
            restaurant.address
        )
        Result.success(true)
    } catch (e: Throwable) {
        Result.failure(handleError(e))
    }

    override suspend fun deleteRestaurant(user: User, restaurant: Restaurant) = try {
        restaurantService.delete(user.id, user.token, restaurant.id)
        Result.success(true)
    } catch (e: Throwable) {
        Result.failure(handleError(e))
    }
}