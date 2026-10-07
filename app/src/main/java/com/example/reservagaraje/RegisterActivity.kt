package com.example.reservagaraje

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RegisterActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editNombre: TextInputEditText
    private lateinit var editEmail: TextInputEditText
    private lateinit var editPassword: TextInputEditText
    private lateinit var editConfirmarPassword: TextInputEditText
    private lateinit var btnRegistrar: Button
    private lateinit var btnVolver: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Inicializar SharedPreferences
        sharedPreferences = getSharedPreferences("parkgo_prefs", Context.MODE_PRIVATE)

        // Conectar vistas
        editNombre = findViewById(R.id.editNombre)
        editEmail = findViewById(R.id.editEmailRegister)
        editPassword = findViewById(R.id.editPasswordRegister)
        editConfirmarPassword = findViewById(R.id.editConfirmarPassword)
        btnRegistrar = findViewById(R.id.btnRegistrar)
        btnVolver = findViewById(R.id.btnVolver)

        // Botón crear cuenta
        btnRegistrar.setOnClickListener {
            val nombre = editNombre.text.toString().trim()
            val email = editEmail.text.toString().trim()
            val password = editPassword.text.toString().trim()
            val confirmarPassword = editConfirmarPassword.text.toString().trim()

            // Validaciones
            if (nombre.isEmpty()) {
                Toast.makeText(this, "Ingresa tu nombre completo", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (email.isEmpty()) {
                Toast.makeText(this, "Ingresa tu correo electrónico", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Ingresa un correo válido", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (password.isEmpty()) {
                Toast.makeText(this, "Ingresa una contraseña", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (password.length < 6) {
                Toast.makeText(this, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (password != confirmarPassword) {
                Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            registrarUsuario(nombre, email, password)
        }

        // Botón volver al login
        btnVolver.setOnClickListener {
            finish()
        }
    }

    private fun registrarUsuario(nombre: String, email: String, password: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8000/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(RegisterApiService::class.java)

        apiService.register(nombre, email, password).enqueue(object : retrofit2.Callback<LoginResponse> {
            override fun onResponse(
                call: retrofit2.Call<LoginResponse>,
                response: retrofit2.Response<LoginResponse>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    val loginResponse = response.body()!!
                    val editor = sharedPreferences.edit()
                    editor.putString("access_token", loginResponse.access_token)
                    editor.putString("user_email", email)
                    editor.putString("user_name", nombre)
                    editor.apply()

                    Toast.makeText(
                        this@RegisterActivity,
                        "¡Registro exitoso! Bienvenido $nombre",
                        Toast.LENGTH_SHORT
                    ).show()

                    // Ir directamente a pantalla principal
                    val intent = Intent(this@RegisterActivity, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()

                } else {
                    Toast.makeText(
                        this@RegisterActivity,
                        "No se pudo registrar. El correo ya existe.",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: retrofit2.Call<LoginResponse>, t: Throwable) {
                Toast.makeText(
                    this@RegisterActivity,
                    "Error de conexión: ${t.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }
}