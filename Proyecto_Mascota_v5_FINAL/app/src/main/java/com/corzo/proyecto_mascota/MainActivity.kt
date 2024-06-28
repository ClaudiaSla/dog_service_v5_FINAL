package com.corzo.proyecto_mascota

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.media.MediaPlayer
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.corzo.proyecto_mascota.entidad.Usuario
import com.corzo.proyecto_mascota.servicio.RetrofitClient
import com.corzo.proyecto_mascota.servicio.UsuarioResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var correoEditText: EditText
    private lateinit var contrasenaEditText: EditText
    private lateinit var olvidoContrasenaTextView: TextView
    private lateinit var loginButton: Button
    private lateinit var btnRegistrar: Button
    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        Thread.sleep(2000) //agregarle segundos al momento que se ejecuta el APP
        super.onCreate(savedInstanceState)
        // Inicializar el reproductor de música con el archivo de música deseado
        mediaPlayer = MediaPlayer.create(this, R.raw.cancion)
        // Reproducir música
        mediaPlayer.start()

        setContentView(R.layout.activity_main)
        asignarReferencias()
    }

    override fun onDestroy() {
        super.onDestroy()
        // Liberar recursos del reproductor de música cuando la actividad se destruye
        mediaPlayer.release()
    }

    private fun asignarReferencias() {
        correoEditText = findViewById(R.id.txtCorreo)
        contrasenaEditText = findViewById(R.id.txtContrasena)
        olvidoContrasenaTextView = findViewById(R.id.txtOlvidoContraseña)
        loginButton = findViewById(R.id.btnIniciarSesion)
        btnRegistrar = findViewById(R.id.btnRegistrarse)

        //Al dar click me redirige al iniciarSesion
        loginButton.setOnClickListener {
            iniciarSesion()
        }
        //Al dar click me redirige al RegistroActivity
        btnRegistrar.setOnClickListener {
            val intent = Intent(this, RegistroActivity::class.java)
            startActivity(intent)
        }
        //Al dar click me redirige al OlvidasteContrasenaActivity
        olvidoContrasenaTextView.setOnClickListener {
            val intent = Intent(this, OlvidasteContrasenaActivity::class.java)
            startActivity(intent)
        }

    }

    private fun iniciarSesion() {
        val correo = correoEditText.text.toString()
        val contrasena = contrasenaEditText.text.toString()

        // Validación básica de campos vacíos
        if (correo.isEmpty() || contrasena.isEmpty()) {
            Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        // Crear un Map con los datos de inicio de sesión
        val params = mapOf("correo" to correo, "contrasena" to contrasena)

        // Envía los datos de inicio de sesión al servidor
        val call = RetrofitClient.webService.iniciarSesion(params)

        call.enqueue(object : Callback<Usuario> {
            override fun onResponse(call: Call<Usuario>, response: Response<Usuario>) {
                if (response.isSuccessful) {
                    val usuarioResponse = response.body()
                    if (usuarioResponse != null) {
                        // Inicio de sesión exitoso
                        Toast.makeText(this@MainActivity, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show()
                        // Aquí podrías guardar el usuario en SharedPreferences o similar
                        val sharedPreferences = getSharedPreferences("mi_app_prefs", MODE_PRIVATE)
                        val editor = sharedPreferences.edit()
                        editor.putInt("usuario_id", usuarioResponse.id_usuarios)
                        editor.putString("nombre", usuarioResponse.nombre)
                        editor.putString("apellido", usuarioResponse.apellido)
                        editor.putString("numero", usuarioResponse.numero)
                        editor.apply()

                        val intent = Intent(this@MainActivity, inicio_sesion::class.java)
                        startActivity(intent)
                    } else {
                        // Respuesta nula del servidor
                        Toast.makeText(this@MainActivity, "Error en la respuesta del servidor", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    // El servidor devolvió un error
                    Toast.makeText(this@MainActivity, "Credenciales incorrectas", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Usuario>, t: Throwable) {
                // Error en la solicitud
                Toast.makeText(this@MainActivity, "Error de red, por favor, inténtelo de nuevo", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
