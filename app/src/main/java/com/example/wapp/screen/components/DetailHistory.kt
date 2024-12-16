package com.example.wapp.screen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.wapp.data.NavigationHistory
import com.example.wapp.screen.maps.MapsViewModel


@Composable
fun DetailHistory(item: NavigationHistory, mapsViewModel: MapsViewModel, navController: NavController){
    val marker = mapsViewModel.marker.collectAsState()
    val scrollState = rememberScrollState()
    LaunchedEffect(Unit){
        mapsViewModel.getMarkersById(item.destinationId)
    }
    Scaffold { innerPadding ->
        marker.value?.let { mark ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(innerPadding)
                    .padding(10.dp)
            ){
                Spacer(modifier = Modifier.height(20.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)){
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(imageVector = Icons.Filled.ArrowBackIosNew ,  tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(40.dp),  contentDescription ="" )
                    }
                    Column {
                        Text(text = mark.locationName , style = TextStyle(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.primary)
                        )
                        Text(text = mark.locationName, style = TextStyle(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Normal,
                            color = MaterialTheme.colorScheme.primary)
                        )
                    }
                }
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(3.dp),
                ) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(5.dp),
                        verticalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        Text(text = "Info Tujuan", style = TextStyle(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Normal,
                            color = MaterialTheme.colorScheme.primary)
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)){
                            Text(text = "Ditandai Sebagai", style = TextStyle(
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Normal,
                                color = MaterialTheme.colorScheme.primary)
                            )
                            Text(text = mark.type, style = TextStyle(
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Normal,
                                color = MaterialTheme.colorScheme.primary)
                            )
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)){
                            Text(text = "Deskripsi", style = TextStyle(
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Normal,
                                color = MaterialTheme.colorScheme.primary)
                            )
                            Text(text = mark.description, style = TextStyle(
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Normal,
                                color = MaterialTheme.colorScheme.primary)
                            )
                        }
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(3.dp),
                ){
                    Spacer(modifier = Modifier.height(10.dp))
                    Column (
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(5.dp)
                        ,
                        verticalArrangement = Arrangement.spacedBy(3.dp)
                    ){
                        Text(text = "Info Navigasi", style = TextStyle(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Normal,
                            color = MaterialTheme.colorScheme.primary)
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)){
                            Text(text = "Jarak Tempuh", style = TextStyle(
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Normal,
                                color = MaterialTheme.colorScheme.primary)
                            )
                            Text(text = item.totalJarak + " (KM)", style = TextStyle(
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Normal,
                                color = MaterialTheme.colorScheme.primary)
                            )
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)){
                            Text(text = "Waktu Tempuh", style = TextStyle(
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Normal,
                                color = MaterialTheme.colorScheme.primary)
                            )
                            Text(text = item.totalWaktu , style = TextStyle(
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Normal,
                                color = MaterialTheme.colorScheme.primary)
                            )
                        }
                        Row (horizontalArrangement = Arrangement.spacedBy(10.dp)){
                            Text(text = "Di Mulai Pada", style = TextStyle(
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Normal,
                                color = MaterialTheme.colorScheme.primary)
                            )
                            Text(text = item.startTime, style = TextStyle(
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Normal,
                                color = MaterialTheme.colorScheme.primary)
                            )
                        }
                        Row (horizontalArrangement = Arrangement.spacedBy(10.dp)){
                            Text(text = "Berakhir Pada", style = TextStyle(
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Normal,
                                color = MaterialTheme.colorScheme.primary)
                            )
                            Text(text = item.endTime!!, style = TextStyle(
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Normal,
                                color = MaterialTheme.colorScheme.primary)
                            )
                        }

                    }

            }
        }?: Text(text = "Loading Mas Bro")

        }
    }
}

