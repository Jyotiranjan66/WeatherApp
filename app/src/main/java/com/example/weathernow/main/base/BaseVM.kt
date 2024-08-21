package com.example.weathernow.main.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weathernow.main.state.ApiRenderState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

abstract class BaseVM : ViewModel() {

    internal val apiError = MutableSharedFlow<ApiResult<Any>>()
    protected val onApiError: (ApiResult<Any>) -> Unit = { error ->
        viewModelScope.launch {
            apiError.emit(error)
        }
    }

    protected val state = MutableSharedFlow<ApiRenderState>()
    internal fun state(): SharedFlow<ApiRenderState> = state
    fun <T> asyncScope(
        dispatcher: CoroutineDispatcher = Dispatchers.IO,
        executable: suspend CoroutineScope.() -> T
    ): Deferred<T> {
        return viewModelScope.async(dispatcher) {
            executable.invoke(this)
        }
    }

    fun <T> scope(
        dispatcher: CoroutineDispatcher = Dispatchers.IO,
        executable: suspend CoroutineScope.() -> T
    ): Job {
        return viewModelScope.launch(dispatcher) {
            executable.invoke(this)
        }
    }
}