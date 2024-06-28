package com.corzo.proyecto_mascota.entidad

data class Avistamiento(
    var avistamiento_fecha:String,
    var avistamiento_lugar:String,
    var Publicacion_id_publicacion:Int,
    var Ubicacion_ubicacion_id:Int
)