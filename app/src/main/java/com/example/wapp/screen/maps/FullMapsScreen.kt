package com.example.wapp.screen.maps

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.example.wapp.MainViewModel
import com.example.wapp.R
import com.example.wapp.bitmapFromDrawableRes
import com.example.wapp.components.MarkerDialog
import com.example.wapp.data.Markers
import com.example.wapp.screen.auth.AuthViewModel
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.EdgeInsets
import com.mapbox.maps.MapInitOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.Style
import com.mapbox.maps.plugin.PuckBearing
import com.mapbox.maps.plugin.animation.MapAnimationOptions
import com.mapbox.maps.plugin.animation.easeTo
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager
import com.mapbox.maps.plugin.gestures.addOnMapLongClickListener
import com.mapbox.maps.plugin.locationcomponent.createDefault2DPuck
import com.mapbox.maps.plugin.locationcomponent.location


@Composable
fun FullMapsScreen(mapsViewModel: MapsViewModel, navController: NavController, authViewModel: AuthViewModel){
    val userLocation by mapsViewModel.startPosition.collectAsState()
    val marker by mapsViewModel.markers.collectAsState()
    val userInfo by authViewModel.userInfo.collectAsState()

    LaunchedEffect(Unit) {
        mapsViewModel.getAllMarkers()
    }

    val markerisAdded by mapsViewModel.markerIsAdded.collectAsState()
    val context = LocalContext.current

    val initialCamera: CameraOptions = CameraOptions
        .Builder()
        .center(Point.fromLngLat(userLocation.longitude(), userLocation.latitude()))
        .zoom(17.0)
        .pitch(20.0)
        .bearing(30.0)
        .build()


    val mapInitOption = MapInitOptions(context, cameraOptions = initialCamera,  styleUri = Style.MAPBOX_STREETS)
    val mapView = MapView(context, mapInitOption)



    Log.d("Ini User Info", userInfo!!.role)



    LaunchedEffect(Unit) {
        if(ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            Toast.makeText(context, "Memerlukan Isin Lokasi", Toast.LENGTH_LONG).show()
            navController.popBackStack()
        }
    }
    LaunchedEffect( marker, marker, mapView, mapsViewModel) {
        marker?.let {
            loadMap(mapView, it, mapsViewModel)
        }
    }


    mapView.mapboxMap.addOnMapLongClickListener{point ->
        userInfo?.let {
            if(it.role == "admin"){
                Log.d(mapsViewModel.LOG_TAG, "Marker.kt Custom: latitude is ${point.latitude()} longitude ${point.longitude()} ")
                mapsViewModel.updateDestinationPosition(point)
                mapsViewModel.updateShowDialog(true)
                if(markerisAdded){
                    addMarker(mapView,point, mapsViewModel)
                }
            }
        }
        true
    }


    val showDialog by mapsViewModel.showDialog.collectAsState()
    val destinationPosition by mapsViewModel.destinationPoint.collectAsState()

    Scaffold(
        
        ){innerPadding ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(top = innerPadding.calculateTopPadding())){
            AndroidView(factory = {mapView})
            Box(modifier = Modifier
                .fillMaxSize()
                .padding(end = 20.dp, bottom = 20.dp), contentAlignment = Alignment.BottomEnd){
                Column(
                    verticalArrangement = Arrangement.spacedBy(30.dp)
                ){
                    IconButton(onClick = {
                        marker?.let {
                            loadMap(mapView, it, mapsViewModel)
                        }
                        
                    }) {
                        Icon(imageVector = Icons.Filled.Refresh, contentDescription = "")
                    }
                    IconButton(onClick = {


                    }) {
                        Icon(imageVector = Icons.Filled.LocationOn, contentDescription = "")
                    }
                }
            }


        }
        if(showDialog && destinationPosition != null){
            destinationPosition?.let {
                MarkerDialog(location = it , onDismis = {
                    mapsViewModel.updateShowDialog(false)
                    mapsViewModel.updateDestinationPosition(null)
                    mapsViewModel.updateStatusmarkerIsAdded(false)
                }) { markers ->
                    mapsViewModel.saveMarkerToDatabase(markers , context)
                    mapsViewModel.updateShowDialog(false)

                    marker?.let { m ->
                        loadMap(mapView, m, mapsViewModel)
                    }

                    mapsViewModel.updateStatusmarkerIsAdded(false)
                }
            }

        }
    }
}
private fun addMarker(mapView: MapView, point: Point, mapsViewModel: MapsViewModel, iconLabel:String = "marker-icon-id"){
    val pointManager = mapView.annotations.createPointAnnotationManager()
    val annotationOptions = PointAnnotationOptions()
        .withPoint(Point.fromLngLat(point.longitude(), point.latitude()))
        .withIconImage(iconLabel)

    val  mark = pointManager.create(annotationOptions)
    pointManager.addClickListener{clickMarker ->
        if(mark.id == clickMarker.id){
            Toast.makeText(mapView.context, "Marker.kt ${point.longitude()}", Toast.LENGTH_LONG ).show()
            updateCamera(mapView = mapView, point)
        }
        true
    }
}

private fun updateCamera(
    mapView: MapView,
    point: Point?,
    bearing: Double? = 90.0,
    zoomLevel: Double? = 18.9,
    startDelay: Long = 500L
) {

    point?.let {
        mapView.mapboxMap.easeTo(
            CameraOptions.Builder()
                .center(point)
                .pitch(45.0)
                .bearing(bearing)
                .zoom(zoomLevel)
                .padding(EdgeInsets(0.0, 0.0, 0.0, 0.0))
                .build(),
            MapAnimationOptions.Builder()
                .duration(200L) // Adjust duration as needed for smoother effect
                .startDelay(startDelay)
                .build()
        )
    }
}

private fun loadMap(mapView: MapView,  markers: List<Markers>, mapsViewModel: MapsViewModel){
    mapView.mapboxMap.loadStyle(Style.MAPBOX_STREETS){ style ->
        mapView.location.apply {
            puckBearing  = PuckBearing.COURSE
            locationPuck = createDefault2DPuck(withBearing = true)
            puckBearingEnabled = true
            enabled = true
        }
        bitmapFromDrawableRes(mapView.context, R.drawable.red_mark)?.let{ bitmap ->
            style.addImage("marker-icon-id", bitmap)
            Log.d("Marker.kt Status", "marker-icon-id add")
        }?: Log.d("Marker.kt Status", "Tidak muncul")
        bitmapFromDrawableRes(mapView.context, R.drawable.ic_launcher_foreground)?.let{ bitmap ->
            style.addImage("marker-icon-id-2", bitmap)
            Log.d("Marker.kt Status", "marker-icon-id add")
        }?: Log.d("Marker.kt Status", "Tidak muncul")
    }

    markers.forEach {
        val point = Point.fromLngLat(it.longitude, it.latitude)
        Log.d("Marker.kt Status", "kulinerMarkers ${it.longitude} ${it.latitude}")

        when(it.type){
            "kuliner" -> {
                addMarker(mapView,point, mapsViewModel)
            }
            "wisata" -> {
                addMarker(mapView,point, mapsViewModel,"marker-icon-id-2")
            }
        }
    }
}
