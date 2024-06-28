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
import com.corzo.proyecto_mascota.entidad.AdaptadorPersonalizado
import com.corzo.proyecto_mascota.entidad.Adopcion
import com.corzo.proyecto_mascota.entidad.Avistamiento
import com.corzo.proyecto_mascota.servicio.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Adopcion1Activity : AppCompatActivity() {

    private lateinit var btnTodoAD:Button
    private lateinit var btnAvistamientosAD:Button
    private lateinit var btnadopcionAD:Button
    private lateinit var btn_homeAD:Button
    private lateinit var btn_addAD:Button
    private lateinit var btn_userAD:Button
    private var adaptador:AdaptadorPersonalizado = AdaptadorPersonalizado()
    private lateinit var rvAdopciones: RecyclerView
    private var listaAdopciones:ArrayList<Adopcion> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_adopcion1)
        asignarReferencias()
        cargarDatos()
    }
    private fun cargarDatos(){
        CoroutineScope(Dispatchers.IO).launch {
            val rpta = RetrofitClient.webService.obtenerAdopcion()
            runOnUiThread {
                if(rpta.isSuccessful){
                    listaAdopciones = rpta.body()!!.listaAdopcion
                    adaptador.agregarDatos(listaAdopciones)
                    mostrarDatos()
                }
            }
        }
    }
    private fun mostrarDatos(){
        rvAdopciones.adapter = adaptador
    }
    private fun asignarReferencias(){
        btnTodoAD = findViewById(R.id.btnTodoAD)
        btnAvistamientosAD = findViewById(R.id.btnAvistamientosAD)
        btnadopcionAD = findViewById(R.id.btnadopcionAD)
         btn_homeAD=findViewById(R.id.btn_homeAD)
         btn_addAD=findViewById(R.id.btn_addAD)
         btn_userAD=findViewById(R.id.btn_userAD)
        rvAdopciones=findViewById(R.id.rvAdopciones)
        rvAdopciones.layoutManager= LinearLayoutManager(this)
        adaptador.setContext(this)


        btnadopcionAD.setOnClickListener {
            val intent = Intent(this, Adopcion1Activity::class.java)
            startActivity(intent)
        }

        btnAvistamientosAD.setOnClickListener {
            val intent = Intent(this, AvistamientosActivity::class.java)
            startActivity(intent)
        }

        btnTodoAD.setOnClickListener {
            val intent = Intent(this, perdido_dashboard::class.java)
            startActivity(intent)
        }
         btn_homeAD.setOnClickListener {
            val intent = Intent(this, perdido_dashboard::class.java)
            startActivity(intent)
         }
         btn_addAD.setOnClickListener {
             val intent = Intent(this, AdopcionActivity::class.java)
             startActivity(intent)
          }

    }

}