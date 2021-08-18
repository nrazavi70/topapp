package com.test.core.interactor

import com.test.core.data.repositories.UserRepo
import com.test.core.domain.User
import com.test.core.interactor.base.BaseUseCase

class GetRemoteUsersUC(userRepo: UserRepo) :
    BaseUseCase<Pair<User, Int>, List<User>, UserRepo>(userRepo) {
    override suspend fun execute(parameter: Pair<User, Int>) =
        repo.getRemoteUsers(parameter.first, parameter.second)
}