package com.example.iot_vicente.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.iot_vicente.viewmodel.AuthViewModel

@Composable
fun RecoverPasswordScreen(
    nav: NavController,
    vm: AuthViewModel = viewModel()
) {
    // Estados de flujo
    // 1: Ingresar Email
    // 2: Ingresar Código y Nueva Contraseña
    var step by remember { mutableStateOf(1) }

    // Campos
    var email by remember { mutableStateOf("") }
    var code by remember { mutableStateOf("") }
    var newPass by remember { mutableStateOf("") }
    var confirmPass by remember { mutableStateOf("") }

    // UI State
    var message by remember { mutableStateOf<String?>(null) }
    var isError by remember { mutableStateOf(false) }
    var loading by remember { mutableStateOf(false) }

    fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun passwordValidationError(pwd: String): String? {
        val errors = mutableListOf<String>()
        if (pwd.length < 8) errors.add("al menos 8 caracteres")
        if (!pwd.any { it.isUpperCase() }) errors.add("una mayúscula")
        if (!pwd.any { it.isLowerCase() }) errors.add("una minúscula")
        if (!pwd.any { it.isDigit() }) errors.add("un número")
        if (!pwd.any { !it.isLetterOrDigit() }) errors.add("un carácter especial")

        return if (errors.isEmpty()) null
        else "Falta: ${errors.joinToString(", ")}."
    }

    // --- PASO 1: Enviar código ---
    fun onSendCode() {
        message = null
        if (email.isBlank()) {
            message = "Email vacío."
            isError = true
            return
        }
        if (!isValidEmail(email)) {
            message = "Formato inválido."
            isError = true
            return
        }

        loading = true
        vm.forgotPassword(
            email = email,
            onSuccess = {
                loading = false
                message = "Código enviado a su correo."
                isError = false
                step = 2
            },
            onFail = {
                loading = false
                message = it
                isError = true
            }
        )
    }

    // --- PASO 2: Cambiar contraseña ---
    fun onChangePassword() {
        message = null

        if (code.isBlank() || newPass.isBlank() || confirmPass.isBlank()) {
            message = "Complete todos los campos."
            isError = true
            return
        }

        if (!code.all { it.isDigit() }) {
            message = "El código debe ser numérico."
            isError = true
            return
        }

        if (newPass != confirmPass) {
            message = "Contraseñas no coinciden."
            isError = true
            return
        }

        val weak = passwordValidationError(newPass)
        if (weak != null) {
            message = "Contraseña débil: $weak"
            isError = true
            return
        }

        loading = true
        vm.resetPassword(
            email = email,
            code = code,
            newPass = newPass,
            onSuccess = {
                loading = false
                // Volver al login con mensaje de éxito
                nav.previousBackStackEntry?.savedStateHandle?.set("registerMessage", "Contraseña cambiada correctamente.")
                nav.popBackStack()
            },
            onFail = {
                loading = false
                message = it ?: "Código incorrecto o vencido."
                isError = true
            }
        )
    }

    Column(
        Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = if (step == 1) "Recuperar Contraseña" else "Crear Nueva Contraseña",
            fontSize = 22.sp
        )
        Spacer(Modifier.height(16.dp))

        if (step == 1) {
            // --- FORMULARIO EMAIL ---
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Ingrese su email") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )

            Spacer(Modifier.height(16.dp))
            Button(
                onClick = { onSendCode() },
                modifier = Modifier.fillMaxWidth(),
                enabled = !loading
            ) {
                Text(if (loading) "Enviando..." else "Recuperar")
            }

        } else {
            // --- FORMULARIO CODIGO + NUEVA PASS ---
            Text("Código enviado a: $email", fontSize = 14.sp, color = Color.Gray)
            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = code,
                onValueChange = { if (it.length <= 5) code = it },
                label = { Text("Código (5 dígitos)") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = newPass,
                onValueChange = { newPass = it },
                label = { Text("Nueva contraseña") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )
            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = confirmPass,
                onValueChange = { confirmPass = it },
                label = { Text("Confirmar contraseña") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )

            Spacer(Modifier.height(16.dp))
            Button(
                onClick = { onChangePassword() },
                modifier = Modifier.fillMaxWidth(),
                enabled = !loading
            ) {
                Text(if (loading) "Actualizando..." else "Cambiar Contraseña")
            }
        }

        // Mensajes
        message?.let {
            Spacer(Modifier.height(12.dp))
            Text(
                text = it,
                color = if (isError) Color.Red else Color(0xFF1B5E20)
            )
        }
    }
}