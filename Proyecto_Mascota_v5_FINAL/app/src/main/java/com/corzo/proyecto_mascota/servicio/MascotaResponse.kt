package com.corzo.proyecto_mascota.servicio

import com.corzo.proyecto_mascota.entidad.Mascota
import com.google.gson.annotations.SerializedName

data class MascotaResponse (
    @SerializedName("listaMascotas") var listaMascotas:ArrayList<Mascota>
)