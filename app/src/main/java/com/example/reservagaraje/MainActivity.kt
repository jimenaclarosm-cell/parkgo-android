package com.example.reservagaraje

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var txtUserInfo: TextView
    private lateinit var btnLogout: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inicializar SharedPreferences
        sharedPreferences = getSharedPreferences("parkgo_prefs", Context.MODE_PRIVATE)

        // Verificar si hay token guardado
        val token = sharedPreferences.getString("access_token", null)
        if (token.isNullOrEmpty()) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        // Conectar vistas
        txtUserInfo = findViewById(R.id.txtUserInfo)
        btnLogout = findViewById(R.id.btnLogout)

        // Mostrar correo del usuario autenticado
        val userEmail = sharedPreferences.getString("user_email", "usuario@ejemplo.com")
        txtUserInfo.text = "Bienvenido: $userEmail"

        // Botones principales
        val btnBuscar = findViewById<Button>(R.id.btnBuscar)
        val btnMisReservas = findViewById<Button>(R.id.btnMisReservas)

        // Navegar a información del garaje
        btnBuscar.setOnClickListener {
            startActivity(Intent(this, ListaGarajesActivity::class.java))
        }

        // Navegar a mis reservas
        btnMisReservas.setOnClickListener {
            startActivity(Intent(this, MisReservasActivity::class.java))
        }

        // Cerrar sesión
        btnLogout.setOnClickListener {
            sharedPreferences.edit().clear().apply()
            Toast.makeText(this, "Sesión cerrada correctamente", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}