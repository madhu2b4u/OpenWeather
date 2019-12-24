package com.demo.openweather.weather.data.model

data class WeatherResponse(
    val base: String,
    val clouds: Clouds,
    val cod: Int,
    val cordinates: Cordinates,
    val dt: Int,
    val id: Int,
    val main: Main,
    val name: String,
    val sys: Sys,
    val timezone: Int,
    val visibility: Int,
    val weather: List<Weather>,
    val wind: Wind
)