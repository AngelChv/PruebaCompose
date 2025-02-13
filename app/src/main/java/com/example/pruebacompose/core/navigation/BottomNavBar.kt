package com.example.pruebacompose.core.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun BottomNavBar(
    navigateToFilms: () -> Unit,
    navigateToProfile: () -> Unit,
    floatingActionButton: @Composable () -> Unit = {}
) {
    BottomAppBar(
        actions = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                IconButton(onClick = navigateToFilms) {
                    Icon(
                        Icons.Filled.Favorite,
                        contentDescription = "Films"
                    )
                }
                IconButton(onClick = navigateToProfile) {
                    Icon(
                        Icons.Filled.Person,
                        contentDescription = "Profile",
                    )
                }
            }
        },
        floatingActionButton = floatingActionButton,
    )
}

@Preview
@Composable
fun BottomNavBarPreview() {
    BottomNavBar({}, {}) { FloatingActionButton(onClick = {}) {
        Icon(Icons.Default.Add, "Description")
    } }
}