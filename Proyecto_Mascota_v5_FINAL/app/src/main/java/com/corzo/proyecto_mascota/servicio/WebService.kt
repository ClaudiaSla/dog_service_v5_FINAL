package com.corzo.proyecto_mascota.servicio

import com.corzo.proyecto_mascota.entidad.Adopcion
import com.corzo.proyecto_mascota.entidad.Avistamiento
import com.corzo.proyecto_mascota.entidad.Mascota
import com.corzo.proyecto_mascota.entidad.Perdido
import com.corzo.proyecto_mascota.entidad.PerdidoyPublicacion
import com.corzo.proyecto_mascota.entidad.Publicacion
import com.corzo.proyecto_mascota.entidad.Usuario
import com.google.gson.GsonBuilder
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

object AppConstantes{
    const val BASE_URL = "http://192.168.1.7:3000"
}

interface WebService {
    @POST("/registrar")
    fun registrarUsuario(@Body usuario: Usuario): Call<Void>
    @POST("/login")
    fun iniciarSesion(@Body params: Map<String, String>): Call<Usuario>
    @POST("/olvidaste-contrasena")
    fun olvidasteContrasena(@Body params: Map<String, String>): Call<Void>
    @POST("/valida-token")
    fun validarToken(@Body params: Map<String, String>): Call<UsuarioResponse>
    @POST("/restablecer-contrasena")
    fun restablecerContrasena(@Body params: Map<String, String>): Call<UsuarioResponse>
    @POST("/publicacion/agregar")
    suspend fun agregarPublicacion(@Body publicacion: Publicacion):Response<IdPublicacionResponse>
    @POST("/avistamiento/agregar")
    suspend fun agregarAvistamiento(@Body avistamiento: Avistamiento):Response<String>
    @POST("/adopcion/agregar")
    suspend fun agregarAdopcion(@Body adopcion: Adopcion):Response<String>
    @GET("/avistamientos")
    suspend fun obtenerAvistamientos():Response<AvistamientosResponse>
    @GET("/publicacion/{id}")
    suspend fun buscarPublicacion(@Path("id") id:Int):Response<Publicacion>
    @GET("/adopcion")
    suspend fun obtenerAdopcion():Response<AdopcionResponse>
    @POST("/perdido/agregar")
    suspend fun agregarPerdido(@Body perdido: Perdido):Response<String>
    @GET("/perdido")
    suspend fun obtenerPerdido():Response<PerdidoResponse>


}
data class IdPublicacionResponse(
    val id_publicacion: Int
)
data class AvistamientosResponse (
    @SerializedName("listaAvistamientos") var listaAvistamientos:ArrayList<Avistamiento>

)
data class AdopcionResponse (
    @SerializedName("listaAdopcion") var listaAdopcion:ArrayList<Adopcion>

)
data class PerdidoResponse (
    @SerializedName("listaPerdidos") var listaPerdidos:ArrayList<PerdidoyPublicacion>

)
object RetrofitClient {
    val webService:WebService by lazy {
        Retrofit.Builder()
            .baseUrl(AppConstantes.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(
                GsonBuilder().create()))
            .build().create(WebService::class.java)
    }
}
