package com.corzo.proyecto_mascota

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.corzo.proyecto_mascota.entidad.Avistamiento
import com.corzo.proyecto_mascota.entidad.Perdido
import com.corzo.proyecto_mascota.entidad.PerdidoyPublicacion
import com.corzo.proyecto_mascota.servicio.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class perdido_dashboard : AppCompatActivity() {

    private lateinit var btnPerdidosDashboard: Button
    private lateinit var btnAvistamientosP: Button
    private lateinit var btnadopcionP: Button
    private lateinit var btn_homeP: Button
    private lateinit var btnNuevoPerdido: Button
    private lateinit var btn_userP: Button
    private lateinit var rvPerdido: RecyclerView

    private var AdaptadorPerdido:AdaptadorPerdido= AdaptadorPerdido()
    private var listaPerdido:ArrayList<PerdidoyPublicacion> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_perdido_dashboard)
        asignarReferencias()
        cargarDatos()
    }
    private fun cargarDatos(){
        CoroutineScope(Dispatchers.IO).launch {
            val rpta = RetrofitClient.webService.obtenerPerdido()
            runOnUiThread {
                if(rpta.isSuccessful){
                    listaPerdido = rpta.body()!!.listaPerdidos
                    AdaptadorPerdido.AgregarDPerdidos(listaPerdido)
                    mostrarDatos()
                }
            }
        }
    }

    private fun mostrarDatos(){
        rvPerdido.adapter = AdaptadorPerdido
    }

    private fun asignarReferencias(){

        btnPerdidosDashboard= findViewById(R.id.btnPerdidosDashboard)
        btnAvistamientosP = findViewById(R.id.btnAvistamientosP)
        btnadopcionP = findViewById(R.id.btnadopcionP)
        btn_homeP=findViewById(R.id.btn_homeP)
        btnNuevoPerdido=findViewById(R.id.btnNuevoPerdido)
        btn_userP=findViewById(R.id.btn_userP)
        rvPerdido=findViewById(R.id.rvPerdido)
        rvPerdido.layoutManager= LinearLayoutManager(this)
        AdaptadorPerdido.setContext(this)

        btnadopcionP.setOnClickListener {
            val intent = Intent(this, Adopcion1Activity::class.java)
            startActivity(intent)
        }

        btnNuevoPerdido.setOnClickListener {
            val intent = Intent(this, perdido_registro::class.java)
            startActivity(intent)
        }
        btnAvistamientosP.setOnClickListener {
            val intent = Intent(this, AvistamientosActivity::class.java)
            startActivity(intent)
        }
        btn_homeP.setOnClickListener {
            val intent = Intent(this, perdido_dashboard::class.java)
            startActivity(intent)
        }
        //btn_homeA.setOnClickListener {
        //    val intent = Intent(this, Inicio::class.java)
        //     startActivity(intent)
        // }
    }
}