package com.corzo.proyecto_mascota

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class perdido_vista : AppCompatActivity() {
    private lateinit var txtTituloVista:TextView
    private lateinit var txtLugarVista:TextView
    private lateinit var txtFechaDesaparecidoVista:TextView
    private lateinit var txtContactoVista:TextView
    private lateinit var txtDescripcionVista:TextView
    private lateinit var txtFechaPubliVista:TextView
    private lateinit var txtusuarioVista:TextView
    private lateinit var RetrocesoVistaPerdido: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perdido_vista)
        asignarReferencias()
        cargarDatos()
    }

    private fun asignarReferencias(){
        txtTituloVista = findViewById(R.id.txtTituloVista)
        txtLugarVista = findViewById(R.id.txtLugarVista)
        txtFechaDesaparecidoVista = findViewById(R.id.txtFechaDesaparecidoVista)
        txtContactoVista = findViewById(R.id.txtContactoVista)
        txtDescripcionVista = findViewById(R.id.txtDescripcionVista)
        txtFechaPubliVista = findViewById(R.id.txtFechaPubliVista)
        txtusuarioVista = findViewById(R.id.txtusuarioVista)
        RetrocesoVistaPerdido = findViewById(R.id.btnRetrocesoVistaPerdido)
        RetrocesoVistaPerdido.setOnClickListener {
            val intent = Intent(this, perdido_dashboard::class.java)
            startActivity(intent)
        }
    }

    private fun cargarDatos(){
        if(intent.hasExtra("id_userp")){
            txtTituloVista.setText(intent.getStringExtra("nombre_publi"))
            txtLugarVista.setText(intent.getStringExtra("lugar_perd"))
            txtFechaDesaparecidoVista.setText(intent.getStringExtra("fecha_perd"))
            txtContactoVista.setText(intent.getStringExtra("num_perd"))
            txtDescripcionVista.setText(intent.getStringExtra("desc_publi"))
            txtFechaPubliVista.setText(intent.getStringExtra("fecha_publi"))
            txtusuarioVista.setText(intent.getStringExtra("id_userp"))

        }
    }
}