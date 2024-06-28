package com.corzo.proyecto_mascota

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.corzo.proyecto_mascota.entidad.Avistamiento
import com.corzo.proyecto_mascota.servicio.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AvistamientosActivity : AppCompatActivity() {

    private lateinit var btnTodoA: Button
    private lateinit var btnAvistamientosA: Button
    private lateinit var btnadopcionA:Button
     private lateinit var btn_homeA:Button
     private lateinit var btn_addA:Button
     private lateinit var btn_userA:Button
    private lateinit var rvAvistamientos: RecyclerView
    private var adaptador:AdaptadorAvistamientos= AdaptadorAvistamientos()
    private var listaAvistamientos:ArrayList<Avistamiento> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_avistamientos)
        asignarReferencias()
        cargarDatos()
    }
    private fun cargarDatos(){
        CoroutineScope(Dispatchers.IO).launch {
            val rpta = RetrofitClient.webService.obtenerAvistamientos()
            runOnUiThread {
                if(rpta.isSuccessful){
                    listaAvistamientos = rpta.body()!!.listaAvistamientos
                    adaptador.agregarDatos(listaAvistamientos)
                    mostrarDatos()
                }
            }
        }
    }

    private fun mostrarDatos(){
        rvAvistamientos.adapter = adaptador
    }

    private fun asignarReferencias(){

        btnTodoA= findViewById(R.id.btnTodoA)
        btnAvistamientosA = findViewById(R.id.btnAvistamientosA)
        btnadopcionA = findViewById(R.id.btnadopcionA)
        btn_homeA=findViewById(R.id.btn_homeA)
        btn_addA=findViewById(R.id.btn_NuevaMascota)
        btn_userA=findViewById(R.id.btn_userA)
        rvAvistamientos=findViewById(R.id.rvMascotas)
        rvAvistamientos.layoutManager=LinearLayoutManager(this)
        adaptador.setContext(this)

        btnadopcionA.setOnClickListener {
            val intent = Intent(this, Adopcion1Activity::class.java)
            startActivity(intent)
        }

        btn_addA.setOnClickListener {
            val intent = Intent(this, Pantallaregistro1Activity::class.java)
            startActivity(intent)
          }
        btnTodoA.setOnClickListener {
            val intent = Intent(this, perdido_dashboard::class.java)
            startActivity(intent)
        }
        btn_homeA.setOnClickListener {
            val intent = Intent(this, perdido_dashboard::class.java)
            startActivity(intent)
        }



        //btn_homeA.setOnClickListener {
        //    val intent = Intent(this, Inicio::class.java)
        //     startActivity(intent)
        // }
    }
}