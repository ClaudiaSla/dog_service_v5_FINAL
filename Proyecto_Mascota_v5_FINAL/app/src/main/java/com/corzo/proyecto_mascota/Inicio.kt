package com.corzo.proyecto_mascota

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class Inicio : AppCompatActivity() {
    private lateinit var btnTodo: Button
    private lateinit var btnadopcion: Button
    private lateinit var btnAvistamientos:Button
    private lateinit var btn_homeI:Button
    private lateinit var btn_addI:Button
     private lateinit var btn_userI:Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_inicio)
        asignarReferencias()
    }

    private fun asignarReferencias() {
        btnTodo = findViewById(R.id.btnTodo)
        btnadopcion = findViewById(R.id.btnadopcion)
        btnAvistamientos=findViewById(R.id.btnAvistamientos)
        btn_homeI=findViewById(R.id.btn_homeI)
        btn_addI=findViewById(R.id.btn_addI)
         btn_userI=findViewById(R.id.btn_userI)

        btnadopcion.setOnClickListener {
            val intent = Intent(this, Adopcion1Activity::class.java)
            startActivity(intent)
        }
        btnAvistamientos.setOnClickListener {
            val intent = Intent(this, AvistamientosActivity::class.java)
            startActivity(intent)
        }
        btnTodo.setOnClickListener {
            val intent = Intent(this, perdido_dashboard::class.java)
            startActivity(intent)
        }
        btn_homeI.setOnClickListener {
            val intent = Intent(this, perdido_dashboard::class.java)
            startActivity(intent)
        }

        //btn_homeI.setOnClickListener {
        //val intent = Intent(this, Inicio::class.java)
        // startActivity(intent)
        //  }
    }

}