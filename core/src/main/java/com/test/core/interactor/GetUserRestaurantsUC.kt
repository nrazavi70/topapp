package com.test.core.interactor

import com.test.core.data.repositories.RestaurantRepo
import com.test.core.domain.Restaurant
import com.test.core.domain.User
import com.test.core.interactor.base.BaseUseCase

class GetUserRestaurantsUC(restaurantRepo: RestaurantRepo) :
    BaseUseCase<Pair<User, Int>, List<Restaurant>, RestaurantRepo>(restaurantRepo) {
    override suspend fun execute(parameter: Pair<User, Int>) =
        repo.getUserRestaurants(parameter.first, parameter.second)
}