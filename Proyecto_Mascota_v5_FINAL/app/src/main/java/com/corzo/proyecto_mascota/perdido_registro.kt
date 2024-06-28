package com.corzo.proyecto_mascota

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.corzo.proyecto_mascota.entidad.Avistamiento
import com.corzo.proyecto_mascota.entidad.Perdido
import com.corzo.proyecto_mascota.entidad.Publicacion
import com.corzo.proyecto_mascota.servicio.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

class perdido_registro : AppCompatActivity() {
    private lateinit var Nombre_Perdido: EditText
    private lateinit var Lugar_Perdido: EditText
    private lateinit var Fecha_Perdido: EditText
    private lateinit var Contacto_Perdido: EditText
    private lateinit var Descripcion_Perdido:EditText
    private lateinit var RetrocesoPerdido:ImageButton
    private lateinit var Publicar_Perdido:Button
    var id:Int=0
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perdido_registro)
        asignarReferencias()

    }
    private fun asignarReferencias(){

        Nombre_Perdido =findViewById(R.id.txtNombre_Perdido)
        Lugar_Perdido=findViewById(R.id.txtLugar_Perdido)
        Fecha_Perdido=findViewById(R.id.txtFecha_Perdido)
        Contacto_Perdido=findViewById(R.id.txtContacto_Perdido)
        Descripcion_Perdido=findViewById(R.id.txtDescripcion_Perdido)
        RetrocesoPerdido=findViewById(R.id.btnRetrocesoPerdido)
        Publicar_Perdido=findViewById(R.id.btnPublicar_Perdido)
        Publicar_Perdido.setOnClickListener {
            agregarPublicacion()
        }

        RetrocesoPerdido.setOnClickListener {
            val intent = Intent(this, perdido_dashboard::class.java)
            startActivity(intent)
        }
        Fecha_Perdido.setOnClickListener {
            val datePickers= DatePickerFragment{ dia, mes, anio-> OnDateSelected(dia, mes+1, anio)}
            datePickers.show(supportFragmentManager,"datePickers")
        }
    }

    private fun agregarPublicacion(){
        val publicacionperdido= Publicacion(1,"","","",1,)
        publicacionperdido.publicacion_titulo=Nombre_Perdido.text.toString()
        publicacionperdido.publicacion_descripcion=Descripcion_Perdido.text.toString()
        publicacionperdido.publicacion_fecha=obtenerFechaActual()
        val sharedPreferences = getSharedPreferences("mi_app_prefs", MODE_PRIVATE)
        val usuario_id = sharedPreferences.getInt("usuario_id", -1)
        publicacionperdido.Usuarios_id_usuarios=usuario_id
        CoroutineScope(Dispatchers.IO).launch {
            val rpta= RetrofitClient.webService.agregarPublicacion(publicacionperdido)
            runOnUiThread {
                if(rpta.isSuccessful){
                    val id_publi= rpta.body()?.id_publicacion
                    if (id_publi != null) {
                        id=id_publi
                        agregarPerdido()
                    }

                }
            }
        }
    }

    private fun agregarPerdido(){
        val perdido = Perdido("","","",1, 1)
        perdido.perdido_fecha=Fecha_Perdido.text.toString()
        perdido.perdido_lugar=Lugar_Perdido.text.toString()
        perdido.perdido_contacto=Contacto_Perdido.text.toString()
        perdido.Publicacion_id_publicacion=id
        perdido.Ubicacion_ubicacion_id=1
        CoroutineScope(Dispatchers.IO).launch {
            val rpta=RetrofitClient.webService.agregarPerdido(perdido)
            runOnUiThread {
                if(rpta.isSuccessful){
                    mostrarMensaje(rpta.body().toString())

                }
            }
        }
        val intent = Intent(this, perdido_dashboard::class.java)
        startActivity(intent)
    }


    fun OnDateSelected(dia:Int,mes:Int,anio:Int){
        Fecha_Perdido.setText("$anio/$mes/$dia")
    }
    fun obtenerFechaActual(): String {
        val formatoFecha = SimpleDateFormat("yyyy/MM/dd")
        val fechaActual = Calendar.getInstance().time
        return formatoFecha.format(fechaActual)
    }
    private fun mostrarMensaje(mensaje:String){
        val ventana = AlertDialog.Builder(this)
        ventana.setTitle("Información")
        ventana.setMessage(mensaje)
        ventana.setPositiveButton("Aceptar", DialogInterface.OnClickListener { dialog, which ->
        })
        ventana.create().show()
    }


}