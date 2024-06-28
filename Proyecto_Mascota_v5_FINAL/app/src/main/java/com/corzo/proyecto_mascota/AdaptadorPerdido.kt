package com.corzo.proyecto_mascota

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.corzo.proyecto_mascota.entidad.PerdidoyPublicacion

class AdaptadorPerdido:RecyclerView.Adapter<AdaptadorPerdido.MiViewHolder>() {

    private var listaPerdido:ArrayList<PerdidoyPublicacion> = ArrayList()
    private lateinit var contexto: Context

    fun AgregarDPerdidos(items: ArrayList<PerdidoyPublicacion>){
        this.listaPerdido = items
    }
    fun setContext(context: Context)
    {
        this.contexto=context
    }

    class MiViewHolder(var view: View):RecyclerView.ViewHolder(view) {
        private var lblNombreP = view.findViewById<TextView>(R.id.lblPerdidoTitulo)
        private var lblLugarP = view.findViewById<TextView>(R.id.lblLugarPerdido)
        private var lblFechaP = view.findViewById<TextView>(R.id.lblFechaPerdido)
        var btnFPerdido=view.findViewById<ImageButton>(R.id.btnfilaPerdido)
        fun setValores(perdidoyPublicacion: PerdidoyPublicacion){
            lblNombreP.text = perdidoyPublicacion.publicacion_titulo
            lblLugarP.text = perdidoyPublicacion.perdido_lugar
            lblFechaP.text = perdidoyPublicacion.perdido_fecha
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)= MiViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.fila_perdido,parent,false)
    )

    override fun getItemCount(): Int {
        return listaPerdido.size
    }

    override fun onBindViewHolder(holder: MiViewHolder, position: Int) {
        val perdidoItem = listaPerdido[position]
        holder.setValores(perdidoItem)
        holder.btnFPerdido.setOnClickListener {
            val intent=Intent(contexto,perdido_vista::class.java)
            intent.putExtra("id_userp",perdidoItem.Usuarios_id_usuarios)
            intent.putExtra("fecha_publi",perdidoItem.publicacion_fecha)
            intent.putExtra("nombre_publi",perdidoItem.publicacion_titulo)
            intent.putExtra("lugar_perd",perdidoItem.perdido_lugar)
            intent.putExtra("fecha_perd",perdidoItem.perdido_fecha)
            intent.putExtra("num_perd",perdidoItem.perdido_contacto)
            intent.putExtra("desc_publi",perdidoItem.publicacion_descripcion)
            contexto.startActivity(intent)
        }    }


}