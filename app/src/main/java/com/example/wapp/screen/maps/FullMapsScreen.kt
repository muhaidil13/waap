package com.example.wapp.screen.maps

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import com.example.wapp.MainViewModel
import com.example.wapp.R
import com.example.wapp.bitmapFromDrawableRes
import com.example.wapp.components.InputSugestions
import com.example.wapp.components.MarkerDialog
import com.example.wapp.components.SearchMarker
import com.example.wapp.data.Markers
import com.example.wapp.getStreetName
import com.example.wapp.hideKeyboard
import com.example.wapp.screen.auth.AuthViewModel
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.EdgeInsets
import com.mapbox.maps.MapInitOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.Style
import com.mapbox.maps.plugin.PuckBearing
import com.mapbox.maps.plugin.animation.MapAnimationOptions
import com.mapbox.maps.plugin.animation.camera
import com.mapbox.maps.plugin.animation.easeTo
import com.mapbox.maps.plugin.animation.flyTo
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager
import com.mapbox.maps.plugin.gestures.addOnMapLongClickListener
import com.mapbox.maps.plugin.locationcomponent.createDefault2DPuck
import com.mapbox.maps.plugin.locationcomponent.location
import com.mapbox.navigation.ui.maps.internal.ui.LocationComponent
import kotlinx.coroutines.launch


