package com.example.mensajesauto.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.telephony.SmsManager
import android.util.Log
import com.example.mensajesauto.SharedPrefManager

class CallMonitorService : Service() {

    override fun onCreate() {
        super.onCreate()

        // Asegurar que la notificación se crea antes de cualquier otra acción
        startForeground(1, createNotification())

        Log.d("MensajesAuto", "Servicio CallMonitorService iniciado correctamente")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val incomingNumber = intent?.getStringExtra("incomingNumber")
        Log.d("MensajesAuto", "Número recibido en servicio: $incomingNumber")

        if (!incomingNumber.isNullOrEmpty()) {
            val sharedPref = SharedPrefManager(applicationContext)
            val storedNumber = sharedPref.getPhoneNumber()

            if (incomingNumber.takeLast(10) == storedNumber.takeLast(10)) {
                Log.d("MensajesAuto", "Número coincide, enviando SMS automático...")
                sendAutoReplySms(incomingNumber)
            } else {
                Log.d("MensajesAuto", "Número NO coincide, no se enviará SMS")
            }
        } else {
            Log.e("MensajesAuto", "No se recibió un número válido")
        }

        return START_NOT_STICKY
    }

    private fun createNotification(): Notification {
        val channelId = "call_monitor_channel"
        val channelName = "Monitoreo de llamadas"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_LOW)
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
        return Notification.Builder(this, channelId)
            .setContentTitle("Monitoreando llamadas")
            .setContentText("El servicio está en ejecución")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .build()
    }

    private fun sendAutoReplySms(phoneNumber: String) {
        val sharedPref = SharedPrefManager(applicationContext)
        val message = sharedPref.getMessage()

        if (message.isNotEmpty()) {
            try {
                Log.d("MensajesAuto", "Enviando mensaje: '$message' a $phoneNumber")
                val smsManager = SmsManager.getDefault()
                smsManager.sendTextMessage(phoneNumber, null, message, null, null)
                Log.d("MensajesAuto", "Mensaje enviado correctamente")
            } catch (e: Exception) {
                Log.e("MensajesAuto", "Error al enviar SMS: ${e.message}")
            }
        } else {
            Log.e("MensajesAuto", "Mensaje vacío, no se enviará SMS")
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
