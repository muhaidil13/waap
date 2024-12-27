package com.example.wapp.screen.admin

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.wapp.components.CardItemsCount

@Composable
fun HomeDashboard( scroll: ScrollState, navController: NavController){

        Column(modifier = Modifier.fillMaxWidth()){
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(scroll)

            ) {

                Text(text = "Petunjuk Penggunaan: ", style = TextStyle(
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Light
                )
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(text = "1. Jika Anda Admin Anda Dapat Mengakses Halaman ini Jika Bukan Halaman Ini Tidak Akan Tampil",
                    style = TextStyle(
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Justify,
                        fontWeight = FontWeight.Light
                    ),
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(text = "2. Anda Dapat Melalukan CRUD Untuk Marker.kt Dan Users",
                    style = TextStyle(
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Justify,
                        fontWeight = FontWeight.Light
                    ),
                )

                Spacer(modifier = Modifier.height(24.dp))
                CardItemsCount(navController= navController)
            }
        }

}