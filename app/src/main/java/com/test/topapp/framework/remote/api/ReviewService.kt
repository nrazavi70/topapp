package com.test.topapp.framework.remote.api

import com.test.topapp.framework.remote.response.ReviewResponse
import retrofit2.http.*

interface ReviewService {
    @FormUrlEncoded
    @POST("/rev/new")
    suspend fun create(
        @Header("id") userId: Int,
        @Header("token") userToken: String,
        @Field("rate") rate: Int,
        @Field("review") review: String,
        @Field("rId") restaurantId: Int
    ): ReviewResponse

    @GET("/rev/{rId}")
    suspend fun getReview(
        @Header("id") userId: Int,
        @Header("token") userToken: String,
        @Path("rId") reviewId: Int
    ): ReviewResponse

    @GET("/rev/list/{page}")
    suspend fun getReviews(
        @Header("id") adminId: Int,
        @Header("token") adminToken: String,
        @Path("page") page: Int
    ): List<ReviewResponse>

    @GET("/rev/pending/{page}")
    suspend fun getPendingReviews(
        @Header("id") ownerId: Int,
        @Header("token") ownerToken: String,
        @Path("page") page: Int
    ): List<ReviewResponse>

    @GET("/rev/{rId}/{page}")
    suspend fun getRestaurantReviews(
        @Header("id") userId: Int,
        @Header("token") userToken: String,
        @Path("rId") restaurantId: Int,
        @Path("page") page: Int
    ): List<ReviewResponse>

    @FormUrlEncoded
    @POST("/rev/answer")
    suspend fun responseReview(
        @Header("id") ownerId: Int,
        @Header("token") ownerToken: String,
        @Field("revId") reviewId: Int,
        @Field("response") response: String
    )

    @DELETE("/rev")
    suspend fun deleteReview(
        @Header("id") adminId: Int,
        @Header("token") adminToken: String,
        @Header("review") reviewId: Int
    )

    @FormUrlEncoded
    @PUT("/rev")
    suspend fun updateReview(
        @Header("id") adminId: Int,
        @Header("token") adminToken: String,
        @Field("id") reviewId: Int,
        @Field("response") response: String?,
        @Field("rate") rate: Int,
        @Field("review") review: String,
    )
}