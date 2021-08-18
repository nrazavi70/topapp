package com.test.core.interactor

import com.test.core.data.repositories.UserRepo
import com.test.core.domain.User
import com.test.core.interactor.base.BaseUseCase

class RegisterUC(userRepo: UserRepo) : BaseUseCase<User, User, UserRepo>(userRepo) {
    override suspend fun execute(parameter: User) = repo.register(parameter)
}