package com.corzo.proyecto_mascota

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.corzo.proyecto_mascota.servicio.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class vista_avistamiento : AppCompatActivity() {
    private lateinit var txtTitulo: TextView
    private lateinit var txtDescripcion:TextView
    private lateinit var txtFechaPublicacion:TextView
    private lateinit var txtFechaAvistamiento:TextView
    private lateinit var txtLugarAvistamiento:TextView
    private lateinit var btnRetroceso:ImageButton
    private var id:Int=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_vista_avistamiento)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        asignarReferencias()
        cargarDatos()
    }
    private fun asignarReferencias()
    {
        txtTitulo=findViewById(R.id.avistamientoTitulo)
        txtDescripcion=findViewById(R.id.avistamientoDesc)
        txtFechaPublicacion=findViewById(R.id.PublicacionAvistamientoFecha)
        txtFechaAvistamiento=findViewById(R.id.txtFechaAvistamiento)
        txtLugarAvistamiento=findViewById(R.id.txtLugarAvistamiento)
        btnRetroceso=findViewById(R.id.btnRetrocesoVistaAvistamiento)
        btnRetroceso.setOnClickListener {
            val intent = Intent(this, AvistamientosActivity::class.java)
            startActivity(intent)
        }
    }
    private fun cargarDatos(){

        if(intent.hasExtra("id")){
            id=intent.getIntExtra("id",0)
            CoroutineScope(Dispatchers.IO).launch {
                val rpta= RetrofitClient.webService.buscarPublicacion(id)
                runOnUiThread {
                    if(rpta.isSuccessful){
                        val publicacion=rpta.body()
                        if (publicacion != null) {
                            txtTitulo.setText(publicacion.publicacion_titulo.toString())
                            txtDescripcion.setText(publicacion.publicacion_descripcion)
                            txtFechaPublicacion.setText(publicacion.publicacion_fecha)
                            txtFechaAvistamiento.setText(intent.getStringExtra("fecha"))
                            txtLugarAvistamiento.setText(intent.getStringExtra("lugar"))
                        }
                    }

                }

            }
        }

    }
}