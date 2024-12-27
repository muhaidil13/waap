package com.example.wapp.screen.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalDining
import androidx.compose.material.icons.filled.PanoramaFishEye
import androidx.compose.material.icons.filled.PersonSearch
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.PhotoLibrary
import androidx.compose.ui.graphics.vector.ImageVector


sealed class BottomMenuScreen(
    val route:String,
    val icon: ImageVector,
    val title: String
){
    object HomePage: BottomMenuScreen(
        route = "Home Screen",
        icon = Icons.Outlined.Home,
        title ="Home"
    )
    object GaleryScreen: BottomMenuScreen(
        route = "GaleryScreen",
        icon = Icons.Outlined.PhotoLibrary,
        title ="Galery"
    )
    object HistoryScreen: BottomMenuScreen(
        route = "HistoryScreen",
        icon = Icons.Outlined.History,
        title ="History"
    )
    object AboutPage: BottomMenuScreen(
        route = "About Screen",
        icon = Icons.Outlined.AccountCircle,
        title ="Tentang"
    )
    object HomeAdmin: BottomMenuScreen(
        route = "adminScreen",
        icon = Icons.Outlined.Home,
        title = "Home"
    )
    object WisataCrud: BottomMenuScreen(
        route = "WisataCrudScreen",
        icon =Icons.Filled.PanoramaFishEye,
        title = "Wisata"

    )
    object KulinerCrud: BottomMenuScreen(
        route = "WisataCrudScreen",
        icon = Icons.Filled.LocalDining,
        title = "Kuliner"
    )
    object UserCrud: BottomMenuScreen(
        route = "UserCrudScreen",
        icon = Icons.Filled.PersonSearch,
        title = "Kuliner"
    )
}
