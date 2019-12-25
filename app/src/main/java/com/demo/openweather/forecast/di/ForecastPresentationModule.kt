package com.demo.openweather.forecast.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.demo.openweather.common.ViewModelFactory
import com.demo.openweather.di.ViewModelKey
import com.demo.openweather.forecast.presentation.viewmodel.ForecastViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ForecastPresentationModule {
    @Binds
    abstract fun bindsViewModelFactory(
        factory: ViewModelFactory
    ): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(ForecastViewModel::class)
    abstract fun bindsHomeViewModel(mForecastViewModel: ForecastViewModel): ViewModel
}