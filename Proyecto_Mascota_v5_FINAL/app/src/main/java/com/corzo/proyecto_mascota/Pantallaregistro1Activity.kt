package com.corzo.proyecto_mascota

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.corzo.proyecto_mascota.entidad.Avistamiento
import com.corzo.proyecto_mascota.entidad.Publicacion
import com.corzo.proyecto_mascota.servicio.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale

class Pantallaregistro1Activity : AppCompatActivity() {
    private lateinit var titulo: EditText
    private lateinit var descripcion: EditText
    private lateinit var fecha: EditText
    private lateinit var lugar: EditText
    private lateinit var btnRegistro:Button
    var id:Int=0
    private val REQUEST_IMAGE_CAPTURE = 101
    private var currentPhotoPath: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pantallaregistro1)
        asignarReferencias()
    }

    private fun asignarReferencias() {
        titulo=findViewById(R.id.txttitulo)
        descripcion=findViewById(R.id.txtdescripcion)
        fecha=findViewById(R.id.txtFecha)
        lugar=findViewById(R.id.txtLugar)
        btnRegistro=findViewById(R.id.btnregistro)
        val btnTomarFoto = findViewById<Button>(R.id.btnfoto)
        btnTomarFoto.setOnClickListener {
            dispatchTakePictureIntent()
        }
        fecha.setOnClickListener {
            val datePicker=DatePickerFragment{dia,mes,anio->OnDateSelected(dia, mes+1, anio)}
            datePicker.show(supportFragmentManager,"datePicker")
        }
        btnRegistro.setOnClickListener {
            agregarPublicacion()
        }
    }

    private fun agregarPublicacion() {
        val publicacion= Publicacion(1,"","","",1,)
        publicacion.publicacion_titulo=titulo.text.toString()
        publicacion.publicacion_descripcion=descripcion.text.toString()
        publicacion.publicacion_fecha=obtenerFechaActual()
        val sharedPreferences = getSharedPreferences("mi_app_prefs", MODE_PRIVATE)
        val usuario_id = sharedPreferences.getInt("usuario_id", -1)
        publicacion.Usuarios_id_usuarios=usuario_id
        CoroutineScope(Dispatchers.IO).launch {
            val rpta=RetrofitClient.webService.agregarPublicacion(publicacion)
            runOnUiThread {
                if(rpta.isSuccessful){
                    val id_publi= rpta.body()?.id_publicacion
                    if (id_publi != null) {
                        id=id_publi
                        agregarAvistamiento()
                    }

                }
            }
        }
    }
    private fun agregarAvistamiento() {
        val avistamiento = Avistamiento("","",1,1)
        avistamiento.avistamiento_fecha=fecha.text.toString()
        avistamiento.avistamiento_lugar=lugar.text.toString()
        avistamiento.Publicacion_id_publicacion=id
        avistamiento.Ubicacion_ubicacion_id=1
        CoroutineScope(Dispatchers.IO).launch {
            val rpta=RetrofitClient.webService.agregarAvistamiento(avistamiento)
            runOnUiThread {
                if(rpta.isSuccessful){
                    mostrarMensaje(rpta.body().toString())

                }

            }

        }
        val intent = Intent(this, AvistamientosActivity::class.java)
        startActivity(intent)
    }
    fun OnDateSelected(dia:Int,mes:Int,anio:Int){
        fecha.setText("$anio/$mes/$dia")
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

    @SuppressLint("QueryPermissionsNeeded")
    private fun dispatchTakePictureIntent() {
        // Verificar permisos de cámara
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                REQUEST_IMAGE_CAPTURE
            )
        } else {
            // Crear un intent para capturar la imagen
            Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
                // Asegurarse de que hay una aplicación de cámara disponible para manejar el intent
                takePictureIntent.resolveActivity(packageManager)?.also {
                    // Crear un archivo donde se guardará la foto
                    val photoFile: File? = try {
                        createImageFile()
                    } catch (ex: IOException) {
                        // Error al crear el archivo
                        Toast.makeText(this, "Error creando el archivo de imagen", Toast.LENGTH_SHORT).show()
                        null
                    }

                    // Continuar si el archivo se creó exitosamente
                    photoFile?.also {
                        val photoURI: Uri = FileProvider.getUriForFile(
                            this,
                            "com.example.android.fileprovider",
                            it
                        )
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                    }
                }
            }
        }
    }


    private fun createImageFile(): File {
        // Crear un nombre de archivo único para la imagen
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)

        // Asegurarse de que el directorio de almacenamiento esté disponible
        if (storageDir == null) {
            Toast.makeText(this, "No se pudo acceder al almacenamiento externo", Toast.LENGTH_SHORT).show()
            throw IOException("No se pudo acceder al almacenamiento externo")
        }

        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Guardar la ruta del archivo
            currentPhotoPath = absolutePath
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent()
            } else {
                Toast.makeText(this, "Permiso de cámara denegado", Toast.LENGTH_SHORT).show()
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            // Mostrar la foto capturada en un ImageView (opcional)
            val imageView = findViewById<ImageView>(R.id.imageView7)
            currentPhotoPath?.let {
                val bitmap = BitmapFactory.decodeFile(it)
                imageView.setImageBitmap(bitmap)
            }
        }
    }



}
