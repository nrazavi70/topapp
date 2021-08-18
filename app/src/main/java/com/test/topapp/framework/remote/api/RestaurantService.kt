package com.test.topapp.framework.remote.api

import com.test.topapp.framework.remote.response.RestaurantResponse
import retrofit2.http.*

interface RestaurantService {
    @FormUrlEncoded
    @POST("/res/new")
    suspend fun create(
        @Header("id") ownerId: Int,
        @Header("token") ownerToken: String,
        @Field("name") name: String,
        @Field("lat") locLAT: Float,
        @Field("lng") locLNG: Float,
        @Field("address") address: String
    ): RestaurantResponse

    @FormUrlEncoded
    @PUT("/res")
    suspend fun update(
        @Header("id") ownerId: Int,
        @Header("token") ownerToken: String,
        @Field("id") id: Int,
        @Field("name") name: String?,
        @Field("lat") locLAT: Float?,
        @Field("lng") locLNG: Float?,
        @Field("address") address: String?
    )

    @DELETE("/res")
    suspend fun delete(
        @Header("id") ownerId: Int,
        @Header("token") ownerToken: String,
        @Header("rId") id: Int
    )

    @GET("/res/{rId}")
    suspend fun getRestaurant(
        @Header("id") userId: Int,
        @Header("token") userToken: String,
        @Path("rId") id: Int
    ): RestaurantResponse

    @GET("/res/list/{page}")
    suspend fun getUserRestaurants(
        @Header("id") userId: Int,
        @Header("token") userToken: String,
        @Path("page") page: Int = 1
    ): List<RestaurantResponse>
}