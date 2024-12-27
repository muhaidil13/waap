package com.example.wapp.components

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import com.example.wapp.R
import com.example.wapp.data.Markers

import com.example.wapp.screen.maps.MapsViewModel
import com.example.wapp.screen.maps.showMarker
import com.example.wapp.screen.maps.updateMapLocation
import com.mapbox.geojson.Point
import com.mapbox.maps.MapView
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@ExperimentalMaterial3Api
@Composable
fun SearchMarker(
    cartItems: List<Markers>,
    onItemSelected: (Markers) -> Unit,
    mapsViewModel: MapsViewModel,
    mapView: MapView,
    role: String
    ) {
    val context = LocalContext.current
    val accessToken = context.getString(R.string.mapbox_access_token)
    var searchQuery by remember { mutableStateOf("") }
    var filteredItems by remember { mutableStateOf(emptyList<Markers>()) }

    val locationActive = remember { mutableStateOf(false) }


    val itemSelected = remember {
        mutableStateOf<Markers?>(null)
    }
    val filteredFeature by mapsViewModel.listSearchPlace.collectAsState()


    val combinedList = (filteredFeature ?: emptyList()) + (filteredItems ?: emptyList())

    val sortedList = combinedList.sortedWith(compareByDescending<Markers> { it.type.isNotBlank() }.thenBy { it.type })

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        locationActive.value = isGranted
    }
    val scope = rememberCoroutineScope()

    var debounceJob by remember { mutableStateOf<Job?>(null) }

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
        itemSelected.value = null
    }
    LaunchedEffect(itemSelected){
        itemSelected.value?.let {
            updateMapLocation(mapView = mapView, longitude = it.longitude, latitude =  it.latitude )
        }
    }

    val key = context.getString(R.string.mapbox_access_token)
    Column(modifier = Modifier) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { query ->
                if(searchQuery.isEmpty()){
                    mapsViewModel.clearListsearchPlace()
                }
                searchQuery = query
                if(role =="admin"){
                    debounceJob?.cancel()
                    debounceJob = scope.launch {
                        delay(1000L)
                        mapsViewModel.search(query, key)
                    }
                }
                filteredItems = cartItems.filter {
                    it.locationName.contains(query, ignoreCase = true)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            placeholder = { Text("Lokasi Pencarian") },
            label ={Text("Lokasi Pencarian")} ,
            singleLine = true,
            textStyle = TextStyle(
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal
            ),
            trailingIcon = {
                Icon(imageVector = Icons.Filled.Search, contentDescription = "")
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                containerColor = Color.Transparent,
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = Color.Gray,
                focusedLabelColor = MaterialTheme.colorScheme.primary,
                unfocusedLabelColor = Color.Gray
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        if(searchQuery.isNotEmpty()){
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ){
                items(sortedList) { item ->
                    if (searchQuery != ""){
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    if (item.type.isNotEmpty()) {
                                        onItemSelected(item)
                                    } else {
                                        item.markerId?.let {
                                            mapsViewModel.getFeatureDetails(
                                                id = it,
                                                accessToken = accessToken
                                            )
                                        }
                                    }
                                    updateMapLocation(
                                        mapView = mapView,
                                        longitude = item.longitude,
                                        latitude = item.latitude
                                    )

                                    itemSelected.value = item
                                    searchQuery = ""
                                }
                        ){
                            if(item.type.isEmpty()) {
                                Text(
                                    text = "Disarakan Untuk Anda",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                    ,
                                    style = TextStyle(
                                        color = MaterialTheme.colorScheme.primary,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Normal
                                    )
                                )
                            }

                            Row(
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ){

                                if(item.type.isNotEmpty()){
                                    Icon(imageVector = Icons.Filled.LocationOn, tint = MaterialTheme.colorScheme.primary, modifier= Modifier.size(25.dp), contentDescription = "")
                                }
                                Text(
                                    text = item.locationName,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                    ,
                                    style = TextStyle(
                                        color = MaterialTheme.colorScheme.primary,
                                        fontSize = 22.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                )
                            }
                            Text(
                                text = item.streetName,
                                modifier = Modifier
                                    .fillMaxWidth()
                                ,
                                style = TextStyle(
                                    color = Color.Gray,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Normal
                                )
                            )
                            Text(
                                text = item.type,
                                modifier = Modifier
                                    .fillMaxWidth()
                                ,
                                style = TextStyle(
                                    color = Color.Gray,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Normal
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}
