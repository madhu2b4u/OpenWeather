package com.demo.openweather.weather.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.demo.openweather.R
import com.demo.openweather.weather.data.model.WeatherResponse
import kotlinx.android.synthetic.main.weather_recycler_item.view.*
import java.math.RoundingMode
import javax.inject.Inject

class WeatherRecyclerviewAdapter @Inject constructor() : RecyclerView.Adapter<WeatherRecyclerviewAdapter.ArticleViewHolder>() {

    private val lstOfWeatherResponse: MutableList<WeatherResponse> = ArrayList()

    fun populateWeather(articlesList: List<WeatherResponse>) {
        lstOfWeatherResponse.clear()
        lstOfWeatherResponse.addAll(articlesList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ArticleViewHolder(
            inflater.inflate(R.layout.weather_recycler_item, parent, false)
        )
    }

    override fun getItemCount() = lstOfWeatherResponse.size

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) = holder.bind(lstOfWeatherResponse[position])

    inner class ArticleViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(weatherResponse : WeatherResponse) {
            with(itemView) {
                    val city = weatherResponse.name+", "+ weatherResponse.sys.country
                    val desc = weatherResponse.weather[0].description
                    val icon = weatherResponse.weather[0].icon
                    val main = weatherResponse.weather[0].main
                    val windSpeed = context.getString(R.string.wind_speed ,weatherResponse.wind.speed.toString())
                    val minTemp = convertKelvinToCelsius(weatherResponse.main.temp_min)
                    val maxTemp = convertKelvinToCelsius(weatherResponse.main.temp_max)
                    val actualTemp = convertKelvinToCelsius(weatherResponse.main.temp)

                    image.setImageURI(fetchImage(icon))
                    tvCity.text = city
                    tvMain.text = main
                    tvDescription.text = desc
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
    }



}