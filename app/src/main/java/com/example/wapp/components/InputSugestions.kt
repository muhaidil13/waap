package com.example.wapp.components

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.wapp.data.Markers
import com.example.wapp.hideKeyboard
import com.google.android.gms.location.LocationServices
import com.mapbox.geojson.Point
import kotlinx.coroutines.launch

@Composable
fun InputSugestions(
    cartItems: List<Markers>,
    locationOfUser: Point?,
    drawerState: DrawerState,
    onItemSelected: (Point) -> Unit,


) {
    val context = LocalContext.current
    var searchQuery by remember { mutableStateOf("") }
    var filteredItems by remember { mutableStateOf(emptyList<Markers>()) }
    val locationActive = remember { mutableStateOf(false) }

    val courutineScope = rememberCoroutineScope()
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        locationActive.value = isGranted
    }

    // Minta izin saat pertama kali diluncurkan
    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }else{
            locationActive.value = true
        }
        Log.d("User Location Is", locationOfUser?.latitude().toString())
    }

    Column(modifier = Modifier
        .padding(16.dp)
        .padding(top = 40.dp)) {
        // Input Search
        TextField(
            value = searchQuery,
            onValueChange = { query ->
                searchQuery = query
                // Filter item berdasarkan input pencarian
                filteredItems = cartItems.filter {
                    it.locationName.contains(query, ignoreCase = true)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Search Lokasi...") }
        )
        Spacer(modifier = Modifier.height(8.dp))

        // List Hasil Pencarian
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ){

            item {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.clickable {
                        locationOfUser?.let{
                            onItemSelected(it)
                        }

                        courutineScope.launch {
                            drawerState.close()
                            hideKeyboard(context)
                        }
                    }
                ){
                    Icon(imageVector = Icons.Filled.LocationOn, tint = MaterialTheme.colorScheme.primary, modifier=Modifier.size(30.dp), contentDescription = "")
                    Column(horizontalAlignment = Alignment.Start){
                        Text(
                            text = "Pilih Lokasi Saya",
                            style = TextStyle(
                                color = MaterialTheme.colorScheme.primary,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                        Text(
                            text = "Menggunakan Lokasi Sekarang Anda Berada",
                            style = TextStyle(
                                color = Color.Black,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Normal
                            )
                        )
                    }
                }
                if (!locationActive.value) {
                    Text(
                        text = "Izin lokasi belum diberikan.",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(8.dp)
                    )
                } else if (locationOfUser == null) {
                    Text(
                        text = "Lokasi tidak tersedia.",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }

            items(filteredItems) { item ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ){
                    Icon(imageVector = Icons.Filled.LocationOn, tint = MaterialTheme.colorScheme.primary, modifier=Modifier.size(30.dp), contentDescription = "")
                    Text(
                        text = item.locationName,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onItemSelected(
                                    Point.fromLngLat(
                                        item.longitude,
                                        item.latitude
                                    )
                                )
                                courutineScope.launch {
                                    drawerState.close()
                                }
                            },
                        style = TextStyle(
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                    Column(horizontalAlignment = Alignment.Start){


                    }
                }

            }
        }
    }
}
