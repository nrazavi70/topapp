package com.test.core.interactor

import com.test.core.data.repositories.RestaurantRepo
import com.test.core.domain.Restaurant
import com.test.core.domain.User
import com.test.core.interactor.base.BaseUseCase

class RestaurantEditUC(restaurantRepo: RestaurantRepo) :
    BaseUseCase<Pair<User, Restaurant>, Boolean, RestaurantRepo>(restaurantRepo) {
    override suspend fun execute(parameter: Pair<User, Restaurant>) =
        repo.updateRestaurant(parameter.first, parameter.second)
}