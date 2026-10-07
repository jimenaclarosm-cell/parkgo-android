package com.example.reservagaraje

data class Garaje(
    val id: String = "",
    val nombre: String = "",
    val direccion: String = "",
    val latitud: Double = 0.0,
    val longitud: Double = 0.0,
    val precioHora: Int = 0,
    val capacidadTotal: Int = 0,
    val espaciosDisponibles: Int = 0,
    val horario: String = ""
)