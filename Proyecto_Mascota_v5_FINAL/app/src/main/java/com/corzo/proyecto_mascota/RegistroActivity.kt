// RegistroActivity.kt
package com.corzo.proyecto_mascota

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.corzo.proyecto_mascota.entidad.Usuario
import com.corzo.proyecto_mascota.servicio.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegistroActivity : AppCompatActivity() {
    private lateinit var correo: EditText
    private lateinit var nombre: EditText
    private lateinit var apellido: EditText
    private lateinit var contrasena: EditText
    private lateinit var numero: EditText
    private lateinit var estado: Spinner
    private lateinit var boton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.registro)
        asignarRef()
    }

    private fun asignarRef() {
        correo = findViewById(R.id.txtCorreoUsuarios)
        nombre = findViewById(R.id.txtNombreUsuarios)
        apellido = findViewById(R.id.txtApellidoUsuarios)
        contrasena = findViewById(R.id.txtContrasenaUsuarios)
        numero = findViewById(R.id.txtCelularUsuarios)
        estado = findViewById(R.id.spinnerEstado)
        boton = findViewById(R.id.btnRegistrarUsuarios)

        // Configurar el Spinner para el estado
        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.estado_array,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        estado.adapter = adapter

        // Establecer el primer ítem como no seleccionable (Seleccione)
        estado.setSelection(0, false)

        boton.setOnClickListener {
            capturarDatos()
        }
    }

    private fun capturarDatos() {
        val correo2 = correo.text.toString()
        val nombre2 = nombre.text.toString()
        val apellido2 = apellido.text.toString()
        val contrasena2 = contrasena.text.toString()
        val numero2 = numero.text.toString()
        val estado2 = estado.selectedItem.toString()
        var valida = true

        if (correo2.isEmpty()) {
            valida = false
            correo.error = "Correo Obligatorio"
        }
        if (nombre2.isEmpty()) {
            valida = false
            nombre.error = "Nombre Obligatorio"
        }
        if (apellido2.isEmpty()) {
            valida = false
            apellido.error = "Apellido Obligatorio"
        }
        if (contrasena2.isEmpty()) {
            valida = false
            contrasena.error = "Contraseña Obligatoria"
        }
        if (numero2.isEmpty()) {
            valida = false
            numero.error = "Número Obligatorio"
        }
        if (estado2 == "Seleccione") {
            valida = false
            Toast.makeText(this, "Debe seleccionar un estado válido", Toast.LENGTH_SHORT).show()
        }

        if (valida) {
            val usuario = Usuario(
                id_usuarios = 1,
                nombre = nombre2,
                apellido = apellido2,
                correo = correo2,
                contrasena = contrasena2,
                numero = numero2,
                estado = estado2
            )
            registrarUsuario(usuario)
        }
    }

    private fun registrarUsuario(usuario: Usuario) {
        val call = RetrofitClient.webService.registrarUsuario(usuario)

        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@RegistroActivity, "Registro exitoso", Toast.LENGTH_SHORT).show()
                    // Redirigir a la actividad de inicio de sesión
                    val intent = Intent(this@RegistroActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish() // Opcional: finaliza la actividad actual para que no quede en el stack
                } else {
                    Toast.makeText(this@RegistroActivity, "Error en el registro", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@RegistroActivity, "Error al enviar datos", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
