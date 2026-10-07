package com.example.reservagaraje

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class GarajeAdapter(private val garajes: List<Garaje>) :
    RecyclerView.Adapter<GarajeAdapter.GarajeViewHolder>() {

    class GarajeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtNombre: TextView = itemView.findViewById(R.id.txtNombre)
        val txtDireccion: TextView = itemView.findViewById(R.id.txtDireccion)
        val txtPrecio: TextView = itemView.findViewById(R.id.txtPrecio)
        val txtDisponibilidad: TextView = itemView.findViewById(R.id.txtDisponibilidad)
        val btnReservar: Button = itemView.findViewById(R.id.btnReservar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GarajeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_garaje, parent, false)
        return GarajeViewHolder(view)
    }

    override fun onBindViewHolder(holder: GarajeViewHolder, position: Int) {
        val garaje = garajes[position]

        holder.txtNombre.text = garaje.nombre
        holder.txtDireccion.text = garaje.direccion
        holder.txtPrecio.text = "Bs. ${garaje.precioHora} / hora"
        holder.txtDisponibilidad.text = "${garaje.espaciosDisponibles} espacios"

        holder.btnReservar.setOnClickListener {
            Toast.makeText(holder.itemView.context,
                "Reservando ${garaje.nombre}", Toast.LENGTH_SHORT).show()
            // Aquí después iremos a la pantalla de reserva
        }
    }

    override fun getItemCount() = garajes.size
}
