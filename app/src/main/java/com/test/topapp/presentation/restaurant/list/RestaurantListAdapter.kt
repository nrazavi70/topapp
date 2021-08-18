package com.test.topapp.presentation.restaurant.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.test.core.domain.Restaurant
import com.test.topapp.databinding.RestaurantListItemBinding
import com.test.topapp.utils.Event

class RestaurantListAdapter(private val eventGate: (Event.Info) -> Unit) :
    ListAdapter<Restaurant, RestaurantListViewHolder>(RestaurantListDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = RestaurantListViewHolder(
        RestaurantListItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    )

    override fun onBindViewHolder(holder: RestaurantListViewHolder, position: Int) {
        val item = getItem(position)
        holder.binding.restaurant = item
        item.averageRate.toString()

        holder.binding.restaurantListItem.setOnClickListener {
            eventGate(Event.Info(restaurantClicked, item))
        }
    }

    companion object {
        const val restaurantClicked = 2000
    }
}