package com.corzo.proyecto_mascota

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.corzo.proyecto_mascota.servicio.RetrofitClient
import com.corzo.proyecto_mascota.servicio.UsuarioResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RestablecerContraseñaActivity : AppCompatActivity() {

    private lateinit var nuevaContrasena: EditText
    private lateinit var confirmarNuevaContrasena: EditText
    private lateinit var btnRestablecer: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restablecer_contrasena)
        asignarReferencias()
    }

    private fun asignarReferencias() {

        nuevaContrasena = findViewById(R.id.txtNuevaContrasena)
        confirmarNuevaContrasena = findViewById(R.id.txtConfirmarNuevaContrasena)
        btnRestablecer = findViewById(R.id.btnRestablecerContrasena)

        btnRestablecer.setOnClickListener {
            restablecerContrasena()
        }
    }

    private fun restablecerContrasena() {
        val nuevaContrasena = nuevaContrasena.text.toString()
        val confirmarContrasena  = confirmarNuevaContrasena.text.toString()

        if (nuevaContrasena.isEmpty() || confirmarContrasena.isEmpty()) {
            Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        if (nuevaContrasena != confirmarContrasena) {
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
            return
        }

        // Obtener el correo electrónico desde la actividad anterior
        val correo = intent.getStringExtra("correo")


        if (correo.isNullOrBlank()) {
            Toast.makeText(this, "Error: correo no encontrado", Toast.LENGTH_SHORT).show()
            return
        }

        // Crear un Map con los datos requeridos por el backend
        val params = mapOf("correo" to correo, "nuevaContrasena" to nuevaContrasena)

        // Verificar que se están pasando correctamente los datos
        println("Nueva Contraseña: $nuevaContrasena")


        RetrofitClient.webService.restablecerContrasena(params).enqueue(object : Callback<UsuarioResponse> {
            override fun onResponse(call: Call<UsuarioResponse>, response: Response<UsuarioResponse>) {
                if (response.isSuccessful) {
                    // Procesar respuesta exitosa
                    Toast.makeText(
                        this@RestablecerContraseñaActivity,
                        "Contraseña restablecida correctamente",
                        Toast.LENGTH_SHORT
                    ).show()
                    val intent = Intent(this@RestablecerContraseñaActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish() // Finalizar la actividad actual
                } else {
                    // Procesar error
                    Toast.makeText(
                        this@RestablecerContraseñaActivity,
                        "Error al restablecer la contraseña",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<UsuarioResponse>, t: Throwable) {
                // Manejar error de red u otro tipo
                Toast.makeText(
                    this@RestablecerContraseñaActivity,
                    "Error de red: " + t.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

}