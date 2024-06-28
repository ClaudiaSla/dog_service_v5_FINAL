package com.corzo.proyecto_mascota

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.os.Handler
import android.os.Looper
class inicio_sesion : AppCompatActivity() {

    private lateinit var txtnombres:TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicio_sesion)
        Handler(Looper.getMainLooper()).postDelayed({
            // Creamos un Intent para cambiar a la nueva actividad (MainActivity en este caso)
            val intent = Intent(this, perdido_dashboard::class.java)
            startActivity(intent)
            finish() // Finalizamos esta actividad para que no se pueda volver atrás
        }, 2000) // 2000 milisegundos = 2 segundos
        txtnombres=findViewById(R.id.txtUsuarioLogin)
        val sharedPreferences = getSharedPreferences("mi_app_prefs", MODE_PRIVATE)
        val usuarioNombre = sharedPreferences.getString("nombre", null)
        val usuarioApellido = sharedPreferences.getString("apellido", null)
        txtnombres.text="$usuarioNombre $usuarioApellido"
    }
}






