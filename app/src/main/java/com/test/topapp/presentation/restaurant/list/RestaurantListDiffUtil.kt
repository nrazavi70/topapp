package com.test.topapp.presentation.restaurant.list

import androidx.recyclerview.widget.DiffUtil
import com.test.core.domain.Restaurant

class RestaurantListDiffUtil : DiffUtil.ItemCallback<Restaurant>() {
    override fun areItemsTheSame(oldItem: Restaurant, newItem: Restaurant) =
        newItem.id == oldItem.id

    override fun areContentsTheSame(oldItem: Restaurant, newItem: Restaurant) = newItem == oldItem
}