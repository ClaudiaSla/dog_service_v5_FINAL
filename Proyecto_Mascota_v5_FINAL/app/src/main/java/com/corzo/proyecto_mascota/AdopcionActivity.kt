package com.corzo.proyecto_mascota

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.corzo.proyecto_mascota.entidad.Adopcion
import com.corzo.proyecto_mascota.entidad.Mascota
import com.corzo.proyecto_mascota.entidad.Publicacion
import com.corzo.proyecto_mascota.servicio.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar

class AdopcionActivity : AppCompatActivity() {

    private lateinit var txtnombreMascota:EditText
    private lateinit var txtedadMascota:EditText
    private lateinit var txtrazaMascota:EditText
    private lateinit var txtvacunasMascota:EditText
    private lateinit var txtlugarEncontrado:EditText
    private lateinit var btnRegistrarMascota:Button
    var id:Int=0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_adopcion)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        asignarReferencias()
    }

    private fun asignarReferencias(){
        txtnombreMascota=findViewById(R.id.txtnombreMascota)
        txtedadMascota=findViewById(R.id.txtedadMascota)
        txtrazaMascota=findViewById(R.id.txtrazaMascota)
        txtvacunasMascota=findViewById(R.id.txtvacunasMascota)
        txtlugarEncontrado=findViewById(R.id.txtlugarEncontrado)
        btnRegistrarMascota=findViewById(R.id.btnRegistrarMascota)
        btnRegistrarMascota.setOnClickListener {
            agregarPublicacion()
        }

    }
    private fun agregarPublicacion() {
        val publicacion = Publicacion(1, "", "", "", 1,)
        publicacion.publicacion_titulo = "Adopcion de ${txtrazaMascota.text.toString()} en ${txtlugarEncontrado.text.toString()}"
        publicacion.publicacion_descripcion ="Se da en adopcion a ${txtnombreMascota.text.toString()} de raza: ${txtrazaMascota.text.toString()} "
        publicacion.publicacion_fecha = obtenerFechaActual()
        val sharedPreferences = getSharedPreferences("mi_app_prefs", MODE_PRIVATE)
        val usuario_id = sharedPreferences.getInt("usuario_id", -1)
        publicacion.Usuarios_id_usuarios=usuario_id
        CoroutineScope(Dispatchers.IO).launch {
            val rpta = RetrofitClient.webService.agregarPublicacion(publicacion)
            runOnUiThread {
                if (rpta.isSuccessful) {
                    val id_publi = rpta.body()?.id_publicacion
                    if (id_publi != null) {
                        id = id_publi
                        agregarAdopcion()
                    }

                }
            }
        }
    }
    private fun agregarAdopcion(){
        val nombre = txtnombreMascota.text.toString()
        val edad = txtedadMascota.text.toString()
        val raza = txtrazaMascota.text.toString()
        val vacunas = txtvacunasMascota.text.toString()
        val lugar = txtlugarEncontrado.text.toString()

        var valida = true

        if (nombre.isEmpty()) {
            valida = false
            txtnombreMascota.error = "Nombre Obligatorio"
        }
        if (edad.isEmpty()) {
            valida = false
            txtedadMascota.error = "Edad Obligatoria"
        }
        if (raza.isEmpty()) {
            valida = false
            txtrazaMascota.error = "Raza Obligatoria"
        }
        if (vacunas.isEmpty()) {
            valida = false
            txtvacunasMascota.error = "Vacunas Obligatorias"
        }
        if (lugar.isEmpty()) {
            valida = false
            txtlugarEncontrado.error = "Lugar Encontrado Obligatorio"
        }
        if (valida) {
            val adopcion= Adopcion(1,"","","","",1,1)
            adopcion.adopcion_edad=edad.toInt()
            adopcion.adopcion_raza=raza
            adopcion.adopcion_vacunas=vacunas
            adopcion.adopcion_nombre=nombre
            adopcion.adopcion_lugar=lugar
            adopcion.Publicacion_id_publicacion=id
            adopcion.Ubicacion_ubicacion_id=1
            CoroutineScope(Dispatchers.IO).launch {
                val rpta=RetrofitClient.webService.agregarAdopcion(adopcion)
                runOnUiThread {
                    if(rpta.isSuccessful){
                        mostrarMensaje(rpta.body().toString())

                    }
                }

            }
            val intent = Intent(this, mascota1::class.java)
            startActivity(intent)
        }
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
