package com.kafein.turkcellsaha.data.api

import com.kafein.turkcellsaha.data.model.*
import io.reactivex.Observable
import okhttp3.MultipartBody
import retrofit2.http.*

interface RestInterface {

    @POST("uaa-server/auth/login")
    fun login(@Body request: LoginRequest): Observable<TokenResponse>

    @GET("/dashboard-service/summaries")
    fun fetchSummaries(@Query("month") month: Int?): Observable<Summary>

    @GET("/wall-service/all")
    fun fetchWallContent(
        @Query("pageNo") pageNo: Int?,
        @Query("pageSize") pageSize: Int?,
        @Query("direction") direction: String = "DESC"
    ): Observable<Wall>



    @DELETE("/wall-service/{id}")
    fun deletePost(
        @Path("id") id: Int
    ): Observable<Any>


    @Multipart
    @PUT("/wall-service/upload")
    fun submitPost(
        @Part file: MultipartBody.Part,
        @Part("text") text: String
    ): Observable<WallContent>


    @PUT("/wall-service/{id}")
    fun updatePost(@Body request: PostUpdateRequest, @Path("id") id : Int): Observable<WallContent>


}