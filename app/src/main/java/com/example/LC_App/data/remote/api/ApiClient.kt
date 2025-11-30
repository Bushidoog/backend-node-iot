package com.example.LC_App.data.remote.api


import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

    // URL DEL SERVIDOR NODE
    private const val BASE_URL = "http://10.0.2.2:3000/"

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    // Aquí conectamos las interfaces de Retrofit
    val authApi: AuthApi = retrofit.create(AuthApi::class.java)
    val userApi: UserApi = retrofit.create(UserApi::class.java)
    val iotApi: IotApi = retrofit.create(IotApi::class.java)
}
