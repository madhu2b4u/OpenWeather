package com.demo.openweather.di

import com.demo.openweather.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class ActivityBuilderModule {
    @ContributesAndroidInjector
    internal abstract fun contributesMainActivity(): MainActivity
}