package com.corzo.proyecto_mascota

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.corzo.proyecto_mascota.entidad.AdaptadorPersonalizado
import com.corzo.proyecto_mascota.entidad.Adopcion
import com.corzo.proyecto_mascota.entidad.Mascota
import com.corzo.proyecto_mascota.servicio.RetrofitClient
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class mascota1 : AppCompatActivity() {
    private lateinit var btnNuevoMascota:Button
    private lateinit var rvMascota:RecyclerView
    private lateinit var btnTodoA:Button
    private lateinit var btnAvistamientosA: Button
    //SE AÑADE EL ADAPTADOR PERSONALIZADO ***//
    private var adaptador:AdaptadorPersonalizado = AdaptadorPersonalizado()
    //LISTA DE LA MASCOTA ***//
    private  var listaAdopcion:ArrayList<Adopcion> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_mascota1)
        asignarReferencias()

       cargarDatos()
    }

    //CARGAR DATOS DE LA MASCOTA ***//

    private fun cargarDatos(){
        CoroutineScope(Dispatchers.IO).launch {
            val rpta = RetrofitClient.webService.obtenerAdopcion()
            runOnUiThread {
                if(rpta.isSuccessful){
                    listaAdopcion= rpta.body()!!.listaAdopcion
                    adaptador.agregarDatos(listaAdopcion)
                    mostrarDatos()
                }
            }
        }
    }






    private fun mostrarDatos(){
        rvMascota.adapter = adaptador
    }
    //FUNCION ***//
    private fun asignarReferencias(){
        rvMascota = findViewById(R.id.rvMascotas)
        btnNuevoMascota = findViewById(R.id.btn_NuevaMascota)
        rvMascota.layoutManager = LinearLayoutManager(this)
        btnTodoA=findViewById(R.id.btnTodoA)
        btnAvistamientosA = findViewById(R.id.btnAvistamientosA)
        btnNuevoMascota.setOnClickListener {
            val intent = Intent(this, AdopcionActivity::class.java)
            startActivity(intent)
        }

        btnTodoA.setOnClickListener {
            val intent = Intent(this, perdido_dashboard::class.java)
            startActivity(intent)
        }

        btnAvistamientosA.setOnClickListener {
            val intent = Intent(this, AvistamientosActivity::class.java)
            startActivity(intent)
        }

    }

}