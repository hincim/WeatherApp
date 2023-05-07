package com.hakaninc.weather_app.model

import com.google.gson.annotations.SerializedName


data class Rain (

  @SerializedName("1h" ) var h : Double? = null

)