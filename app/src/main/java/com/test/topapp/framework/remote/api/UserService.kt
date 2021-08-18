package com.test.topapp.framework.remote.api

import com.test.core.domain.Role
import com.test.topapp.framework.remote.response.UserResponse
import retrofit2.http.*

interface UserService {
    @FormUrlEncoded
    @POST("/user/login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String,
    ): UserResponse

    @FormUrlEncoded
    @POST("/user/register")
    suspend fun register(
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("role") role: Role,
        @Field("firstname") firstname: String?,
        @Field("lastname") lastname: String?,
    ): UserResponse

    @GET("/user/{uId}")
    suspend fun getUser(
        @Header("id") adminId: Int,
        @Header("token") adminToken: String,
        @Path("uId") userId: Int
    ): UserResponse

    @GET("/user/list/{page}")
    suspend fun getUsers(
        @Header("id") adminId: Int,
        @Header("token") adminToken: String,
        @Path("page") page: Int
    ): List<UserResponse>

    @DELETE("/user")
    suspend fun deleteUser(
        @Header("id") adminId: Int,
        @Header("token") adminToken: String,
        @Header("user") userId: Int
    ): UserResponse

    @FormUrlEncoded
    @PUT("/user")
    suspend fun updateUser(
        @Header("id") adminId: Int,
        @Header("token") adminToken: String,
        @Field("id") userId: Int,
        @Field("email") email: String,
        @Field("role") role: Role,
        @Field("firstname") firstname: String?,
        @Field("lastname") lastname: String?,
    ): UserResponse
}