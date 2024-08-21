package com.example.weathernow.api.service

import com.example.weathernow.AppConstants.WEATHER_END_POINT
import com.example.weathernow.data.model.response.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {
    @GET(WEATHER_END_POINT)
    suspend fun getCurrentWeather(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("appid") apiKey: String,
    ): WeatherResponse
}