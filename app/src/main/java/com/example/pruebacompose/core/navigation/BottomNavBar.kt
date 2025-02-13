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
import androidx.compose.ui.unit.dp

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
                IconButton(modifier = Modifier.size(56.dp), onClick = navigateToFilms) {
                    Icon(
                        Icons.Filled.Favorite,
                        modifier = Modifier.padding(8.dp).fillMaxSize(),
                        contentDescription = "Films"
                    )
                }
                IconButton(modifier = Modifier.size(56.dp), onClick = navigateToProfile) {
                    Icon(
                        Icons.Filled.Person,
                        modifier = Modifier.padding(8.dp).fillMaxSize(),
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