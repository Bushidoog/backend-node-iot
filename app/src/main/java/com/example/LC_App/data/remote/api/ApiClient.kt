package com.example.LC_App.data.remote.api

import android.content.Context
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

    // ¡RECUERDA USAR TU IP ELÁSTICA DE AWS AQUÍ!
    private const val BASE_URL = "http://192.168.100.8:3000/"

    // Función para crear la API SIN seguridad (Login, Registro)
    val authApi: AuthApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AuthApi::class.java)
    }

    // Función para crear la API CON seguridad (Usuarios, Sensores)
    // Ahora pide 'context' para poder buscar el token
    fun getAuthenticatedUserApi(context: Context): UserApi {
        // 1. Creamos el cliente OkHttp con el Interceptor
        val client = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(context))
            .build()

        // 2. Creamos Retrofit usando ese cliente
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client) // <--- Conectamos el cliente seguro
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UserApi::class.java)
    }

    // Función para crear la API de IoT CON seguridad
    fun getAuthenticatedIotApi(context: Context): IotApi {
        val client = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(context)) // Le ponemos la "estampilla" del token
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(IotApi::class.java)
    }
}
