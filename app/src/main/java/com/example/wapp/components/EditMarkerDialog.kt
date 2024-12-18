package com.example.wapp.components

import android.Manifest
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wapp.data.Markers
import com.example.wapp.hideKeyboard
import com.example.wapp.screen.components.InputType
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.mapbox.geojson.Point
import kotlinx.coroutines.launch


@Composable
fun EditMarkerDialog(location: Point, marker: Markers?, onDismis:() -> Unit, onSubmit: (Markers) -> Unit, onCameraClick: () -> Unit,  onGaleryClick:() -> Unit) {
    val scrollState = rememberScrollState()
    val name = remember {
        mutableStateOf(marker?.locationName ?: "")
    }

    val type  = remember {
        mutableStateOf(marker?.type ?: "")
    }
    val desctiption = remember {
        mutableStateOf(marker?.description ?: "")
    }

    val streetName = remember {
        mutableStateOf(marker?.streetName ?: "")
    }

    val radioButtonState = remember{
        mutableStateOf( marker?.type == "wisata")
    }
    val isButtonEnabled = remember {
        mutableStateOf(false)
    }
    LaunchedEffect(name.value, type.value, desctiption.value){
        isButtonEnabled.value = name.value.isNotEmpty() &&  type.value.isNotEmpty() && desctiption.value.isNotEmpty()
    }
    val courutineScope = rememberCoroutineScope()

    Log.d("des", desctiption.value)

    val context = LocalContext.current


    AlertDialog(
        modifier = Modifier.clickable {
            courutineScope.launch {
                hideKeyboard(context)
            }

        },
        onDismissRequest = {onDismis() },
        confirmButton = {
            Button(
                modifier = Modifier.windowInsetsPadding(WindowInsets.ime),

                onClick = {
                onSubmit(
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
                        streetName =streetName.value
                    )
                ) },) {
                Text(text = "Selesai")
            }

        },
        title = {
            Text(text = "Edit Marker", color = MaterialTheme.colorScheme.primary, style = TextStyle(
                fontWeight = FontWeight.SemiBold,
                fontSize = 25.sp)
            )  },
        icon = { Icon(imageVector = Icons.Outlined.Add, modifier = Modifier.size(50.dp), tint = MaterialTheme.colorScheme.primary , contentDescription = "Add") },
        text = {
            Column (
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(scrollState),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ){
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ){
                    Text(text = "Nama Jalan :", color = MaterialTheme.colorScheme.primary, style = TextStyle(
                        fontWeight = FontWeight.Normal,
                        fontSize = 16.sp)
                    )
                    Text(text = streetName.value, color = MaterialTheme.colorScheme.primary, style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp)
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
                Column(modifier = Modifier.fillMaxWidth()){
                    Text(text = "Gambar", color = MaterialTheme.colorScheme.primary, style = TextStyle(
                        fontWeight = FontWeight.Normal,
                        fontSize = 16.sp)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Row {
                        Button(onClick =onCameraClick, modifier = Modifier.weight(.5f), colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.onPrimary), border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary) ) {
                            Text(text = "Camera", style = TextStyle(
                                fontWeight = FontWeight.Normal,
                                color = MaterialTheme.colorScheme.primary,
                                fontSize = 13.sp)
                            )
                        }
                        Button(onClick =onGaleryClick, modifier = Modifier.weight(.5f), colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.onPrimary), border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary) ) {
                            Text(text = "Galery", style = TextStyle(
                                fontWeight = FontWeight.Normal,
                                color = MaterialTheme.colorScheme.primary,
                                fontSize = 13.sp)
                            )
                        }
                    }

                }
            }

        }
    )
}