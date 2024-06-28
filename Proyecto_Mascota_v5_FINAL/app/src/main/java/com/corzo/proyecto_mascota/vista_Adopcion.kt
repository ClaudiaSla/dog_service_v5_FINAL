package com.corzo.proyecto_mascota

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.corzo.proyecto_mascota.servicio.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class vista_Adopcion : AppCompatActivity() {
    private lateinit var txtnombre_A: TextView
    private lateinit var txtedad_A: TextView
    private lateinit var txtraza_A:TextView
    private lateinit var txtvacunas_A: TextView
    private lateinit var txtdescripcion_A: TextView
    private lateinit var txtFecha_A: TextView
    private lateinit var txtlugar_A:TextView
    private var id:Int=0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_vista_adopcion)
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
        txtnombre_A=findViewById(R.id.txtnombre_A)
        txtedad_A=findViewById(R.id.txtedad_A)
        txtraza_A=findViewById(R.id.txtraza_A)
        txtvacunas_A=findViewById(R.id.txtvacunas_A)
        txtlugar_A=findViewById(R.id.txtlugar_A)
        txtdescripcion_A=findViewById(R.id.txtdescripcion_A)
        txtFecha_A=findViewById(R.id.txtfecha_A)

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

                            txtdescripcion_A.setText(publicacion.publicacion_descripcion)
                            txtnombre_A.setText(publicacion.publicacion_titulo)
                            txtedad_A.setText(intent.getIntExtra("edad",-2).toString())
                            txtraza_A.setText(intent.getStringExtra("raza"))
                            txtvacunas_A.setText(intent.getStringExtra("vacunas"))
                            txtlugar_A.setText(intent.getStringExtra("lugar"))
                            txtFecha_A.setText(publicacion.publicacion_fecha)

                        }
                    }

                }

            }
        }

    }
}