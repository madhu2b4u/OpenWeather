package com.demo.openweather.forecast.presentation.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.demo.openweather.R
import com.demo.openweather.forecast.data.model.Forecast
import kotlinx.android.synthetic.main.forecast_recycler_item.view.*
import java.math.RoundingMode
import javax.inject.Inject
import java.text.SimpleDateFormat


class ForecastRecyclerviewAdapter @Inject constructor() : RecyclerView.Adapter<ForecastRecyclerviewAdapter.ArticleViewHolder>() {

    private val lstOfWeatherResponse: MutableList<Forecast> = ArrayList()

    fun populateWeather(articlesList: List<Forecast>) {
        lstOfWeatherResponse.clear()
        lstOfWeatherResponse.addAll(articlesList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ArticleViewHolder(
            inflater.inflate(R.layout.forecast_recycler_item, parent, false)
        )
    }

    override fun getItemCount() = lstOfWeatherResponse.size

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) = holder.bind(lstOfWeatherResponse[position])

    inner class ArticleViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(weatherResponse : Forecast) {
            with(itemView) {
                    val desc = weatherResponse.weather[0].description
                    val icon = weatherResponse.weather[0].icon
                    val main = weatherResponse.weather[0].main
                    val windSpeed = weatherResponse.wind.speed.toString() + " MPH"
                    val minTemp = convertKelvinToCelsius(weatherResponse.main.temp_min)
                    val maxTemp = convertKelvinToCelsius(weatherResponse.main.temp_max)
                    val actualTemp = convertKelvinToCelsius(weatherResponse.main.temp)

                    image.setImageURI(fetchImage(icon))
                    tvMain.text = main
                    tvDescription.text = desc
                    tvWindSpeed.text = windSpeed
                    tvMin.text = "Min : $minTemp"
                    tvMax.text = "Max : $maxTemp"
                    tvTemperature.text = actualTemp

            }
        }


        private fun fetchImage(icon : String): String {
            return "http://openweathermap.org/img/wn/$icon@2x.png"
        }

        private fun convertKelvinToCelsius(kevinValue :Double): String {
            return (kevinValue - 273.15).toBigDecimal().setScale(1, RoundingMode.UP).toDouble().toString()+ "°C"
        }
    }



}