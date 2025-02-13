package com.example.pruebacompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.pruebacompose.core.navigation.NavigationWrapper
import com.example.pruebacompose.ui.theme.PruebaComposeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PruebaComposeTheme {
                NavigationWrapper()
            }
        }
    }
}


@Preview(showSystemUi = true)
@Composable
fun PreviewApp() {
    PruebaComposeTheme {
        NavigationWrapper()
    }
}