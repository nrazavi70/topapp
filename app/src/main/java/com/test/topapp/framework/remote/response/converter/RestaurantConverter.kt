package com.test.topapp.framework.remote.response.converter

import com.test.core.domain.Restaurant
import com.test.topapp.framework.remote.response.RestaurantResponse

fun RestaurantResponse.toDomain() = Restaurant(
    this.id,
    this.ownerId,
    this.name,
    this.loc_lat,
    this.loc_lng,
    this.address,
    this.avgRate,
    this.revCount,
    if (this.lowestRev != null) this.lowestRev.map { it.toDomain() } else listOf(),
    if (this.highestRev != null) this.highestRev.map { it.toDomain() } else listOf()
)