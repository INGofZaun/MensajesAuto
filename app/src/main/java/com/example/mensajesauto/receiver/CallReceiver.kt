package com.example.mensajesauto.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.telephony.TelephonyManager
import android.util.Log
import com.example.mensajesauto.SharedPrefManager
import com.example.mensajesauto.service.CallMonitorService

class CallReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("CallReceiver", "Broadcast recibido")

        if (intent?.action == TelephonyManager.ACTION_PHONE_STATE_CHANGED) {
            val state = intent.getStringExtra(TelephonyManager.EXTRA_STATE)
            Log.d("CallReceiver", "Estado del teléfono: $state")

            if (state == TelephonyManager.EXTRA_STATE_RINGING) {
                Log.d("CallReceiver", "Llamada entrante detectada.")

                if (context != null) {
                    val sharedPref = SharedPrefManager(context)
                    val savedNumber = sharedPref.getPhoneNumber()
                    val message = sharedPref.getMessage()

                    Log.d("CallReceiver", "Número guardado en preferencias: $savedNumber")
                    Log.d("CallReceiver", "Mensaje guardado en preferencias: $message")

                    if (!savedNumber.isNullOrEmpty() && !message.isNullOrEmpty()) {
                        Log.d("CallReceiver", "Iniciando servicio CallMonitorService...")

                        val serviceIntent = Intent(context, CallMonitorService::class.java).apply {
                            putExtra("incomingNumber", savedNumber)
                        }

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            context.startForegroundService(serviceIntent)
                        } else {
                            context.startService(serviceIntent)
                        }
                    } else {
                        Log.d("CallReceiver", "Número o mensaje vacío, no se enviará SMS.")
                    }
                }
            }
        }
    }
}
