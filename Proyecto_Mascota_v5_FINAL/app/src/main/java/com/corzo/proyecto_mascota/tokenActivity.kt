package com.corzo.proyecto_mascota

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.corzo.proyecto_mascota.servicio.RetrofitClient
import com.corzo.proyecto_mascota.servicio.UsuarioResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class tokenActivity : AppCompatActivity() {

    private lateinit var tokenEditText: EditText
    private lateinit var btnEnviar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_token)
        asignarReferencias()
    }

    private fun asignarReferencias() {
        tokenEditText = findViewById(R.id.tokenEditText)
        btnEnviar = findViewById(R.id.btnenviar)

        btnEnviar.setOnClickListener {
            validarToken()
        }
    }

    private fun validarToken() {
        val token = tokenEditText.text.toString().trim()

        if (token.isEmpty()) {
            Toast.makeText(this, "Por favor, ingrese el token", Toast.LENGTH_SHORT).show()
            return
        }

        // Obtener el correo electrónico desde la actividad anterior
        val correo = intent.getStringExtra("correo")

        // Crear un Map con el token y correo para enviar al servidor
        val params = mapOf("correo" to correo!!, "token" to token)

        // Enviar los datos al servidor utilizando Retrofit
        RetrofitClient.webService.validarToken(params).enqueue(object : Callback<UsuarioResponse> {
            override fun onResponse(call: Call<UsuarioResponse>, response: Response<UsuarioResponse>) {
                if (response.isSuccessful) {
                    // Verificación exitosa del token
                    Toast.makeText(this@tokenActivity, "Token validado correctamente", Toast.LENGTH_SHORT).show()

                    // Redirigir a RestablecerContrasenaActivity
                    val intent = Intent(this@tokenActivity, RestablecerContraseñaActivity::class.java)
                    intent.putExtra("correo", correo)
                    startActivity(intent)
                    finish() // Finalizar la actividad actual
                } else {
                    // Error en la validación del token
                    Toast.makeText(this@tokenActivity, "Error al validar el token", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UsuarioResponse>, t: Throwable) {
                // Error en la solicitud
                Toast.makeText(this@tokenActivity, "Error de red, por favor, inténtelo de nuevo", Toast.LENGTH_SHORT).show()
            }
        })
    }
}