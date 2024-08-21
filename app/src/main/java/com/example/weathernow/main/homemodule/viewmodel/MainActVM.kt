package com.example.weathernow.main.homemodule.viewmodel

import com.example.weathernow.main.base.BaseVM
import com.example.weathernow.main.homemodule.repo.MainActRepo
import com.example.weathernow.main.state.ApiRenderState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
@HiltViewModel
class MainActVM
@Inject
constructor(private val repo: MainActRepo) : BaseVM() {

    fun getCurrentWeather(
        latitude: Double,
        longitude: Double,
        apiKey: String){
        scope {
            state.emit(ApiRenderState.Loading)

            repo.getCurrentWeather(latitude,longitude,apiKey,onApiError)?.let {
                state.emit(ApiRenderState.ApiSuccess(it))
                return@scope
            }
        }
    }

}