package com.corzo.proyecto_mascota.entidad

data class Publicacion(
    var id_publicacion:Int,
    var publicacion_titulo:String,
    var publicacion_descripcion:String,
    var publicacion_fecha:String,
    var Usuarios_id_usuarios:Int
)