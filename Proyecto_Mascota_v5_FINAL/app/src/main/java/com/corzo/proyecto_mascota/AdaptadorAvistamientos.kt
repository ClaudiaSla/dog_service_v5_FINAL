package com.corzo.proyecto_mascota

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.corzo.proyecto_mascota.entidad.Avistamiento

class AdaptadorAvistamientos:RecyclerView.Adapter<AdaptadorAvistamientos.MiViewHolder>() {

    private var listaAvistamientos:ArrayList<Avistamiento> = ArrayList()
    private lateinit var contexto:Context


    fun agregarDatos(items: ArrayList<Avistamiento>){
        this.listaAvistamientos = items
    }
    fun setContext(context: Context)
    {
        this.contexto=context
    }

    class MiViewHolder(var view: View):RecyclerView.ViewHolder(view) {
        private var filaFehca = view.findViewById<TextView>(R.id.filaFechaAvistamiento)
        private var filaLugar = view.findViewById<TextView>(R.id.filaLugarAvistamiento)
        var btnAvistamiento=view.findViewById<ImageButton>(R.id.btnFila)
        fun setValores(avistamiento: Avistamiento){
            filaFehca.text = avistamiento.avistamiento_fecha
            filaLugar.text = avistamiento.avistamiento_lugar
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup,viewType: Int) = MiViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.fila_avistamiento,parent,false)
    )

    override fun onBindViewHolder(holder: AdaptadorAvistamientos.MiViewHolder, position: Int) {
        val avistamientoItem = listaAvistamientos[position]
        holder.setValores(avistamientoItem)
        holder.btnAvistamiento.setOnClickListener {
            val intent=Intent(contexto,vista_avistamiento::class.java)
            intent.putExtra("id",avistamientoItem.Publicacion_id_publicacion)
            intent.putExtra("fecha",avistamientoItem.avistamiento_fecha)
            intent.putExtra("lugar",avistamientoItem.avistamiento_lugar)
            contexto.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return listaAvistamientos.size
    }
}