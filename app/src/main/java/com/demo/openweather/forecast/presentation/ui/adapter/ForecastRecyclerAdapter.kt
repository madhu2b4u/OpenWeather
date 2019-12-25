package com.demo.openweather.forecast.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.demo.openweather.R
import com.demo.openweather.forecast.data.model.DateItem
import com.demo.openweather.forecast.data.model.Forecast
import com.demo.openweather.forecast.data.model.ItemType
import com.demo.openweather.forecast.data.model.ListItem
import kotlinx.android.synthetic.main.forecast_recycler_item.view.*
import kotlinx.android.synthetic.main.row_date.view.*
import java.math.RoundingMode
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

private const val TYPE_DATE = 0
private const val TYPE_GENERAL = 1

class ForecastRecyclerAdapter @Inject constructor() : RecyclerView.Adapter<ForcastRecyclerViewHolder>(){

    private val forecastList = mutableListOf<ListItem>()

    override fun onBindViewHolder(holder: ForcastRecyclerViewHolder, position: Int) {
        holder.bind()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForcastRecyclerViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            TYPE_DATE -> DateViewHolder(inflater.inflate(R.layout.row_date,parent,false))
            TYPE_GENERAL -> ArticleViewHolder(inflater.inflate(R.layout.forecast_recycler_item,parent,false))
            else -> throw Exception("Not Supported View Type")
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = forecastList[position]
        return when (item.getType()) {
            ItemType.TYPE_DATE -> TYPE_DATE
            ItemType.TYPE_GENERAL -> TYPE_GENERAL
        }
    }

    inner class DateViewHolder(view: View) : ForcastRecyclerViewHolder(view) {
        override fun bind() {
            val item = forecastList[adapterPosition] as DateItem
            with(itemView) {
                tvDate.text = item.date
            }
        }

    }

    fun setForecast(matchesList: List<ListItem>) {
        val diffResult = DiffUtil.calculateDiff(ForecastDiffUtilCallback(this.forecastList, matchesList))
        this.forecastList.clear()
        this.forecastList.addAll(matchesList)
        diffResult.dispatchUpdatesTo(this)
    }


    override fun getItemCount() = forecastList.size

    inner class ArticleViewHolder constructor(itemView: View) : ForcastRecyclerViewHolder(itemView) {

        override fun bind() {
            val weatherResponse = forecastList[adapterPosition] as Forecast

            with(itemView) {
                    val desc = weatherResponse.weather[0].description
                    val icon = weatherResponse.weather[0].icon
                    val main = weatherResponse.weather[0].main
                    val windSpeed = context.getString(R.string.wind_speed ,weatherResponse.wind.speed.toString())
                    val minTemp = convertKelvinToCelsius(weatherResponse.main.temp_min)
                    val maxTemp = convertKelvinToCelsius(weatherResponse.main.temp_max)
                    val actualTemp = convertKelvinToCelsius(weatherResponse.main.temp)
                    val time = weatherResponse.dt_txt.split(" ")[1]

                    image.setImageURI(fetchImage(icon))
                    tvMain.text = main
                    tvDescription.text = desc
                    tvTime.text =getTime(time)
                    tvWindSpeed.text = windSpeed
                    tvMin.text = context.getString(R.string.min_max_temperature ,minTemp)
                    tvMax.text = context.getString(R.string.min_max_temperature, maxTemp)
                    tvTemperature.text = actualTemp


            }
        }

        private fun fetchImage(icon : String): String {
            return "http://openweathermap.org/img/wn/$icon@2x.png"
        }

        private fun convertKelvinToCelsius(kevinValue :Double): String {
            return (kevinValue - 273.15).toBigDecimal().setScale(1, RoundingMode.UP).toDouble().toString()+ "°C"
        }

        private fun getTime(time :String): String {
            val sdf = SimpleDateFormat("HH:mm:ss", Locale.ENGLISH)
            val dateObj: Date = sdf.parse(time)
            return SimpleDateFormat("hh:mm aa", Locale.ENGLISH).format(dateObj)
        }

    }



}