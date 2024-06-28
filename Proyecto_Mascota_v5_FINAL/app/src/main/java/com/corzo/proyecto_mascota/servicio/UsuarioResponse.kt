package com.corzo.proyecto_mascota.servicio

import com.corzo.proyecto_mascota.entidad.Usuario
import com.google.gson.annotations.SerializedName

data class UsuarioResponse (
    @SerializedName ("listaUsuarios") var listaUsuarios:ArrayList<Usuario>,
)