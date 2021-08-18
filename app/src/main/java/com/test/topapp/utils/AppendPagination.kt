package com.test.topapp.utils

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first

class AppendPagination<in In, Res>(
    private val service: suspend (Pair<In, Int>) -> Result<List<Res>>,
    private val perPage: Int = 10,
    private val errorGate: suspend (Throwable) -> Unit
) {
    private var page: Int = 0
    private var firstPair: In? = null
    private var firstPairSet = false
    private var pageEnd = false
    private val _list = MutableStateFlow(listOf<Res>())

    val list: StateFlow<List<Res>> = _list

    fun setFirstPair(input: In): AppendPagination<In, Res> {
        firstPair = input
        firstPairSet = true
        return this
    }

    suspend fun refresh() {
        page = 0
        _list.emit(listOf())
        fetch()
    }

    suspend fun fetch() {
        if (pageEnd || !firstPairSet) return
        service(firstPair!! to page + 1)
            .onSuccess {
                _list.emit(_list.first() + it)
                if (it.size != perPage) pageEnd = true else page++
            }
            .onFailure { errorGate(it) }
    }
}