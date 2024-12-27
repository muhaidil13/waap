package com.example.wapp.screen.admin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.wapp.screen.maps.MapsViewModel

@Composable
fun AdminDashboard(navController: NavController, mapsViewModel: MapsViewModel){

    val scroll = rememberScrollState()
    Scaffold { paddingValues ->
        Column(modifier = Modifier.fillMaxWidth().padding(paddingValues).padding(horizontal = 25.dp,)) {
            Row(modifier = Modifier
                .padding(top = 60.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(15.dp)

            ){
                Icon(imageVector = Icons.Filled.Home, modifier = Modifier.size(30.dp), tint = MaterialTheme.colorScheme.primary, contentDescription = "")
                Text(text = "Selamat Datang: Admin", style = TextStyle(color = MaterialTheme.colorScheme.primary, fontSize = 20.sp, fontWeight = FontWeight.SemiBold))
            }
            HomeDashboard( scroll = scroll, navController = navController)
        }
    }
}
