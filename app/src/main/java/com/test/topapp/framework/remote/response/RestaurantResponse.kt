package com.test.topapp.framework.remote.response

data class RestaurantResponse(
    val id: Int,
    val ownerId: Int,
    val name: String,
    val loc_lat: Float,
    val loc_lng: Float,
    val address: String,
    val avgRate: Float,
    val revCount: Int,
    val lowestRev: List<ReviewResponse>?,
    val highestRev: List<ReviewResponse>?
)