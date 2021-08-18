package com.test.core.interactor

import com.test.core.data.repositories.UserRepo
import com.test.core.domain.User
import com.test.core.interactor.base.BaseUseCase

class UserEditUC(userRepo: UserRepo) : BaseUseCase<Pair<User, User>, User, UserRepo>(userRepo) {
    override suspend fun execute(parameter: Pair<User, User>) =
        repo.updateUser(parameter.first, parameter.second)
}