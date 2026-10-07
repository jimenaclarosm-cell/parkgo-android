package com.example.reservagaraje

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.widget.Toast

class ListaGarajesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_garaje_detalle)

        // Configurar Toolbar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Garaje disponible"
        toolbar.setNavigationOnClickListener { onBackPressed() }

        // Datos fijos del garaje
        val nombre = "Garaje ParkGo - Centro"
        val direccion = "Calle Comercio N° 123, La Paz"
        val precio = "Bs. 5 / hora"
        val horario = "07:00 - 22:00"
        val capacidad = "20 espacios en total"
        val disponibles = "15 espacios disponibles"
        val latitud = -16.5000
        val longitud = -68.1500

        // Conectar vistas
        findViewById<TextView>(R.id.txtNombreGaraje).text = nombre
        findViewById<TextView>(R.id.txtDireccionGaraje).text = direccion
        findViewById<TextView>(R.id.txtPrecioGaraje).text = precio
        findViewById<TextView>(R.id.txtHorarioGaraje).text = "Horario: $horario"
        findViewById<TextView>(R.id.txtCapacidadGaraje).text = capacidad
        findViewById<TextView>(R.id.txtDisponiblesGaraje).text = disponibles

        // Botón Ver en Google Maps
        findViewById<Button>(R.id.btnVerMapa).setOnClickListener {
            val uri = Uri.parse(
                "geo:$latitud,$longitud?q=$latitud,$longitud($nombre)"
            )
            val intent = Intent(Intent.ACTION_VIEW, uri)
            intent.setPackage("com.google.android.apps.maps")
            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            } else {
                // Si no tiene Google Maps abre en navegador
                val browserUri = Uri.parse(
                    "https://www.google.com/maps/search/?api=1&query=$latitud,$longitud"
                )
                startActivity(Intent(Intent.ACTION_VIEW, browserUri))
            }
        }

        // Botón Reservar
        findViewById<Button>(R.id.btnReservar).setOnClickListener {
            startActivity(Intent(this, ReservaActivity::class.java))
        }
    }
}