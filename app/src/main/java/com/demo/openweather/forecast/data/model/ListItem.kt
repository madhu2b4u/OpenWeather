package com.demo.openweather.forecast.data.model

abstract class ListItem {
    abstract fun getType(): ItemType
}

enum class ItemType{
    TYPE_DATE, TYPE_GENERAL
}