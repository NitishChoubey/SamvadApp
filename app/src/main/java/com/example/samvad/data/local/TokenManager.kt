package com.example.samvad.data.local

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenManager @Inject constructor(
    @ApplicationContext context: Context
) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("samvad_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_TOKEN = "auth_token"
        private const val KEY_REFRESH_TOKEN = "refresh_token"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_PHONE_NUMBER = "phone_number"
        private const val KEY_IS_SIGNED_IN = "isSignedIn"
    }

    fun saveTokens(token: String, refreshToken: String) {
        prefs.edit().apply {
            putString(KEY_TOKEN, token)
            putString(KEY_REFRESH_TOKEN, refreshToken)
            apply()
        }
    }

    fun getToken(): String? = prefs.getString(KEY_TOKEN, null)

    fun getRefreshToken(): String? = prefs.getString(KEY_REFRESH_TOKEN, null)

    fun saveUserId(userId: String) {
        prefs.edit().putString(KEY_USER_ID, userId).apply()
    }

    fun getUserId(): String? = prefs.getString(KEY_USER_ID, null)

    fun savePhoneNumber(phoneNumber: String) {
        prefs.edit().putString(KEY_PHONE_NUMBER, phoneNumber).apply()
    }

    fun getPhoneNumber(): String? = prefs.getString(KEY_PHONE_NUMBER, null)

    fun markUserAsSignedIn(isSignedIn: Boolean) {
        prefs.edit().putBoolean(KEY_IS_SIGNED_IN, isSignedIn).apply()
    }

    fun isUserSignedIn(): Boolean = prefs.getBoolean(KEY_IS_SIGNED_IN, false)

    fun clearTokens() {
        prefs.edit().apply {
            remove(KEY_TOKEN)
            remove(KEY_REFRESH_TOKEN)
            remove(KEY_USER_ID)
            remove(KEY_PHONE_NUMBER)
            putBoolean(KEY_IS_SIGNED_IN, false)
            apply()
        }
    }
}

