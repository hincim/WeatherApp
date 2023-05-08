package com.hakaninc.weather_app.api

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hakaninc.weather_app.model.Info
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherAPI {

    // https://api.openweathermap.org/data/2.5/weather?&appid=7b6ad88d11b66abc57257ca4417fe2a7&units=metric

    @GET("data/2.5/weather?&appid=7b6ad88d11b66abc57257ca4417fe2a7&units=metric")
    fun getWeather(
        @Query("lat") lat: String,
        @Query("lon") lon:String,
    ): Single<Info>


}