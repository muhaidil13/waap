package com.example.wapp.components

import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wapp.data.Markers
import com.example.wapp.screen.components.InputType
import com.google.firebase.firestore.GeoPoint
import com.mapbox.geojson.Point

@Composable
fun MarkerDialog(location: Point,streetName: String, locationName: String? = null ,onDismis:() -> Unit, onAddMarker: (Markers) -> Unit ) {
    val name = remember {
        mutableStateOf(locationName ?: "")
    }

    val type  = remember {
        mutableStateOf("wisata")
    }
    val desctiption = remember {
        mutableStateOf("")
    }

    val radioButtonState = remember{
        mutableStateOf(true)
    }
    val isButtonEnabled = remember {
        mutableStateOf(false)
    }
    LaunchedEffect(name.value, type.value, desctiption.value){
        isButtonEnabled.value = name.value.isNotEmpty() &&  type.value.isNotEmpty() && desctiption.value.isNotEmpty()
    }


    AlertDialog(
        onDismissRequest = {onDismis() },
        confirmButton = {
            Button(onClick = {
                onAddMarker(
                    Markers(
                        locationName = name.value,
                        type = type.value,
                        categoryId = "",
                        latitude = location.latitude(),
                        longitude = location.longitude(),
                        addedBy = "",
                        createdAt = "",
                        description =desctiption.value,
                        updatedAt = "",
                        streetName = streetName
                    )
                ) },) {
                Text(text = "Tambah")
            }

        },
        title = {
            Text(text = "Tambah Marker", color = MaterialTheme.colorScheme.primary, style = TextStyle(
                fontWeight = FontWeight.SemiBold,
                fontSize = 25.sp)
        )  },
        icon = { Icon(imageVector = Icons.Outlined.Add, modifier = Modifier.size(50.dp), tint =MaterialTheme.colorScheme.primary , contentDescription = "Add") },
        text = {
            Column (
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ){
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ){
                    Text(text = "Jalan :", color = MaterialTheme.colorScheme.primary, style = TextStyle(
                        fontWeight = FontWeight.Normal,
                        fontSize = 16.sp)
                    )
                    Text(text = streetName, color = MaterialTheme.colorScheme.primary, style = TextStyle(
                        fontWeight = FontWeight.Normal,
                        fontSize = 15.sp)
                    )
                }

                Column{
                    Text(text = "Nama Tempat", color = MaterialTheme.colorScheme.primary, style = TextStyle(
                        fontWeight = FontWeight.Normal,
                        fontSize = 16.sp)
                    )
                    InputFieldCustom(onValueChange = {
                        name.value = it
                    }, value = name.value, placeHolder = "Nama Tempat", onKeyEvent = {

                    },
                        type = InputType.Text,
                        focusRequester = null
                    )
                }
                Column{
                    Text(text = "Deskripsi Tempat", color = MaterialTheme.colorScheme.primary, style = TextStyle(
                        fontWeight = FontWeight.Normal,
                        fontSize = 16.sp
                    )
                    )
                    InputFieldCustom(onValueChange = {
                        desctiption.value = it
                    }, value = desctiption.value, placeHolder = "Deskripsi Tempat", onKeyEvent = {

                    },
                        type = InputType.Text,
                        focusRequester = null
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ){
                    Text(text = "Ditandai Sebagai :", color = MaterialTheme.colorScheme.primary, style = TextStyle(
                        fontWeight = FontWeight.Normal,
                        fontSize = 16.sp)
                    )
                    Text(text = type.value, color = MaterialTheme.colorScheme.primary, style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp)
                    )
                }
                Column {
                    Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically){
                        RadioButton(selected = radioButtonState.value, onClick = {
                            radioButtonState.value = true
                            type.value = "wisata"
                        })
                        Text(text = "Wisata", color = if (radioButtonState.value) Color.Blue else Color.Black)
                        RadioButton(selected = !radioButtonState.value, onClick = {
                            radioButtonState.value = false
                            type.value = "kuliner"
                        })
                        Text(text = "Kuliner", color = if (!radioButtonState.value) Color.Blue else Color.Black)
                    }
                }
            }

        }
    )
}