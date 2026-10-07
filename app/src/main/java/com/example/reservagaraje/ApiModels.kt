package com.example.reservagaraje

import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

// Modelo de respuesta compartido
data class LoginResponse(
    val access_token: String,
    val token_type: String,
    val expires_in: Int
)

// Interfaz de la API para login
interface LoginApiService {
    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): retrofit2.Call<LoginResponse>
}

// Interfaz de la API para registro
interface RegisterApiService {
    @FormUrlEncoded
    @POST("register")
    fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): retrofit2.Call<LoginResponse>
}