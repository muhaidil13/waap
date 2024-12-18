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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
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
import com.mapbox.geojson.Point
import kotlinx.coroutines.launch


@ExperimentalMaterial3Api
@Composable
fun SearchMarker(
    cartItems: List<Markers>,
    onItemSelected: (Markers) -> Unit,
    ) {
    val context = LocalContext.current
    var searchQuery by remember { mutableStateOf("") }
    var filteredItems by remember { mutableStateOf(emptyList<Markers>()) }
    val locationActive = remember { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        locationActive.value = isGranted
    }


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
    }

    Column(modifier = Modifier) {
        // Input Search

        OutlinedTextField(
            value = searchQuery,
            onValueChange = { query ->
                searchQuery = query
                // Filter items based on the input search query
                filteredItems = cartItems.filter {
                    it.locationName.contains(query, ignoreCase = true)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            placeholder = { Text("Search Lokasi...") },
            singleLine = true,
            textStyle = TextStyle(
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal
            ),
            // Customizing the border color on focus and unfocus
            colors = TextFieldDefaults.outlinedTextFieldColors(
                containerColor = Color.Transparent, // Transparent background
                focusedBorderColor = MaterialTheme.colorScheme.primary, // Border color when focused
                unfocusedBorderColor = Color.Gray, // Border color when unfocused
                focusedLabelColor = MaterialTheme.colorScheme.primary, // Label color when focused
                unfocusedLabelColor = Color.Gray // Label color when unfocused
            )
        )
        Spacer(modifier = Modifier.height(8.dp))

        val scope = rememberCoroutineScope()
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ){
            items(filteredItems) { item ->
                if (searchQuery != ""){
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onItemSelected(item)
                                searchQuery = ""
                            }
                    ){
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ){
                            Icon(imageVector = Icons.Filled.LocationOn, tint = MaterialTheme.colorScheme.primary, modifier= Modifier.size(25.dp), contentDescription = "")
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
