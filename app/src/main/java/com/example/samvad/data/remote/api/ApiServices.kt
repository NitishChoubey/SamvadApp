package com.example.samvad.data.remote.api

import com.example.samvad.data.remote.dto.*
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface AuthApi {

    @POST("api/auth/send-otp")
    suspend fun sendOtp(@Body request: SendOtpRequest): Response<SendOtpResponse>

    @POST("api/auth/verify-otp")
    suspend fun verifyOtp(@Body request: VerifyOtpRequest): Response<AuthResponse>

    @POST("api/auth/refresh")
    suspend fun refreshToken(@Header("Authorization") token: String): Response<AuthResponse>
}

interface UserApi {

    @GET("api/users/profile")
    suspend fun getProfile(): Response<UserDto>

    @GET("api/users/profile/{userId}")
    suspend fun getUserProfile(@Path("userId") userId: String): Response<UserDto>

    @PUT("api/users/profile")
    suspend fun updateProfile(@Body request: UpdateProfileRequest): Response<UserDto>

    @Multipart
    @POST("api/users/profile/image")
    suspend fun uploadProfileImage(@Part image: MultipartBody.Part): Response<Map<String, String>>

    @GET("api/users/profile/image/{userId}")
    suspend fun getProfileImage(@Path("userId") userId: String): Response<ByteArray>

    @GET("api/users/search")
    suspend fun searchByPhoneNumber(@Query("phoneNumber") phoneNumber: String): Response<UserDto>
}

interface MessageApi {

    @GET("api/messages/conversation/{otherUserId}")
    suspend fun getConversation(@Path("otherUserId") otherUserId: String): Response<List<MessageDto>>
}

