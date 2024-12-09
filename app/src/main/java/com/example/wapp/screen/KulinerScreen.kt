package com.example.wapp.screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.wapp.MainViewModel
import com.example.wapp.components.ItemCard
import com.example.wapp.screen.maps.MapsViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi


@ExperimentalPermissionsApi
@Composable
fun KulinerScreen(navController: NavController, mapsViewModel: MapsViewModel){

    val kulinerMarkers =  mapsViewModel.markers.collectAsState()


    LaunchedEffect(Unit){
        mapsViewModel.getListOfMarkerKuliner()
    }
    Box(modifier = Modifier
        .fillMaxSize()
        .padding(top = 80.dp) ){
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(top = 10.dp, end = 10.dp, start = 10.dp,)) {
            Text(
                text = "Lokasi Kuliner", style = TextStyle(
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Normal,
                    fontSize = 20.sp
                )
            )
            Spacer(modifier = Modifier.height(20.dp))
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                kulinerMarkers.value?.let {
                    items(it) { item ->
                        ItemCard(
                            marker = item,
                            onClick = {
                                navController.navigate("Detail/${item.id}")
                            },


                        )
                    }
                }
            }
        }
    }
}
