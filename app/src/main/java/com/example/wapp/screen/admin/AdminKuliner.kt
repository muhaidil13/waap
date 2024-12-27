package com.example.wapp.screen.admin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.wapp.components.ItemCard
import com.example.wapp.screen.maps.MapsViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi


@ExperimentalPermissionsApi
@Composable
fun AdminKulinerScreen(mapsViewModel: MapsViewModel, navController: NavController){
    val kulinerMarker = mapsViewModel.markers.collectAsState()
    val listState = rememberLazyListState()
    val context = LocalContext.current


    LaunchedEffect(kulinerMarker.value){
        mapsViewModel.getListOfMarkerKuliner()
    }



    Scaffold {innerPadding ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 10.dp)){
            Column(modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(top = 10.dp, end = 10.dp, start = 10.dp)) {
                Text(
                    text = "Lokasi Wisata:", style = TextStyle(
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Normal,
                        fontSize = 20.sp
                    )
                )

                Spacer(modifier = Modifier.height(20.dp))
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    kulinerMarker.value?.let {
                        items(it) {item ->
                            ItemCard(
                                marker = item,
                                onClick = {
                                    navController.navigate("Detail/${item.id}")
                                },
                                onDelete = {
                                    item.id?.let { id ->
                                        mapsViewModel.deleteMarkerFromDatabase(context = context, markerId = id)
                                    }
                                    mapsViewModel.getListOfMarkerKuliner()
                                },
                                onUpdate = { mark ->
                                    item.id?.let { id ->
                                        mapsViewModel.updateMarkerToDatabase(context = context, markers = mark, markerId = id)
                                        mapsViewModel.getListOfMarkerKuliner()
                                    }
                                },
                                isEditable = true,
                                onUpload = { file ->
                                    item.id?.let{ id ->
                                        mapsViewModel.uploadImage(file, id, context = context)
                                    }
                                    mapsViewModel.getAllImage()
                                }
                            )
                        }
                    }?: item {
                        Text(
                            text = "Anda Belum Menambah Marker ", style = TextStyle(
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Normal,
                                fontSize = 20.sp
                            )
                        )
                    }
                }
            }
        }
    }
}