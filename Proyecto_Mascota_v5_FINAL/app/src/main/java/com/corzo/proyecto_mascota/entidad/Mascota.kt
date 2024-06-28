package com.corzo.proyecto_mascota.entidad

data class Mascota (
    var mascota_id:Int = 0,
    var mascota_nombre:String,
    var mascota_edad:String,
    var mascota_raza:String,
    var mascota_vacunas:String,
    var mascota_lugar:String,
    val usuario: Usuario
    )