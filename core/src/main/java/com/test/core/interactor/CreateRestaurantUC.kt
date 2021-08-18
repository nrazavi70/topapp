package com.test.core.interactor

import com.test.core.data.repositories.RestaurantRepo
import com.test.core.domain.Restaurant
import com.test.core.domain.User
import com.test.core.interactor.base.BaseUseCase

class CreateRestaurantUC(restaurantRepo: RestaurantRepo) :
    BaseUseCase<Pair<User, Restaurant>, Restaurant, RestaurantRepo>(restaurantRepo) {
    override suspend fun execute(parameter: Pair<User, Restaurant>) =
        repo.addRestaurants(parameter.first, parameter.second)
}