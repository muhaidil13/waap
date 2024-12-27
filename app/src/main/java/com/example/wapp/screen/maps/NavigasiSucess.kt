package com.example.wapp.screen.maps

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.wapp.RouteApp
import com.example.wapp.screen.components.BottomMenuScreen

@Composable
fun NavigasiSucess(navController: NavController){
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ){
            Text(
                text = "Selamat Anda Berhasil Sampai  Tujuan",
                style = TextStyle(
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Normal
                )
            )
            Button(onClick = {
                navController.navigate(BottomMenuScreen.HomePage.route){
                    popUpTo(BottomMenuScreen.HomePage.route){
                        inclusive = true
                    }
                }
            }) {
                Text(text = "Kembali Ke Home")
            }
        }
    }
}