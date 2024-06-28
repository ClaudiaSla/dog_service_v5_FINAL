package com.corzo.proyecto_mascota

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.corzo.proyecto_mascota.R
import com.corzo.proyecto_mascota.servicio.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OlvidasteContrasenaActivity : AppCompatActivity() {

    private lateinit var correoEditText: EditText
    private lateinit var btnEnviar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_olvidaste_contrasena)
        asignarReferencias()
    }

    private fun asignarReferencias() {
        correoEditText = findViewById(R.id.editTextCorreo)
        btnEnviar = findViewById(R.id.btnEnviar)

        ///Al dar click me redirige al enviarSolicitudOlvidasteContrasena
        btnEnviar.setOnClickListener {
            enviarSolicitudOlvidasteContrasena()
        }
    }

    private fun enviarSolicitudOlvidasteContrasena() {

        val correo = correoEditText.text.toString().trim()

        if (correo.isEmpty()) {
            Toast.makeText(this, "Ingrese su correo electrónico", Toast.LENGTH_SHORT).show()
            return
        }

        val params = mapOf("correo" to correo)

        // Llamar al método de Retrofit para enviar solicitud
        RetrofitClient.webService.olvidasteContrasena(params).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    // Procesar respuesta exitosa (correo enviado)
                    Toast.makeText(this@OlvidasteContrasenaActivity, "Correo enviado", Toast.LENGTH_SHORT).show()

                    val intent = Intent(this@OlvidasteContrasenaActivity, tokenActivity::class.java)
                    intent.putExtra("correo", correo)  // Donde 'correo' es el correo electrónico ingresado
                    startActivity(intent)

                } else {
                    // Procesar error
                    Toast.makeText(this@OlvidasteContrasenaActivity, "Error al enviar el correo", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                // Manejar error de red u otro tipo
                Toast.makeText(this@OlvidasteContrasenaActivity, "Error de red: " + t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }
}
