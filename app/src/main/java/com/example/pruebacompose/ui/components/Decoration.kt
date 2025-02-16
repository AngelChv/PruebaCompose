package com.example.pruebacompose.ui.components

import androidx.compose.foundation.Image
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import com.example.pruebacompose.R

@Composable
fun HeaderImage() {
    Image(
        painter = painterResource(R.drawable.chv), contentDescription = "Logo",
        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
    )
}
