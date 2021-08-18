package com.test.core.interactor

import com.test.core.data.repositories.UserRepo
import com.test.core.domain.User
import com.test.core.interactor.base.BaseUseCase
import kotlinx.coroutines.flow.Flow

class GetLocalUsersUC(userRepo: UserRepo) : BaseUseCase<Unit, List<User>, UserRepo>(userRepo) {
    override suspend fun execute(parameter: Unit) = repo.getLocalUsers()
}