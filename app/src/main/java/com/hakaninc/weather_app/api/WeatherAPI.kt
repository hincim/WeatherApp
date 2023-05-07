package com.hakaninc.weather_app.api

import com.hakaninc.weather_app.model.Info
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET

interface WeatherAPI {

    // https://api.openweathermap.org/data/2.5/weather?lat=37.950242&lon=32.506330&appid=7b6ad88d11b66abc57257ca4417fe2a7&unit=metric
    @GET("data/2.5/weather?lat=37.950242&lon=32.506330&appid=7b6ad88d11b66abc57257ca4417fe2a7&unit=metric")
    fun getWeather(): Single<Info>
}