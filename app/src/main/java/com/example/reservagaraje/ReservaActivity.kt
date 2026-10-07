package com.example.reservagaraje

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import java.util.Calendar

class ReservaActivity : AppCompatActivity() {

    // Variables para guardar fecha y hora seleccionadas
    private var fechaSeleccionada = ""
    private var horaEntrada = ""
    private var horaSalida = ""
    private var precioPorHora = 5.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reserva)

        // Configurar Toolbar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Nueva Reserva"
        toolbar.setNavigationOnClickListener { onBackPressed() }

        // Conectar vistas
        val txtFecha = findViewById<TextView>(R.id.txtFechaSeleccionada)
        val txtHoraEntrada = findViewById<TextView>(R.id.txtHoraEntrada)
        val txtHoraSalida = findViewById<TextView>(R.id.txtHoraSalida)
        val txtCostoTotal = findViewById<TextView>(R.id.txtCostoTotal)
        val btnSeleccionarFecha = findViewById<Button>(R.id.btnSeleccionarFecha)
        val btnHoraEntrada = findViewById<Button>(R.id.btnHoraEntrada)
        val btnHoraSalida = findViewById<Button>(R.id.btnHoraSalida)
        val btnConfirmarReserva = findViewById<Button>(R.id.btnConfirmarReserva)

        // Seleccionar fecha
        btnSeleccionarFecha.setOnClickListener {
            val calendar = Calendar.getInstance()
            val año = calendar.get(Calendar.YEAR)
            val mes = calendar.get(Calendar.MONTH)
            val dia = calendar.get(Calendar.DAY_OF_MONTH)

            DatePickerDialog(this, { _, y, m, d ->
                fechaSeleccionada = "$d/${m + 1}/$y"
                txtFecha.text = "Fecha: $fechaSeleccionada"
                calcularCosto(txtCostoTotal)
            }, año, mes, dia).show()
        }

        // Seleccionar hora de entrada
        btnHoraEntrada.setOnClickListener {
            val calendar = Calendar.getInstance()
            TimePickerDialog(this, { _, hora, minuto ->
                horaEntrada = String.format("%02d:%02d", hora, minuto)
                txtHoraEntrada.text = "Entrada: $horaEntrada"
                calcularCosto(txtCostoTotal)
            }, calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE), true).show()
        }

        // Seleccionar hora de salida
        btnHoraSalida.setOnClickListener {
            val calendar = Calendar.getInstance()
            TimePickerDialog(this, { _, hora, minuto ->
                horaSalida = String.format("%02d:%02d", hora, minuto)
                txtHoraSalida.text = "Salida: $horaSalida"
                calcularCosto(txtCostoTotal)
            }, calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE), true).show()
        }

        // Confirmar reserva
        btnConfirmarReserva.setOnClickListener {
            if (fechaSeleccionada.isEmpty()) {
                Toast.makeText(this, "Selecciona una fecha", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (horaEntrada.isEmpty()) {
                Toast.makeText(this, "Selecciona hora de entrada", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (horaSalida.isEmpty()) {
                Toast.makeText(this, "Selecciona hora de salida", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (horaEntrada >= horaSalida) {
                Toast.makeText(this, "La hora de salida debe ser mayor a la de entrada", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Obtener token guardado
            val sharedPreferences = getSharedPreferences("parkgo_prefs", Context.MODE_PRIVATE)
            val token = sharedPreferences.getString("access_token", null)

            if (token.isNullOrEmpty()) {
                Toast.makeText(this, "Sesión expirada, inicia sesión nuevamente", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Mostrar confirmación
            Toast.makeText(
                this,
                "¡Reserva confirmada!\nFecha: $fechaSeleccionada\nEntrada: $horaEntrada\nSalida: $horaSalida",
                Toast.LENGTH_LONG
            ).show()

            // Regresar a pantalla anterior
            finish()
        }
    }

    // Calcular costo total según horas seleccionadas
    private fun calcularCosto(txtCostoTotal: TextView) {
        if (horaEntrada.isNotEmpty() && horaSalida.isNotEmpty()) {
            val partsEntrada = horaEntrada.split(":")
            val partsSalida = horaSalida.split(":")

            val minutosEntrada = partsEntrada[0].toInt() * 60 + partsEntrada[1].toInt()
            val minutosSalida = partsSalida[0].toInt() * 60 + partsSalida[1].toInt()

            val diferenciaMinutos = minutosSalida - minutosEntrada

            if (diferenciaMinutos > 0) {
                val horas = diferenciaMinutos / 60.0
                val costo = horas * precioPorHora
                txtCostoTotal.text = "Costo total: Bs. %.2f".format(costo)
            } else {
                txtCostoTotal.text = "Costo total: Bs. 0.00"
            }
        }
    }
}