package com.demo.openweather.weather.presentation.ui.fragments


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.demo.openweather.R
import com.demo.openweather.common.Status
import com.demo.openweather.common.ViewModelFactory
import com.demo.openweather.weather.data.model.WeatherResponse
import com.demo.openweather.weather.presentation.ui.adapter.WeatherRecyclerviewAdapter
import com.demo.openweather.weather.presentation.viewmodel.WeatherViewModel
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_weather.*
import java.math.RoundingMode
import javax.inject.Inject


class WeatherFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var mWeatherViewModel: WeatherViewModel
    private var lstOfWeatherResponse : ArrayList<WeatherResponse>? = ArrayList()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_weather, container, false)
    }


    override fun onStart() {
        super.onStart()
        lifecycleScope.launchWhenStarted {
            try {
                mWeatherViewModel =
                    ViewModelProviders.of(activity!!, viewModelFactory)
                        .get(WeatherViewModel::class.java)

                listOfCities().forEach {
                    mWeatherViewModel.fetchWeather(it)
                }

                mWeatherViewModel.weatherResult.observe(this@WeatherFragment, Observer {
                    when (it.status) {
                        Status.LOADING -> showLoading()
                        Status.ERROR -> hideLoading()
                        Status.SUCCESS -> {
                            hideLoading()
                            it.data?.let { response ->
                                setResponseToView(response)
                            }
                        }
                    }
                })

            } finally {
                // This line might execute after Lifecycle is DESTROYED.
                if (lifecycle.currentState >= Lifecycle.State.STARTED) {
                    // Here, since we've checked, it is safe to run any
                    // Fragment transactions.

                }
            }
        }
    }

    private fun setResponseToView(response: WeatherResponse) {
        lstOfWeatherResponse?.add(response)
        articlesRecycler.layoutManager = LinearLayoutManager(activity)
        val adapter = WeatherRecyclerviewAdapter()
        lstOfWeatherResponse?.let { adapter.populateWeather(it) }
        articlesRecycler.adapter = adapter


    }

    private fun showLoading() {
        llNoDataLayout.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
        articlesRecycler.visibility = View.GONE
    }

    private fun hideLoading() {
        llNoDataLayout.visibility = View.GONE
        progressBar.visibility = View.GONE
        articlesRecycler.visibility = View.VISIBLE
    }


    private fun listOfCities() = arrayListOf<String>("Hyderabad", "Delhi","Chennai","London","Auckland")


}
