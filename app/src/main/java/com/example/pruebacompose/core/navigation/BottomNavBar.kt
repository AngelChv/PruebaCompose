package com.example.pruebacompose.core.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.pruebacompose.ui.theme.PruebaComposeTheme


@Composable
fun BottomNavBar(
    navController: NavController,
) {
    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        topLevelRoutes.forEach { topLevelRoute ->
            NavigationBarItem(
                icon = { Icon(topLevelRoute.icon, topLevelRoute.name) },
                label = { Text(topLevelRoute.name) },
                // Para marcar si un item está seleccionado:
                // Comparo la ruta actual con la del item a crear.
                // Sin embargo, la ruta del item devuelve la referencia del objeto en memoria,
                // mientras que la ruta de la navegación es el nombre completo de la clase.
                // Por eso uso ::class.qualifiedName para obtener solo el nombre de la clase.
                selected = currentDestination?.route == topLevelRoute.route::class.qualifiedName,
                onClick = {
                    navController.navigate(topLevelRoute.route) {
                        // Con deep links puede dar problemas
                        popUpTo(0) { inclusive = true }  // Elimina el historial de navegación
                        launchSingleTop = true // Evita duplicados en la pila de navegación
                        restoreState = true // Restaura el estado si ya estaba en la pila
                    }

                }
            )
        }
    }
}

@Preview
@Composable
fun BottomNavBarPreview() {
    PruebaComposeTheme {
        BottomNavBar(rememberNavController())
    }
}