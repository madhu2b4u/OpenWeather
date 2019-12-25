package com.demo.openweather.forecast.presentation.ui.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.demo.openweather.R
import com.demo.openweather.common.GpsProvider
import com.demo.openweather.common.Status
import com.demo.openweather.common.ViewModelFactory
import com.demo.openweather.forecast.data.mappers.WeatherUIMapper
import com.demo.openweather.forecast.data.model.ForecastResponse
import com.demo.openweather.forecast.presentation.ui.adapter.ForecastRecyclerAdapter
import com.demo.openweather.forecast.presentation.viewmodel.ForecastViewModel
import com.google.android.gms.location.LocationServices
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_forecast.*
import javax.inject.Inject



class ForecastFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory


    private lateinit var mForecastViewModel: ForecastViewModel

    @Inject
    lateinit var mapper : WeatherUIMapper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_forecast, container, false)
    }


    override fun onStart() {
        super.onStart()
        lifecycleScope.launchWhenStarted {
            try {
                mForecastViewModel =
                    ViewModelProviders.of(activity!!, viewModelFactory).get(ForecastViewModel::class.java)

                setGps()
                toolbar.title = getString(R.string.toolbar_forecast_title)

                swipeRefresh.setOnRefreshListener {
                    setGps()
                }
                mForecastViewModel.forecastResult.observe(this@ForecastFragment, Observer {
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

    private fun showLoading() {
        llNoDataLayout.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
        rvForecast.visibility = View.GONE
    }

    private fun hideLoading() {
        llNoDataLayout.visibility = View.GONE
        progressBar.visibility = View.GONE
        rvForecast.visibility = View.VISIBLE
        if (swipeRefresh.isRefreshing)
            swipeRefresh.isRefreshing = false
    }

    private fun setResponseToView(response: ForecastResponse) {
        tvCity.text = response.city.name +", "+ response.city.country
        rvForecast.layoutManager = LinearLayoutManager(activity)
        val adapter = ForecastRecyclerAdapter()
        rvForecast.adapter = adapter
        adapter.setForecast(mapper.mapToPresentation(response.list))

    }

    private fun setGps(){
        val mFusedLocationClient = LocationServices.getFusedLocationProviderClient(activity!!)
        val gps = GpsProvider(activity!!,mFusedLocationClient)
        gps.getLastLocation()
        gps.locationListener { latitude, longitude ->
            mForecastViewModel.fetchForecast(latitude,longitude)
        }

    }




}
