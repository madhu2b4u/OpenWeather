package com.demo.openweather.forecast.presentation.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import com.demo.openweather.forecast.data.model.DateItem
import com.demo.openweather.forecast.data.model.Forecast
import com.demo.openweather.forecast.data.model.ListItem

class ForecastDiffUtilCallback constructor(
    private val oldList: List<ListItem>,
    private val newList: List<ListItem>
) : DiffUtil.Callback() {
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldListItem = oldList[oldItemPosition]
        val newListItem = newList[newItemPosition]

        return if (oldListItem::class == newListItem::class) {
            if (oldListItem is DateItem)
                oldListItem.date == (newListItem as DateItem).date
            else
                (oldListItem as Forecast).dt == (newListItem as Forecast).dt
        } else
            false
    }

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldListItem = oldList[oldItemPosition]
        val newListItem = newList[newItemPosition]

        return if (oldListItem::class == newListItem::class) {
            oldListItem == newListItem
        } else
            false
    }
}