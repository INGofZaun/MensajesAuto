package com.example.mensajesauto

import android.content.Context
import android.content.SharedPreferences
import android.util.Log

class SharedPrefManager(context: Context) {
    private val sharedPref: SharedPreferences =
        context.getSharedPreferences("MensajesAutoPrefs", Context.MODE_PRIVATE)

    fun savePhoneNumber(phoneNumber: String) {
        sharedPref.edit().putString("phoneNumber", phoneNumber).apply()
        Log.d("SharedPrefManager", "Número guardado: $phoneNumber")
    }

    fun saveMessage(message: String) {
        sharedPref.edit().putString("message", message).apply()
        Log.d("SharedPrefManager", "Mensaje guardado: $message")
    }

    fun getPhoneNumber(): String {
        val number = sharedPref.getString("phoneNumber", "") ?: ""
        Log.d("SharedPrefManager", "Número recuperado: $number")
        return number
    }

    fun getMessage(): String {
        val message = sharedPref.getString("message", "") ?: ""
        Log.d("SharedPrefManager", "Mensaje recuperado: $message")
        return message
    }
}
