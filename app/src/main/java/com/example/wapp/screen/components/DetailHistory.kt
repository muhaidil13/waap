package com.example.wapp.screen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.ui.Alignment
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
            BoxWithConstraints(
                modifier = Modifier.padding(vertical = 50.dp, horizontal = 20.dp)
            ){
                val max = maxHeight
                Spacer(modifier = Modifier.height(max * .03f))
                Box(modifier =  Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter){
                    Row(horizontalArrangement = Arrangement.Start, modifier = Modifier.fillMaxWidth()){
                        IconButton(onClick = {
                            navController.popBackStack()
                        }) {
                            Icon(imageVector = Icons.Filled.ArrowBackIosNew ,  tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(40.dp),  contentDescription ="" )
                        }
                        Column {
                            Text(text = mark.locationName , style = TextStyle(
                                fontSize = 23.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.primary)
                            )
                            Text(text = mark.streetName, style = TextStyle(
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Normal,
                                color = MaterialTheme.colorScheme.primary)
                            )
                        }
                    }
                }
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                        .padding(innerPadding)
                        .padding(horizontal = 10.dp, vertical = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(5.dp),
                        verticalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)){
                            Text(text = "Detail History Pencarian Anda: ", style = TextStyle(
                                fontSize = 20.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.primary)
                            )
                            Column(modifier = Modifier.padding(start = 10.dp)){
                                Text(text = mark.locationName , style = TextStyle(
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.primary)
                                )
                                Text(text = mark.streetName, style = TextStyle(
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Normal,
                                    color = MaterialTheme.colorScheme.primary)
                                )
                            }
                            Column(modifier = Modifier.padding(start = 10.dp)){
                                Text(text = "Ditandai Sebagai", style = TextStyle(
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.primary)
                                )
                                Text(text = mark.type, style = TextStyle(
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Normal,
                                    color = MaterialTheme.colorScheme.primary)
                                )
                            }
                            Column(modifier = Modifier.padding(start = 10.dp)){
                                Text(text = "Deskripsi", style = TextStyle(
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.primary)
                                )
                                Text(text = mark.description, style = TextStyle(
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Normal,
                                    color = MaterialTheme.colorScheme.primary)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)){
                            Text(text = "Rincian Perjalanan Anda: ", style = TextStyle(
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.primary)
                            )
                            Column(modifier = Modifier.padding(start = 10.dp)){
                                Text(text = "Lokasi Awal Anda", style = TextStyle(
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.primary)
                                )
                                Text(text = item.startStreetName, style = TextStyle(
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Normal,
                                    color = MaterialTheme.colorScheme.primary)
                                )
                            }
                            Column(modifier = Modifier.padding(start = 10.dp)){
                                Text(text = "Jarak Tempuh", style = TextStyle(
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.primary)
                                )
                                Text(text = item.totalJarak + " (KM)", style = TextStyle(
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Normal,
                                    color = MaterialTheme.colorScheme.primary)
                                )
                            }
                            Column(modifier = Modifier.padding(start = 10.dp)){
                                Text(text = "Waktu Anda Memulai Perjalanan", style = TextStyle(
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.primary)
                                )
                                Text(text = item.totalWaktu , style = TextStyle(
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Normal,
                                    color = MaterialTheme.colorScheme.primary)
                                )
                            }
                            Column(modifier = Modifier.padding(start = 10.dp)){
                                Text(text = "Di Mulai Pada", style = TextStyle(
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.primary)
                                )
                                Text(text = item.startTime, style = TextStyle(
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Normal,
                                    color = MaterialTheme.colorScheme.primary)
                                )
                            }
                            Column(modifier = Modifier.padding(start = 10.dp)){
                                Text(text = "Berakhir Pada", style = TextStyle(
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.SemiBold,
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

                }

            }
        }
    }
}

