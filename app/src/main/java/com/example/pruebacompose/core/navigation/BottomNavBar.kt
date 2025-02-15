package com.example.pruebacompose.core.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.pruebacompose.ui.theme.PruebaComposeTheme

@Composable
fun <T> BottomNavBar(
    currentScreen: T,
    navigateToFilms: () -> Unit = {},
    navigateToProfile: () -> Unit = {},
) {
    NavigationBar {
        NavigationBarItem(
            icon = {Icon(Icons.Filled.Favorite, "Films")},
            label = { Text("Films")},
            selected = currentScreen is Films,
            onClick = navigateToFilms
        )

        NavigationBarItem(
            icon = {Icon(Icons.Filled.Person, "Profile")},
            label = { Text("Profile")},
            selected = currentScreen is Profile,
            onClick = navigateToProfile
        )
    }
}

@Preview
@Composable
fun BottomNavBarPreview() {
    PruebaComposeTheme {
        BottomNavBar(Films)
    }
}