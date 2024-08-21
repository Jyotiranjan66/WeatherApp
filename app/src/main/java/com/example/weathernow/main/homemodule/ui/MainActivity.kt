package com.example.weathernow.main.homemodule.ui

import android.annotation.SuppressLint
import android.content.Context
import android.location.LocationManager
import android.util.Log
import androidx.activity.viewModels
import com.example.weathernow.AppConstants.API_KEY
import com.example.weathernow.Layouts
import com.example.weathernow.data.model.response.WeatherResponse
import com.example.weathernow.databinding.ActivityMainBinding
import com.example.weathernow.helper.permission_handler.LocationPermissionHandler
import com.example.weathernow.main.base.BaseAct
import com.example.weathernow.main.homemodule.viewmodel.MainActVM
import com.example.weathernow.main.state.ApiRenderState
import dagger.hilt.android.AndroidEntryPoint
import com.example.weathernow.BR

@AndroidEntryPoint
class MainActivity : BaseAct<ActivityMainBinding, MainActVM>(Layouts.activity_main) {
    override val vm: MainActVM by viewModels()
    private lateinit var locationPermissionHandler: LocationPermissionHandler
    override val hasProgress: Boolean = true
    override fun init() {
        locationPermissionHandler = LocationPermissionHandler(
            activity = this,
            onPermissionGranted = { getWeatherUpdate() },
        ).apply {
            checkAndRequestPermissions()
        }
    }
    @SuppressLint("MissingPermission") // Permission always Granted
    private fun getWeatherUpdate() {
        val location = (getSystemService(Context.LOCATION_SERVICE) as LocationManager).getLastKnownLocation(LocationManager.GPS_PROVIDER)

        location?.let {
            vm.getCurrentWeather(               // API call for get Weather Details
                latitude = it.latitude,
                longitude = it.longitude,
                apiKey = API_KEY
            )
        }
    }

    override fun renderState(apiRenderState: ApiRenderState) {
        when (apiRenderState) {
            is ApiRenderState.ApiSuccess<*> -> {
                hideProgress()
                successToast("Success")
                if(apiRenderState.result is WeatherResponse){
                    Log.d("TAG",apiRenderState.result.toString())
                    binding.setVariable(BR.weatherRes,apiRenderState.result)
                }
            }
            is ApiRenderState.ValidationError -> {
                hideProgress()
                apiRenderState.message?.let {
                    errorToast(getString(it))
                }
            }
            else -> showProgress()
        }
    }
}