package com.example.pruebacompose.ui.components

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.example.pruebacompose.R

@Composable
fun UsernameField(value: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        maxLines = 1,
        label = { Text("Nombre de usuario") }
    )
}

@Composable
fun EmailField(value: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        maxLines = 1,
        label = { Text("Correo electrónico") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
    )
}

@Composable
fun PasswordField(
    value: String, isHidingPassword: Boolean,
    onToggleHidePassword: () -> Unit,
    onValueChange: (String) -> Unit,
) {
    val visualTransformation = if (isHidingPassword) PasswordVisualTransformation()
    else VisualTransformation.None

    @Composable
    fun leadingIcon() = if (isHidingPassword) {
        IconButton(onToggleHidePassword) {
            Icon(
                painter = painterResource(R.drawable.eye_24),
                "Show password"
            )
        }
    } else {
        IconButton(onToggleHidePassword) {
            Icon(
                painter = painterResource(R.drawable.hide_eye_black_24),
                "Hide password"
            )
        }
    }

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        maxLines = 1,
        label = { Text("Contraseña") },
        trailingIcon = { leadingIcon() },
        visualTransformation = visualTransformation,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
    )
}
