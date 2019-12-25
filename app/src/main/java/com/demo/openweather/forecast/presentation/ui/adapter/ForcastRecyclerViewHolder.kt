package com.demo.openweather.forecast.presentation.ui.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class ForcastRecyclerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    abstract fun bind()
}