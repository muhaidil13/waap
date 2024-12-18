package com.example.wapp.components

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForwardIos
import androidx.compose.material.icons.automirrored.outlined.ReadMore
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.wapp.RequestPermissions
import com.example.wapp.data.Markers

import com.example.wapp.saveBitmapToFile
import com.example.wapp.saveUriToFile
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState
import com.mapbox.geojson.Point
import java.io.File

@ExperimentalPermissionsApi
@Composable
fun ItemCard(
    marker: Markers,
    onDelete: ((Markers) -> Unit)? = null,
    onClick: () -> Unit,
    onUpload:((File) -> Unit)? = null,
    isEditable: Boolean = false,
    onUpdate: ((Markers) -> Unit)? = null
) {
    val showAlert = remember { mutableStateOf("") }
    val isShowDialog = remember { mutableStateOf(false) }
    val context = LocalContext.current

    val permissionState= rememberMultiplePermissionsState(listOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA))


    LaunchedEffect(Unit){
        permissionState.launchMultiplePermissionRequest()
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview(),
        onResult = { bitmap ->
            if (bitmap != null) {
                val file = saveBitmapToFile(context, bitmap)
                onUpload?.let {
                    onUpload.invoke(file)
                }

            }
        }
    )

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            if (uri != null) {
                val file = saveUriToFile(context, uri)
                onUpload?.let {
                    onUpload.invoke(file)
                }
            }
        }
    )


    Box(
        modifier = Modifier
            .shadow(2.dp, RoundedCornerShape(10.dp), clip = true)
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colorScheme.onPrimary)
            .padding(vertical = 12.dp, horizontal = 12.dp)
            .fillMaxSize()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .height(70.dp)
                    .width(8.dp)
                    .background(Color.LightGray)
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = marker.locationName,
                            style = TextStyle(
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Normal,
                                fontSize = 20.sp
                            )
                        )
                    }
                }

                Column(modifier = Modifier.fillMaxWidth()) {
                    Row {
                        Text(
                            text = "Nama Jalan : ",
                            style = TextStyle(
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Light,
                                fontSize = 13.sp
                            )
                        )
                        Text(
                            text = marker.streetName,
                            style = TextStyle(
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Light,
                                fontSize = 13.sp
                            )
                        )
                    }
                }
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row {
                        Text(
                            text = "Longitude: ",
                            style = TextStyle(
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Light,
                                fontSize = 13.sp
                            )
                        )
                        Text(
                            text = "${marker.longitude}",
                            style = TextStyle(
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Light,
                                fontSize = 13.sp
                            )
                        )
                    }
                    Row {
                        Text(
                            text = "Latitude: ",
                            style = TextStyle(
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Light,
                                fontSize = 13.sp
                            )
                        )
                        Text(
                            text = "${marker.latitude}",
                            style = TextStyle(
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Light,
                                fontSize = 13.sp
                            )
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.End
                ) {
                    if (isEditable) {
                        Row {
                            IconButton(onClick = {
                                showAlert.value = "DELETE"
                                isShowDialog.value = true
                            }) {
                                Icon(
                                    imageVector = Icons.Outlined.Delete,
                                    tint = Color.Red,
                                    contentDescription = ""
                                )
                            }
                            IconButton(onClick = {
                                showAlert.value = "EDIT"
                                isShowDialog.value = true
                            }) {
                                Icon(
                                    imageVector = Icons.Outlined.Edit,
                                    tint = Color.Blue,
                                    contentDescription = ""
                                )
                            }
                            IconButton(onClick = {
                                showAlert.value = "GO TO"
                                isShowDialog.value = true
                            }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Outlined.ReadMore,
                                    tint = MaterialTheme.colorScheme.primary,
                                    contentDescription = ""
                                )
                            }
                        }
                    } else {
                        IconButton(onClick = {
                            showAlert.value = "GO TO"
                            isShowDialog.value = true
                        }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Outlined.ArrowForwardIos,
                                tint = MaterialTheme.colorScheme.primary,
                                contentDescription = ""
                            )
                        }
                    }
                }
            }

            // Handle Dialog Actions
            when (showAlert.value) {
                "DELETE" -> if (isShowDialog.value) {
                    DeleteMarkerDialog(onSubmit = {
                        onDelete?.invoke(marker)
                        isShowDialog.value = false
                    }, onDismis = {
                        isShowDialog.value = false
                    })
                }
                "EDIT" -> if (isShowDialog.value) {
                    EditMarkerDialog(
                        marker = marker,
                        location = Point.fromLngLat(marker.longitude, marker.latitude),
                        onDismis = { isShowDialog.value = false },
                        onSubmit = { mark ->
                            Log.d("mark", "${mark}")
                            onUpdate?.invoke(mark)
                            isShowDialog.value = false
                        },
                        onCameraClick = {
                            if(ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                                permissionState.launchMultiplePermissionRequest()
                            }else{
                                cameraLauncher.launch(null)
                            }
                        },
                        onGaleryClick = {
                            if(ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                                permissionState.launchMultiplePermissionRequest()
                            }else{
                                galleryLauncher.launch("image/*")
                            }
                        }

                    )
                }
                "GO TO" -> if (isShowDialog.value) {
                    isShowDialog.value = false
                    onClick() // Navigasi hanya dipanggil sekali
                }
            }
        }
    }
}