@ExperimentalMaterial3Api
@Composable
fun FullMapsScreen(mapsViewModel: MapsViewModel, navController: NavController, authViewModel: AuthViewModel, userLocation: Point){
    val marker by mapsViewModel.markers.collectAsState()
    val streetName by mapsViewModel.streetName.collectAsState()
    val userInfo by authViewModel.userInfo.collectAsState()

    val markerisAdded by mapsViewModel.markerIsAdded.collectAsState()
    val context = LocalContext.current

    val mapView = MapView(context)
    Log.d("Ini User Info", userInfo!!.role)



    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Show permission required message and navigate back
            Toast.makeText(context, "Memerlukan Isin Lokasi", Toast.LENGTH_LONG).show()
            navController.popBackStack()
            return@LaunchedEffect
        }

        mapsViewModel.getAllMarkers()
    }

    LaunchedEffect(marker) {
        marker?.let {
            loadMap(mapView, it, mapsViewModel)
        }
    }

    val key = context.getString(R.string.mapbox_access_token)
    mapView.mapboxMap.addOnMapLongClickListener{point ->
        val p = Point.fromLngLat(point.longitude(), point.latitude())
        userInfo?.let {
            if(it.role == "admin"){
                mapsViewModel.getStreetMarkersByGeometry(longitude = point.longitude(), latitude =point.latitude() , accessToken = key)
//                getStreetName(p, key, mapsViewModel)
                mapsViewModel.updateDestinationPosition(point)
                mapsViewModel.updateShowDialog(true)
                if(markerisAdded){
                    addMarker(mapView,point, mapsViewModel)
                }
            }
        }
        true
    }

    val selectedFeature by mapsViewModel.searchPlaceSelected.collectAsState()


    val showDialog by mapsViewModel.showDialog.collectAsState()
    val destinationPosition by mapsViewModel.destinationPoint.collectAsState()
    val showDesc by mapsViewModel.currentMarkersSelected.collectAsState()
    Scaffold(
        ){innerPadding ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(top = innerPadding.calculateTopPadding()))
        {

            marker?.let {  mark ->
                AndroidView(factory = {mapView}, modifier = Modifier.fillMaxSize())
            } ?: Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally){
                Text(text = "Loading")
            }
            Box(modifier = Modifier
                .fillMaxWidth()
                .height(350.dp)
                , contentAlignment = Alignment.TopCenter){
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp)
                    .shadow(
                        1.dp,
                        RoundedCornerShape(bottomEnd = 20.dp, bottomStart = 20.dp),
                        clip = true
                    )
                    .clip(RoundedCornerShape(bottomEnd = 20.dp, bottomStart = 20.dp))
                    .background(MaterialTheme.colorScheme.onPrimary)
                ){
                    Column(modifier = Modifier.padding(vertical = 10.dp, horizontal = 20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)){
                        marker?.let { markers ->
                            userInfo?.let {
                                SearchMarker(
                                    cartItems = markers,
                                    onItemSelected = { mark ->
                                        Log.d("Selected Marker", "Selected: ${mark.longitude}")
                                        mapsViewModel.selectedMarkers(mark)
                                        hideKeyboard(context)
                                    },
                                    mapView = mapView,
                                    mapsViewModel =mapsViewModel,
                                    role = it.role
                                )
                            }
                        }

                    }
                }
            }

            if(showDesc != null){
                showDesc?.let { mr ->
                    Box(modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 30.dp)
                        .padding(horizontal = 10.dp), contentAlignment = Alignment.BottomCenter){
                        Column(modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp)
                            .padding(bottom = 20.dp)
                            .shadow(1.dp, RoundedCornerShape(20.dp), clip = true)
                            .clip(RoundedCornerShape(10.dp))
                            .background(MaterialTheme.colorScheme.onPrimary)
                        ){
                            Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(10.dp)){
                                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)){
                                    Text(
                                        text = "Tempat : ", style = TextStyle(
                                            color = MaterialTheme.colorScheme.primary,
                                            fontWeight = FontWeight.SemiBold,
                                            fontSize = 17.sp
                                        )
                                    )
                                    Text(
                                        text = mr.locationName, style = TextStyle(
                                            color = MaterialTheme.colorScheme.primary,
                                            fontWeight = FontWeight.Normal,
                                            fontSize = 12.sp
                                        )
                                    )
                                }
                                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)){
                                    Text(
                                        text = "Nama jalan : ", style = TextStyle(
                                            color = MaterialTheme.colorScheme.primary,
                                            fontWeight = FontWeight.SemiBold,
                                            fontSize = 17.sp
                                        )
                                    )
                                    Text(
                                        text = mr.streetName, style = TextStyle(
                                            color = MaterialTheme.colorScheme.primary,
                                            fontWeight = FontWeight.Normal,
                                            fontSize = 12.sp
                                        )
                                    )
                                }
                                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)){
                                    Text(
                                        text = "Ditandai Sebagai : ", style = TextStyle(
                                            color = MaterialTheme.colorScheme.primary,
                                            fontWeight = FontWeight.SemiBold,
                                            fontSize = 17.sp
                                        )
                                    )
                                    Text(
                                        text = mr.type, style = TextStyle(
                                            color = MaterialTheme.colorScheme.primary,
                                            fontWeight = FontWeight.Normal,
                                            fontSize = 14.sp
                                        )
                                    )
                                }

                                Spacer(modifier = Modifier.height(10.dp))
                                Button(onClick = {
                                    navController.navigate("Detail/${mr.id}")
                                }) {
                                    Row(horizontalArrangement = Arrangement.spacedBy(14.dp), verticalAlignment = Alignment.CenterVertically){
                                        Icon(
                                            imageVector = Icons.Filled.Navigation,
                                            tint = MaterialTheme.colorScheme.onPrimary,
                                            modifier = Modifier.size(25.dp),
                                            contentDescription = ""
                                        )
                                        Text(
                                            text = "Lihat Sekarang !", style = TextStyle(
                                                color = MaterialTheme.colorScheme.onPrimary,
                                                fontWeight = FontWeight.Normal,
                                                fontSize = 13.sp
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        if(selectedFeature != null){
            selectedFeature?.let { feature ->
                val Point = Point.fromLngLat(feature.longitude, feature.latitude)
                MarkerDialog(location = Point ,streetName = feature.streetName, locationName =feature.locationName,   onDismis = {
                    mapsViewModel.updateSearchPlaceSelected(null)
                }) { markers ->
                    mapsViewModel.saveMarkerToDatabase(markers , context)
                    mapsViewModel.updateSearchPlaceSelected(null)
                    mapsViewModel.getAllMarkers()

                }
            }
        }
        if(showDialog && destinationPosition != null){
            destinationPosition?.let { location ->
                streetName?.let {
                    MarkerDialog(location = location ,streetName = it,  onDismis = {
                        mapsViewModel.updateShowDialog(false)
                        mapsViewModel.updateDestinationPosition(null)
                        mapsViewModel.updateStatusmarkerIsAdded(false)
                    }) { markers ->
                        mapsViewModel.saveMarkerToDatabase(markers , context)
                        mapsViewModel.getAllMarkers()
                        mapsViewModel.updateShowDialog(false)
                        mapsViewModel.updateStatusmarkerIsAdded(false)
                    }
                }
            }

        }
    }
}
fun showMarker(
    mapView: MapView,
    point: Point,
    mapsViewModel: MapsViewModel,
    iconLabel: String = "marker-icon-id",
    marker: Markers? = null
) {
    val pointManager = mapView.annotations.createPointAnnotationManager()
    val annotationOptions = PointAnnotationOptions()
        .withPoint(Point.fromLngLat(point.longitude(), point.latitude()))
        .withIconImage(iconLabel)

    val mark = pointManager.create(annotationOptions)
    pointManager.addClickListener { clickMarker ->
        marker?.let {  s ->
            if (mark.id == clickMarker.id) {
                mapsViewModel.selectedMarkers(s)
                updateCamera(mapView = mapView, point = point)
            } else {
                mapsViewModel.selectedMarkers(null)
            }
        }
        true
    }
}

private fun updateCamera(
    mapView: MapView,
    point: Point,
    LOG_TAG: String = "testMapbox",
    bearing: Double? = 90.0,
    zoomLevel: Double? = 18.9,
    startDelay: Long? = 500L
) {
    val mapAnimationOptions = MapAnimationOptions.Builder()
        .startDelay(startDelay ?: 800L)
        .build()

    mapView.apply {
            mapView.camera.easeTo(
                CameraOptions.Builder()
                    .center(point)
                    .pitch(45.0)
                    .bearing(bearing ?: 90.0)
                    .zoom(zoomLevel ?: 18.9)
                    .padding(EdgeInsets(0.0, 0.0, 0.0, 0.0))
                    .build(),
                mapAnimationOptions
            )

    }
}

fun updateMapLocation(mapView: MapView, longitude: Double, latitude: Double) {
    val mapboxMap = mapView.mapboxMap
    mapboxMap.flyTo(
        CameraOptions.Builder()
            .center(Point.fromLngLat(longitude, latitude)) // Set center to the desired coordinates
            .zoom(20.0)  // Set the zoom level
            .build()
    )

}


private fun loadMap(mapView: MapView,  markers: List<Markers>, mapsViewModel: MapsViewModel){
    mapView.mapboxMap.loadStyle(Style.MAPBOX_STREETS){ style ->
        mapView.location.apply {
            puckBearing  = PuckBearing.COURSE
            locationPuck = createDefault2DPuck(withBearing = true)
            puckBearingEnabled = true
            enabled = true
            addOnIndicatorPositionChangedListener{ location ->
                updateMapLocation(mapView,location.longitude(), location.latitude())
            }
        }
        bitmapFromDrawableRes(mapView.context, R.drawable.red_mark)?.let{ bitmap ->
            style.addImage("marker-icon-id", bitmap)
            Log.d("Marker.kt Status", "marker-icon-id add")
        }?: Log.d("Marker.kt Status", "Tidak muncul")
    }


    mapView.mapboxMap.subscribeMapLoaded {
        // Add markers to the map after it's loaded
        markers.forEachIndexed { index, p ->
            val point = Point.fromLngLat(p.longitude, p.latitude)
            Log.d("Marker.kt Status", "kulinerMarkers ${p.longitude} ${p.latitude}")
            showMarker(mapView = mapView, point = point, mapsViewModel = mapsViewModel, marker = p)
        }

//        // Optionally fly to a location (you can update with your logic)
//        // Example: Fly to a specific coordinate or the user's location
//        mapView.mapboxMap.flyTo(
//            CameraOptions.Builder()
//                .center(Point.fromLngLat(markers.firstOrNull()?.longitude ?: 106.84513, markers.firstOrNull()?.latitude ?: -6.21462))
//                .zoom(14.0)  // Set desired zoom level
//                .build()
//        )
    }
}

