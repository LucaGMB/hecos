package com.hecos.mobile.data.api

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import retrofit2.Response
import retrofit2.http.*

interface HecosApi {

    @POST("/auth/google")
    suspend fun authGoogle(@Body body: Map<String, String>): Response<AuthResponse>

    @POST("/api/health/{type}")
    suspend fun syncRecords(
        @Path("type") type: String,
        @Header("Authorization") token: String,
        @Body records: JsonArray
    ): Response<SyncResponse>

    @GET("/api/health/summary")
    suspend fun getSummary(@Header("Authorization") token: String): Response<JsonObject>
}

data class AuthResponse(
    val token: String,
    val userId: String,
    val email: String,
    val name: String
)

data class SyncResponse(
    val received: Int,
    val saved: Int,
    val duplicates: Int
)
