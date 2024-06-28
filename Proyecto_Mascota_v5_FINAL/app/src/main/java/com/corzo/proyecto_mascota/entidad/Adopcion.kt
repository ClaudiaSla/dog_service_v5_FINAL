package com.corzo.proyecto_mascota.entidad

data class Adopcion (
    var adopcion_edad:Int,
    var adopcion_raza:String,
    var adopcion_vacunas:String,
    var adopcion_nombre:String,
    var adopcion_lugar:String,
    var Publicacion_id_publicacion: Int,
    var Ubicacion_ubicacion_id: Int
)