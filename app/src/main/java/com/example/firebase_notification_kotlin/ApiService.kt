package com.example.firebase_notification_kotlin

import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("posts/")
    fun getAllPosts(): Call<List<POST>>

    @GET("posts/{id}")
    fun getPostById(@Path("id") id: Int): Call<POST>

    @POST("posts/{id}")
    fun editPostById(@Path("id") id: Int): Call<POST>


    @POST("apiNotification/AltaUsuario.php")
    fun altaUsuario(@Body usuario: Usuario ): Call<UsuarioResponse>

    @POST("firebase/Usuario_x_Canales.php")
    fun suscribirseCanal(@Body canalPorUsuario: CanalPorUsuario): Call<CanalPorUsuarioResponse>

    @GET("firebase/ObtenerCanales.php")
    fun getCanales(): Call<List<Canal>>
}