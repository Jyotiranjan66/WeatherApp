package com.example.weathernow.main.homemodule.repo

import android.util.Log
import com.example.weathernow.api.service.WeatherApiService
import com.example.weathernow.data.model.response.WeatherResponse
import com.example.weathernow.main.base.ApiResult
import com.example.weathernow.main.base.BaseRepo
import javax.inject.Inject

class MainActRepo
@Inject
constructor(private val weatherApiService:WeatherApiService):BaseRepo(){
    suspend fun getCurrentWeather(
        latitude: Double,
        longitude: Double,
        apiKey: String,
        onError: ((ApiResult<Any>) -> Unit)?
    ): WeatherResponse?{
        return with(
            apiCall(executable = {
                weatherApiService.getCurrentWeather(latitude, longitude, apiKey)
            })
        ){
            if(data == null){
                Log.e("Error",error.toString())
                onError?.invoke(ApiResult(null,resultType, error, resCode = resCode))
            }

            data
        }
    }
}