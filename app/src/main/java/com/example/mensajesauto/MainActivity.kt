package com.example.mensajesauto

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat

class MainActivity : AppCompatActivity() {

    private lateinit var phoneNumberInput: EditText
    private lateinit var messageInput: EditText
    private lateinit var saveButton: Button
    private lateinit var sharedPref: SharedPrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        phoneNumberInput = findViewById(R.id.phoneNumberInput)
        messageInput = findViewById(R.id.messageInput)
        saveButton = findViewById(R.id.saveButton)
        sharedPref = SharedPrefManager(this)

        // Cargar valores guardados
        phoneNumberInput.setText(sharedPref.getPhoneNumber())
        messageInput.setText(sharedPref.getMessage())

        // Botón para guardar número y mensaje
        saveButton.setOnClickListener {
            val phoneNumber = phoneNumberInput.text.toString()
            val message = messageInput.text.toString()

            if (phoneNumber.isNotEmpty() && message.isNotEmpty()) {
                sharedPref.savePhoneNumber(phoneNumber)
                sharedPref.saveMessage(message)
                Toast.makeText(this, "Configuración guardada", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Por favor, ingrese todos los campos", Toast.LENGTH_SHORT).show()
            }
        }

        // Solicitar permisos
        requestPermissions()
    }

    private fun requestPermissions() {
        val permissions = arrayOf(
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.SEND_SMS,
            Manifest.permission.RECEIVE_SMS
        )

        if (!permissions.all {
                ActivityCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
            }) {
            ActivityCompat.requestPermissions(this, permissions, 1)
        }
    }
}
