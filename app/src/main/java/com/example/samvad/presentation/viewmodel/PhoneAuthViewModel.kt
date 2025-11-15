package com.example.samvad.presentation.viewmodel

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.samvad.data.local.TokenManager
import com.example.samvad.data.remote.api.AuthApi
import com.example.samvad.data.remote.api.UserApi
import com.example.samvad.data.remote.dto.SendOtpRequest
import com.example.samvad.data.remote.dto.UpdateProfileRequest
import com.example.samvad.data.remote.dto.VerifyOtpRequest
import com.example.samvad.models.PhoneAuthUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream
import javax.inject.Inject

@HiltViewModel
class PhoneAuthViewModel @Inject constructor(
    private val authApi: AuthApi,
    private val userApi: UserApi,
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Ideal)
    val authState = _authState.asStateFlow()

    private var currentPhoneNumber: String = ""

    fun sendVerificationCode(phoneNumber: String, activity: Activity) {
        _authState.value = AuthState.Loading
        currentPhoneNumber = phoneNumber

        viewModelScope.launch {
            try {
                val response = authApi.sendOtp(SendOtpRequest(phoneNumber))

                if (response.isSuccessful && response.body() != null) {
                    Log.d("PhoneAuth", "OTP sent successfully to $phoneNumber")
                    _authState.value = AuthState.CodeSent(verificationId = phoneNumber)
                } else {
                    Log.e("PhoneAuth", "Failed to send OTP: ${response.errorBody()?.string()}")
                    _authState.value = AuthState.Error("Failed to send OTP")
                }
            } catch (e: Exception) {
                Log.e("PhoneAuth", "Error sending OTP: ${e.message}")
                _authState.value = AuthState.Error(e.message ?: "Network error")
            }
        }

    }

    fun verifyCode(otp: String, context: Context) {
        _authState.value = AuthState.Loading

        viewModelScope.launch {
            try {
                val response = authApi.verifyOtp(VerifyOtpRequest(currentPhoneNumber, otp))

                if (response.isSuccessful && response.body() != null) {
                    val authResponse = response.body()!!

                    // Save tokens and user info
                    tokenManager.saveTokens(authResponse.token, authResponse.refreshToken)
                    tokenManager.saveUserId(authResponse.user.userId)
                    tokenManager.savePhoneNumber(authResponse.user.phoneNumber)
                    tokenManager.markUserAsSignedIn(true)

                    // Convert UserDto to PhoneAuthUser
                    val phoneAuthUser = PhoneAuthUser(
                        userId = authResponse.user.userId,
                        phoneNumber = authResponse.user.phoneNumber,
                        name = authResponse.user.name,
                        status = authResponse.user.status,
                        profileImage = null // Will be loaded separately
                    )

                    Log.d("PhoneAuth", "OTP verified successfully")
                    _authState.value = AuthState.Success(phoneAuthUser)
                } else {
                    Log.e("PhoneAuth", "OTP verification failed: ${response.errorBody()?.string()}")
                    _authState.value = AuthState.Error("Invalid OTP")
                }
            } catch (e: Exception) {
                Log.e("PhoneAuth", "Error verifying OTP: ${e.message}")
                _authState.value = AuthState.Error(e.message ?: "Verification failed")
            }
        }
    }

    private fun markUserAsSignedIn(context: Context) {
        tokenManager.markUserAsSignedIn(true)
    }

    fun saveUserProfile(userId: String, name: String, status: String, profileImage: Bitmap?) {
        viewModelScope.launch {
            try {
                // Update profile name and status
                val updateRequest = UpdateProfileRequest(name, status)
                val profileResponse = userApi.updateProfile(updateRequest)

                if (profileResponse.isSuccessful) {
                    Log.d("PhoneAuth", "Profile updated successfully")

                    // Upload profile image if provided
                    profileImage?.let { bitmap ->
                        uploadProfileImage(bitmap)
                    }
                } else {
                    Log.e("PhoneAuth", "Failed to update profile: ${profileResponse.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("PhoneAuth", "Error saving profile: ${e.message}")
            }
        }
    }

    private suspend fun uploadProfileImage(bitmap: Bitmap) {
        try {
            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream)
            val imageBytes = byteArrayOutputStream.toByteArray()

            val requestBody = imageBytes.toRequestBody("image/jpeg".toMediaTypeOrNull())
            val multipartBody = MultipartBody.Part.createFormData("image", "profile.jpg", requestBody)

            val response = userApi.uploadProfileImage(multipartBody)

            if (response.isSuccessful) {
                Log.d("PhoneAuth", "Profile image uploaded successfully")
            } else {
                Log.e("PhoneAuth", "Failed to upload image: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            Log.e("PhoneAuth", "Error uploading image: ${e.message}")
        }
    }

    fun resetAuthState() {
        _authState.value = AuthState.Ideal
    }

    fun signOut(activity: Activity) {
        tokenManager.clearTokens()
        _authState.value = AuthState.Ideal
    }

}

sealed class AuthState {
    object Ideal : AuthState()
    object Loading : AuthState()
    data class CodeSent(val verificationId: String) : AuthState()
    data class Success(val user: PhoneAuthUser) : AuthState()
    data class Error(val message: String) : AuthState()
}