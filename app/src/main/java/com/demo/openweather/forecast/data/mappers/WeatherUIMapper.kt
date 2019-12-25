package com.demo.openweather.forecast.data.mappers


import com.demo.openweather.forecast.data.model.Forecast
import com.demo.openweather.forecast.data.model.ListItem
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class WeatherUIMapper @Inject constructor() {

    fun mapToPresentation(forecastList: List<Forecast>): List<ListItem> {
        val listItem = ArrayList<ListItem>()
        val groupedList = groupDataIntoHashMap(forecastList)
        groupedList.keys.forEach { date ->
            val dateItem = date.map()
            listItem.add(dateItem)

            groupedList[date]?.forEach {
                listItem.add(it)
            }
        }
        return listItem
    }

    private fun groupDataIntoHashMap(list: List<Forecast>): Map<Date, List<Forecast>> {
        return list.sortedBy {
            it.dt_txt.split("")[0]
        }.groupBy { list ->
            val calendar = Calendar.getInstance()
            calendar.time = getDate(list.dt_txt)
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)
            calendar.time
        }
    }

    private fun getDate(dt :String): Date? {
        var date : Date? = null
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        try {
             date = format.parse(dt)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return date
    }
}