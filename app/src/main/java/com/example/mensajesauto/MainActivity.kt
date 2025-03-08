package com.example.mensajesauto

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat

class MainActivity : ComponentActivity() {

    private lateinit var sharedPref: SharedPrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPref = SharedPrefManager(this)

        setContent {
            AppUI()
        }

        requestPermissions()
    }

    @Composable
    fun AppUI() {
        var phoneNumber by rememberSaveable { mutableStateOf(sharedPref.getPhoneNumber()) }
        var message by rememberSaveable { mutableStateOf(sharedPref.getMessage()) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(text = "Configuración de Respuesta Automática", style = MaterialTheme.typography.headlineMedium)

            OutlinedTextField(
                value = phoneNumber,
                onValueChange = { phoneNumber = it },
                label = { Text("Número de teléfono") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = message,
                onValueChange = { message = it },
                label = { Text("Mensaje de respuesta") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    sharedPref.savePhoneNumber(phoneNumber)
                    sharedPref.saveMessage(message)
                    Toast.makeText(this@MainActivity, "Configuración guardada", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar")
            }
        }
    }

    private fun requestPermissions() {
        val permissions = arrayOf(
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.SEND_SMS
        )

        if (!permissions.all { ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED }) {
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { result ->
                if (!result.all { it.value }) {
                    Toast.makeText(this, "Permisos denegados. La app podría no funcionar correctamente.", Toast.LENGTH_LONG).show()
                }
            }.launch(permissions)
        }
    }
}
