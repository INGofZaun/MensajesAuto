package com.example.mensajesauto

import android.content.Context
import android.content.SharedPreferences

class SharedPrefManager(context: Context) {
    private val sharedPref: SharedPreferences =
        context.getSharedPreferences("MensajesAutoPrefs", Context.MODE_PRIVATE)

    fun savePhoneNumber(phoneNumber: String) {
        sharedPref.edit().putString("phoneNumber", phoneNumber).apply()
    }

    fun saveMessage(message: String) {
        sharedPref.edit().putString("message", message).apply()
    }

    fun getPhoneNumber(): String {
        return sharedPref.getString("phoneNumber", "") ?: ""
    }

    fun getMessage(): String {
        return sharedPref.getString("message", "") ?: ""
    }
}
