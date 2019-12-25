package com.demo.openweather.forecast.data.model

data class Forecast(
    val clouds: Clouds,
    val dt: Int,
    val dt_txt: String,
    val main: Main,
    val rain: Rain,
    val sys: Sys,
    val weather: List<Weather>,
    val wind: Wind
):ListItem() {
    override fun getType() = ItemType.TYPE_GENERAL
}