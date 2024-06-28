package com.corzo.proyecto_mascota.entidad

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.corzo.proyecto_mascota.R
import com.corzo.proyecto_mascota.vista_Adopcion

class AdaptadorPersonalizado:RecyclerView.Adapter<AdaptadorPersonalizado.MiViewHolder>() {

    private var listaAdopcion:ArrayList<Adopcion> = ArrayList()
    private lateinit var contexto:Context


    fun agregarDatos(items: ArrayList<Adopcion>){
        this.listaAdopcion= items
    }

    fun setContext(context: Context)
    {
        this.contexto = context
    }

    class MiViewHolder(var view: View):RecyclerView.ViewHolder(view) {
            private var filaNombreM = view.findViewById<TextView>(R.id.filaNombreM)
            private var filaEdadM = view.findViewById<TextView>(R.id.filaEdadM)
            private var filaRazaM = view.findViewById<TextView>(R.id.filaRazaM)
            private var filaVacunasM = view.findViewById<TextView>(R.id.filaVacunasM)
            private var filaLugarM = view.findViewById<TextView>(R.id.filaLugarM)
            var btnAdopcion = view.findViewById<ImageButton>(R.id.btnFilaAdopcion)

            fun setValores(adopcion: Adopcion) {
                filaNombreM.text = adopcion.adopcion_nombre
                filaEdadM.text = adopcion.adopcion_edad.toString()
                filaRazaM.text = adopcion.adopcion_raza
                filaVacunasM.text = adopcion.adopcion_vacunas
                filaLugarM.text = adopcion.adopcion_lugar

            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = MiViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.fila,parent, false)

    )

    override fun onBindViewHolder(holder: AdaptadorPersonalizado.MiViewHolder, position: Int) {
        val adopcionItem = listaAdopcion[position]
        holder.setValores(adopcionItem)
        holder.btnAdopcion.setOnClickListener {
            val intent= Intent(contexto, vista_Adopcion::class.java)
            intent.putExtra("id",adopcionItem.Publicacion_id_publicacion)
            intent.putExtra("nombre",adopcionItem.adopcion_nombre)
            intent.putExtra("edad",adopcionItem.adopcion_edad)
            intent.putExtra("vacunas",adopcionItem.adopcion_vacunas)
            intent.putExtra("lugar",adopcionItem.adopcion_lugar)
            intent.putExtra("raza",adopcionItem.adopcion_raza)
            contexto.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return listaAdopcion.size
    }
}