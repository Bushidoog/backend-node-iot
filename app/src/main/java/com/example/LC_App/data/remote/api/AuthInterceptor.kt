package com.example.LC_App.data.remote.api

import android.content.Context
import com.example.LC_App.data.local.TokenStorage
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        // 1. Obtenemos el token guardado (Usamos runBlocking porque el interceptor no es suspend)
        val token = runBlocking {
            TokenStorage.getToken(context)
        }

        // 2. Tomamos la petición original
        val originalRequest = chain.request()

        // 3. Si no hay token, la mandamos tal cual (ej: Login o Registro)
        if (token.isNullOrEmpty()) {
            return chain.proceed(originalRequest)
        }

        // 4. Si hay token, creamos una nueva petición con el Header de autorización
        val newRequest = originalRequest.newBuilder()
            .addHeader("Authorization", "Bearer $token") // <--- AQUÍ ESTÁ LA MAGIA
            .build()

        return chain.proceed(newRequest)
    }
}