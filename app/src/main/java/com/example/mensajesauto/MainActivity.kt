package com.example.mensajesauto

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log  // 🔹 Importación agregada
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

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
            val phoneNumber = phoneNumberInput.text.toString().trim()
            val message = messageInput.text.toString().trim()

            if (phoneNumber.isNotEmpty() && message.isNotEmpty()) {
                sharedPref.savePhoneNumber(phoneNumber)
                sharedPref.saveMessage(message)
                Toast.makeText(this, "Configuración guardada", Toast.LENGTH_SHORT).show()
                Log.d("MainActivity", "Número y mensaje guardados correctamente") // ✅ Log funcionando
            } else {
                Toast.makeText(this, "Ingrese un número y mensaje válidos", Toast.LENGTH_SHORT).show()
            }
        }

        requestPermissions()
    }

    private fun requestPermissions() {
        val permissions = arrayOf(
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.SEND_SMS
        )

        if (!permissions.all { ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED }) {
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { result ->
                if (result.all { it.value }) {
                    Toast.makeText(this, "Permisos concedidos", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Permisos denegados. La app podría no funcionar correctamente.", Toast.LENGTH_LONG).show()
                }
            }.launch(permissions)
        }
    }
}
