package com.demo.openweather.weather.presentation.ui.fragments


import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.demo.openweather.R
import com.demo.openweather.R.menu.weather_menu
import com.demo.openweather.common.Status
import com.demo.openweather.common.ViewModelFactory
import com.demo.openweather.weather.data.model.WeatherResponse
import com.demo.openweather.weather.presentation.ui.adapter.WeatherRecyclerviewAdapter
import com.demo.openweather.weather.presentation.viewmodel.WeatherViewModel
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_weather.*
import okhttp3.internal.wait
import javax.inject.Inject

class WeatherFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var mWeatherViewModel: WeatherViewModel
    private var lstOfWeatherResponse : ArrayList<WeatherResponse>? = ArrayList()
    private var citiesList : List<String> = ArrayList()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_weather, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launchWhenStarted {
            try {
                mWeatherViewModel =
                    ViewModelProviders.of(activity!!, viewModelFactory)
                        .get(WeatherViewModel::class.java)
                setToolbarViews()

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

    private fun setToolbarViews() {
        toolbar.title = "Weather"
        toolbar.inflateMenu(weather_menu)
        toolbar.setOnMenuItemClickListener { item ->
            val id = item.itemId
            if (id == R.id.action_add) {
                showCreateCategoryDialog()
            }
            if (id == R.id.action_forecast) {
                view?.let { Navigation.findNavController(it).navigate(R.id.action_navigate_forecast) }
            }
            false
        }
    }

    private fun setResponseToView(response: WeatherResponse) {
        if (lstOfWeatherResponse?.size != listOfCities().size)
            lstOfWeatherResponse?.add(response)
        rvWeather.layoutManager = LinearLayoutManager(activity)
        val adapter = WeatherRecyclerviewAdapter()
        lstOfWeatherResponse?.let { adapter.populateWeather(it) }
        rvWeather.adapter = adapter
    }

    private fun showLoading() {
        llNoDataLayout.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
        rvWeather.visibility = View.GONE
    }

    private fun hideLoading() {
        llNoDataLayout.visibility = View.GONE
        progressBar.visibility = View.GONE
        rvWeather.visibility = View.VISIBLE
    }


    private fun showCreateCategoryDialog() {
        val builder = context?.let { AlertDialog.Builder(it) }
        val view = layoutInflater.inflate(R.layout.dialog_new_city, null)
        val cityEditText = view.findViewById(R.id.etCities) as EditText
        builder?.setView(view)
        builder?.setCancelable(false)


        builder?.setPositiveButton(android.R.string.ok) {dialog: DialogInterface?, which: Int ->
            val city = cityEditText.text
            lstOfWeatherResponse?.clear()
            citiesList = city.split(",").map { it.trim() }
            if (citiesList.size in 3..7){
                citiesList.forEach {
                    mWeatherViewModel.fetchWeather(it)
                    dialog?.dismiss()
                }
            }else {
                Toast.makeText(context, "Add minimum 3 cities and maximum 7 cities", Toast.LENGTH_SHORT).show();
                showCreateCategoryDialog()
            }

        }

        builder?.setNegativeButton(android.R.string.cancel) { dialog, p1 ->
            dialog.cancel()
        }
        builder?.show()
    }



    private fun listOfCities() = arrayListOf<String>("Hyderabad", "Delhi","Chennai","London","Auckland")


}
