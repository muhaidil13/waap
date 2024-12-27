package com.example.wapp.screen.maps

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import com.example.wapp.R
import com.example.wapp.RouteApp
import com.example.wapp.bitmapFromDrawableRes
import com.example.wapp.data.Markers
import com.mapbox.api.directions.v5.models.RouteOptions
import com.mapbox.bindgen.Expected
import com.mapbox.common.location.Location
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.EdgeInsets
import com.mapbox.maps.MapInitOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.Style
import com.mapbox.maps.plugin.PuckBearing
import com.mapbox.maps.plugin.animation.MapAnimationOptions
import com.mapbox.maps.plugin.animation.camera
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorPositionChangedListener
import com.mapbox.maps.plugin.locationcomponent.createDefault2DPuck
import com.mapbox.maps.plugin.locationcomponent.location
import com.mapbox.navigation.base.TimeFormat
import com.mapbox.navigation.base.extensions.applyDefaultNavigationOptions
import com.mapbox.navigation.base.extensions.applyLanguageAndVoiceUnitOptions
import com.mapbox.navigation.base.formatter.DistanceFormatterOptions
import com.mapbox.navigation.base.route.NavigationRoute
import com.mapbox.navigation.base.route.NavigationRouterCallback
import com.mapbox.navigation.base.route.RouterFailure
import com.mapbox.navigation.base.trip.model.RouteProgress
import com.mapbox.navigation.core.directions.session.RoutesObserver
import com.mapbox.navigation.core.formatter.MapboxDistanceFormatter
import com.mapbox.navigation.core.trip.session.LocationMatcherResult
import com.mapbox.navigation.core.trip.session.LocationObserver
import com.mapbox.navigation.core.trip.session.RouteProgressObserver
import com.mapbox.navigation.core.trip.session.VoiceInstructionsObserver
import com.mapbox.navigation.tripdata.maneuver.api.MapboxManeuverApi
import com.mapbox.navigation.tripdata.progress.api.MapboxTripProgressApi
import com.mapbox.navigation.tripdata.progress.model.DistanceRemainingFormatter
import com.mapbox.navigation.tripdata.progress.model.EstimatedTimeToArrivalFormatter
import com.mapbox.navigation.tripdata.progress.model.PercentDistanceTraveledFormatter
import com.mapbox.navigation.tripdata.progress.model.TimeRemainingFormatter
import com.mapbox.navigation.tripdata.progress.model.TripProgressUpdateFormatter
import com.mapbox.navigation.ui.base.util.MapboxNavigationConsumer
import com.mapbox.navigation.ui.components.maneuver.view.MapboxManeuverView
import com.mapbox.navigation.ui.components.maps.camera.view.MapboxRecenterButton
import com.mapbox.navigation.ui.components.maps.camera.view.MapboxRouteOverviewButton
import com.mapbox.navigation.ui.components.tripprogress.view.MapboxTripProgressView
import com.mapbox.navigation.ui.components.voice.view.MapboxSoundButton
import com.mapbox.navigation.ui.maps.camera.NavigationCamera
import com.mapbox.navigation.ui.maps.camera.data.MapboxNavigationViewportDataSource
import com.mapbox.navigation.ui.maps.camera.lifecycle.NavigationBasicGesturesHandler
import com.mapbox.navigation.ui.maps.camera.state.NavigationCameraState
import com.mapbox.navigation.ui.maps.camera.transition.NavigationCameraTransitionOptions
import com.mapbox.navigation.ui.maps.location.NavigationLocationProvider
import com.mapbox.navigation.ui.maps.route.RouteLayerConstants.TOP_LEVEL_ROUTE_LINE_LAYER_ID
import com.mapbox.navigation.ui.maps.route.arrow.api.MapboxRouteArrowApi
import com.mapbox.navigation.ui.maps.route.arrow.api.MapboxRouteArrowView
import com.mapbox.navigation.ui.maps.route.arrow.model.RouteArrowOptions
import com.mapbox.navigation.ui.maps.route.line.api.MapboxRouteLineApi
import com.mapbox.navigation.ui.maps.route.line.api.MapboxRouteLineView
import com.mapbox.navigation.ui.maps.route.line.model.MapboxRouteLineApiOptions
import com.mapbox.navigation.ui.maps.route.line.model.MapboxRouteLineViewOptions
import com.mapbox.navigation.voice.api.MapboxSpeechApi
import com.mapbox.navigation.voice.api.MapboxVoiceInstructionsPlayer
import com.mapbox.navigation.voice.model.SpeechAnnouncement
import com.mapbox.navigation.voice.model.SpeechError
import com.mapbox.navigation.voice.model.SpeechValue
import com.mapbox.navigation.voice.model.SpeechVolume
import java.text.SimpleDateFormat
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale


@Composable
fun MapNavigate(mapsViewModel: MapsViewModel, userId: String,latitude:Double, longitude:Double, id:String, navController: NavController) {

    val marker = mapsViewModel.marker.collectAsState()
    val navData = mapsViewModel.navigationHistory.collectAsState()
    val nameStreet by mapsViewModel.streetName.collectAsState()
    val context = LocalContext.current
    val key = context.getString(R.string.mapbox_access_token)
    LaunchedEffect(Unit){
        mapsViewModel.getMarkersById(id)
        mapsViewModel.getStreetMarkersByGeometry(longitude, latitude, key)
    }
    val isStart = mapsViewModel.isStart.collectAsState()

    val navigationOn = remember {
        mutableStateOf(false)
    }

    val  durationRemaining = remember {
        mutableStateOf("")
    }


    val  streetName = remember {
        mutableStateOf("")
    }

    val totaldistanceRemaining = remember {
        mutableStateOf("")

    }




    LaunchedEffect(navigationOn.value){

        if (navigationOn.value){
            val startP = Point.fromLngLat(longitude, latitude)
            mapsViewModel.createNavigation(userId= userId,
                idMarker=id,
                locationPoint = startP,
                jarak=totaldistanceRemaining.value,
                durationRemaining= durationRemaining.value,
                startStreetName= nameStreet ?: streetName.value ,
                context = context
            )
        }else{

        }
    }

    val mapboxNavigation = mapsViewModel.navigation
    val startPosition = Point.fromLngLat(longitude, latitude)
    val initialCamera: CameraOptions = CameraOptions
        .Builder()
        .center(startPosition)
        .zoom(20.0)
        .pitch(20.0)
        .build()


    val mapInitOption = MapInitOptions(context, cameraOptions = initialCamera,  styleUri = Style.MAPBOX_STREETS)
    val mapView = MapView(context, mapInitOption)
    val permissionLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) { grand ->
        if(!grand){
            Toast.makeText(mapView.context, "Requred Location Permission", Toast.LENGTH_SHORT).show()
        }
    }


    val con = LocalContext.current

    val maneuverView = MapboxManeuverView(con)
    val tripProgressView = MapboxTripProgressView(con)
    val soundButton = MapboxSoundButton(con)
    val routeOverview = MapboxRouteOverviewButton(con)

    val isVoiceInstructionsMuted  = remember {
        mutableStateOf(false)
    }



    val recenter = MapboxRecenterButton(con)

    // ViewDataSource
    val viewDataSource = MapboxNavigationViewportDataSource(mapView.mapboxMap)


    // Manufer API
    val distanceFormatterOptions = DistanceFormatterOptions.Builder(context).build()
    val manuverApi = MapboxManeuverApi(
        MapboxDistanceFormatter(distanceFormatterOptions)
    )

    // tripProgressApi
    val tripProgressApi = MapboxTripProgressApi(
        TripProgressUpdateFormatter.Builder(context)
            .timeRemainingFormatter(TimeRemainingFormatter(context))
            .distanceRemainingFormatter(DistanceRemainingFormatter(distanceFormatterOptions))
            .percentRouteTraveledFormatter(PercentDistanceTraveledFormatter())
            .estimatedTimeToArrivalFormatter(EstimatedTimeToArrivalFormatter(context, TimeFormat.NONE_SPECIFIED))
            .build()
    )

    val speechApi = MapboxSpeechApi(context, Locale("id").language)
    val voiceInstructionsPlayer = MapboxVoiceInstructionsPlayer(context, Locale("id").language)

    val voiceInstructionsPlayerCallback = MapboxNavigationConsumer<SpeechAnnouncement> { value ->
        speechApi.clean(value)
    }
    val speechCallback =  MapboxNavigationConsumer<Expected<SpeechError, SpeechValue>> { expected ->
        expected.fold({error ->
            voiceInstructionsPlayer.play(error.fallback, voiceInstructionsPlayerCallback)
        }, {value ->
            voiceInstructionsPlayer.play(value.announcement, voiceInstructionsPlayerCallback)
        })
    }

    val voiceInstructionsObserver = VoiceInstructionsObserver{voiceInstructions ->
        speechApi.generate(voiceInstructions, speechCallback)
    }


    if(isVoiceInstructionsMuted.value){
        voiceInstructionsPlayer.volume(SpeechVolume(0f))
    }else{
        voiceInstructionsPlayer.volume(SpeechVolume(1f))
    }




    val routeLineViewOption: MapboxRouteLineViewOptions = MapboxRouteLineViewOptions.Builder(context).build()
    val routeLineView =  MapboxRouteLineView(routeLineViewOption)

    val navigationLocationProvider = NavigationLocationProvider()
    val routeLineApiOptions: MapboxRouteLineApiOptions = MapboxRouteLineApiOptions
        .Builder()
        .vanishingRouteLineEnabled(true)
        .build()


    val routeArrowApi = MapboxRouteArrowApi()
    val routeArrowApiOptions: RouteArrowOptions = RouteArrowOptions.Builder(context).withSlotName(
        TOP_LEVEL_ROUTE_LINE_LAYER_ID
    ).build()

    val routeArrowView = MapboxRouteArrowView(routeArrowApiOptions)
    val routeLineApi= MapboxRouteLineApi(routeLineApiOptions)


    val routesObserver = RoutesObserver{routeUpdateResult ->
        if(routeUpdateResult.navigationRoutes.isNotEmpty()){
            routeLineApi.setNavigationRoutes(
                routeUpdateResult.navigationRoutes
            ){values ->
                mapView.mapboxMap.style?.apply {
                    routeLineView.renderRouteDrawData(this, values)
                }
            }
            viewDataSource.onRouteChanged(routeUpdateResult.navigationRoutes.first())
            viewDataSource.evaluate()
        }else{
            val style = mapView.mapboxMap.style
            if(style != null){
                routeLineApi.clearRouteLine{value ->
                    routeLineView.renderClearRouteLineValue(style, value)
                }
                routeArrowView.render(style, routeArrowApi.clearArrows())

            }
        }
        viewDataSource.clearRouteData()
        viewDataSource.evaluate()
    }

    val onPositionChange = OnIndicatorPositionChangedListener{point->
        val result = routeLineApi.updateTraveledRouteLine(point)
        mapView.mapboxMap.style?.apply {
            routeLineView.renderRouteLineUpdate(this, result)
        }
    }
    val calendar = Calendar.getInstance()


    val routeProgressObserver  = RouteProgressObserver{routeProgress: RouteProgress ->

        if (routeProgress.remainingWaypoints == 0) {
            mapboxNavigation.stopTripSession()
            mapboxNavigation.setNavigationRoutes(listOf())
        }
        viewDataSource.onRouteProgressChanged(routeProgress)
        viewDataSource.evaluate()

        val style = mapView.mapboxMap.style
        if(style != null){
            val arrowUpdate = routeArrowApi.addUpcomingManeuverArrow(routeProgress)
            routeArrowView.renderManeuverUpdate(style, arrowUpdate)
        }

        val maneuvers  = manuverApi.getManeuvers(routeProgress)
        maneuvers .fold({error ->
            Toast.makeText(context, "${error.errorMessage}", Toast.LENGTH_SHORT).show()
        }, {

            maneuverView.visibility = View.VISIBLE
            maneuverView.renderManeuvers(maneuvers)
        })

        tripProgressView.render(
            tripProgressApi.getTripProgress(routeProgress)
        )


        val durationR = routeProgress.durationRemaining

        calendar.add(Calendar.SECOND, durationR.toInt())

        val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

        val currentLegProgress = routeProgress.currentLegProgress
        val currentStepProgress = currentLegProgress?.currentStepProgress
        val currentStep = currentStepProgress?.step

        if (currentStep != null) {

            val estimatedArrivalTime = dateFormat.format(calendar.time)
            Log.d("RouteInfo", "Estimated Arrival Time: $estimatedArrivalTime")

            val distanceRemaining = routeProgress.distanceRemaining / 1000
            Log.d("RouteInfo", "Distance Remaining: $distanceRemaining km")

            val streetN = currentStep.name() ?: "No Name"
            Log.d("RouteInfo", "Street Name: $streetName")

            durationRemaining.value = estimatedArrivalTime
            totaldistanceRemaining.value = distanceRemaining.toString()
            streetName.value = streetN
        } else {
            Log.d("RouteInfo", "No current step available.")
        }






        navigationOn.value = true
        routeLineApi.updateWithRouteProgress(routeProgress){result ->
            mapView.mapboxMap.style?.apply {
                routeLineView.renderRouteLineUpdate(this, result)
            }
        }
    }


    val navigationCamera = NavigationCamera(
        mapView.mapboxMap,
        mapView.camera,
        viewDataSource
    )


    recenter.setOnClickListener{
        navigationCamera.requestNavigationCameraToFollowing()
        routeOverview.showTextAndExtend(1500L)
    }
    routeOverview.setOnClickListener{
        navigationCamera.requestNavigationCameraToOverview()
        recenter.showTextAndExtend(1500L)
    }
    soundButton.setOnClickListener{
        isVoiceInstructionsMuted.value = !isVoiceInstructionsMuted.value
    }
    soundButton.unmute()

    mapView.camera.addCameraAnimationsLifecycleListener(
        NavigationBasicGesturesHandler(navigationCamera)
    )

    navigationCamera.registerNavigationCameraStateChangeObserver{navigationCameraState ->
        when(navigationCameraState){
            NavigationCameraState.IDLE,
            NavigationCameraState.TRANSITION_TO_FOLLOWING -> recenter.visibility = View.INVISIBLE
            NavigationCameraState.FOLLOWING,
            NavigationCameraState.TRANSITION_TO_OVERVIEW,
            NavigationCameraState.OVERVIEW -> recenter.visibility = View.VISIBLE
        }
    }



    val locationObserver = object: LocationObserver {
        var firstLocationUpdateReceived = false
        override fun onNewLocationMatcherResult(locationMatcherResult: LocationMatcherResult) {
            val enhancedLocation = locationMatcherResult.enhancedLocation
            navigationLocationProvider.changePosition(enhancedLocation,locationMatcherResult.keyPoints)
            viewDataSource.onLocationChanged(enhancedLocation)
            viewDataSource.evaluate()
            if(!firstLocationUpdateReceived){
                firstLocationUpdateReceived = true
                navigationCamera.requestNavigationCameraToOverview(
                    stateTransitionOptions = NavigationCameraTransitionOptions.Builder()
                        .maxDuration(300L)
                        .build()
                )
            }
            updateCamera(mapView =  mapView, point= Point.fromLngLat(enhancedLocation.longitude, enhancedLocation.latitude), bearing = enhancedLocation.bearing)

        }

        override fun onNewRawLocation(rawLocation: Location) {

        }

    }

    fun stopNavigate(){
        mapboxNavigation.stopTripSession()
        mapboxNavigation.setNavigationRoutes(emptyList())
    }
    fun fetchRoute(){
        marker.value?.let {
            val destinationPosition = Point.fromLngLat(it.longitude, it.latitude)
            mapboxNavigation.requestRoutes(
                RouteOptions.builder()
                    .applyDefaultNavigationOptions()
                    .applyLanguageAndVoiceUnitOptions(context)
                    .coordinatesList(listOf(startPosition,destinationPosition))
                    .layersList(listOf(mapboxNavigation.getZLevel(), null))
                    .build(),
                object : NavigationRouterCallback {
                    override fun onCanceled(routeOptions: RouteOptions, routerOrigin: String) {

                    }

                    override fun onFailure(reasons: List<RouterFailure>, routeOptions: RouteOptions) {

                    }

                    override fun onRoutesReady(routes: List<NavigationRoute>, routerOrigin: String) {
                        mapboxNavigation.setNavigationRoutes(routes)

                        soundButton.visibility = View.VISIBLE
                        routeOverview.visibility = View.VISIBLE

                        mapView.mapboxMap.style?.apply {
                            routeLineView.hideAlternativeRoutes(this)
                        }
                        navigationCamera.requestNavigationCameraToOverview()
                    }

                }
            )
        }

    }

    val lifecycleOwner = LocalLifecycleOwner.current
    lifecycleOwner.lifecycle.addObserver(LifecycleEventObserver{ _, event ->
        when(event){
            Lifecycle.Event.ON_CREATE -> {}
            Lifecycle.Event.ON_START -> {}
            Lifecycle.Event.ON_RESUME -> {
                mapsViewModel.navigation.registerRoutesObserver(routesObserver)
                mapsViewModel.navigation.registerLocationObserver(locationObserver)
                mapsViewModel.navigation.registerRouteProgressObserver(routeProgressObserver)
                mapsViewModel.navigation.registerVoiceInstructionsObserver(voiceInstructionsObserver)
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
                    if(ActivityCompat.checkSelfPermission(mapView.context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED){
                        permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                    }
                }
                mapsViewModel.navigation.startTripSession()
            }
            Lifecycle.Event.ON_PAUSE -> {

            }
            Lifecycle.Event.ON_STOP -> {

            }
            Lifecycle.Event.ON_DESTROY -> {
                mapsViewModel.navigation.unregisterRoutesObserver(routesObserver)
                mapsViewModel.navigation.unregisterLocationObserver(locationObserver)
                mapsViewModel.navigation.unregisterVoiceInstructionsObserver(voiceInstructionsObserver)
                mapsViewModel.navigation.unregisterRouteProgressObserver(routeProgressObserver)
                mapsViewModel.navigation.stopTripSession()

            }
            Lifecycle.Event.ON_ANY -> {

            }
        }
    })
    mapView.mapboxMap.loadStyle(Style.MAPBOX_STREETS){ style ->
        routeLineView.initializeLayers(style)
        bitmapFromDrawableRes(mapView.context, R.drawable.red_mark)?.let{ bitmap ->
            style.addImage("marker-icon-id", bitmap)
        }
        mapView.location.apply {
            setLocationProvider(navigationLocationProvider)
            addOnIndicatorPositionChangedListener(onPositionChange)
            puckBearing  = PuckBearing.COURSE
            locationPuck = createDefault2DPuck(withBearing = true)
            puckBearingEnabled = true
            enabled = true
        }
    }
    marker.value?.let {
        val destinationPosition = Point.fromLngLat(it.longitude, it.latitude)
        addMarker(mapView, destinationPosition, mapsViewModel)

    }


    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(
                mapView.context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }

    }

    Scaffold(

    ) { innerPadding ->

        Box( modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)){
            AndroidView(factory = { mapView
            })
            AndroidView(factory = { tripProgressView })
            if(isStart.value){
                BoxWithConstraints(modifier = Modifier
                    .fillMaxSize(), contentAlignment = Alignment.TopStart){
                    val maxW = maxWidth
                    marker.value?.let { marker ->
                        Column (modifier = Modifier
                            .width(maxW * .8f)
                            .clip(
                                RoundedCornerShape(20.dp)
                            )
                            .background(MaterialTheme.colorScheme.onPrimary)){
                            Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(10.dp))
                            {
                            navData.value?.let {
                                navd ->
                                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)){
                                        Text(
                                            text = "Tujuan Anda",
                                            style = TextStyle(
                                                color = MaterialTheme.colorScheme.primary,
                                                fontSize = 15.sp,
                                                fontWeight = FontWeight.Normal
                                            )
                                        )
                                        Text(
                                            text = marker.locationName,
                                            style = TextStyle(
                                                color = MaterialTheme.colorScheme.primary,
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Normal
                                            )
                                        )
                                    }

                                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                        Text(
                                            text = "Total Jarak(KM) : ",
                                            style = TextStyle(
                                                color = MaterialTheme.colorScheme.primary,
                                                fontSize = 15.sp,
                                                fontWeight = FontWeight.Normal
                                            )
                                        )
                                        Text(
                                            text = navd.totalJarak + "KM",
                                            style = TextStyle(
                                                color = MaterialTheme.colorScheme.primary,
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Normal
                                            )
                                        )
                                    }

                                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                        Text(
                                            text = "Perkiraan Sampai : ",
                                            style = TextStyle(
                                                color = MaterialTheme.colorScheme.primary,
                                                fontSize = 15.sp,
                                                fontWeight = FontWeight.Normal
                                            )
                                        )
                                        Text(
                                            text = navd.totalWaktu,
                                            style = TextStyle(
                                                color = MaterialTheme.colorScheme.primary,
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Normal
                                            )
                                        )
                                    }
                                    Text(
                                        text = "Lokasi Sekarang : ",
                                        style = TextStyle(
                                            color = MaterialTheme.colorScheme.primary,
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Normal
                                        )
                                    )
                                    Text(
                                        text = streetName.value,
                                        style = TextStyle(
                                            color = MaterialTheme.colorScheme.primary,
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    )
                                }
                            }
                        }
                    }

                }
            }
            Box(modifier =  Modifier
                .fillMaxSize(), contentAlignment = Alignment.BottomCenter){
                if (!isStart.value){
                    Button(
                        onClick = {
                            mapsViewModel.updateStartNavigation(true)
                            fetchRoute()

                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(text = "Mulai")
                    }
                }else{
                    Button(
                        onClick = {
                            navData.value?.let {
                                stopNavigate()
                                mapsViewModel.stopUserNavigation(it, context)
                                navController.navigate(RouteApp.NavigasiSuccess.routeid)
                            }

                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(text = "Selesai")
                    }
                }

            }
        }
    }
}

fun addMarker(mapView: MapView, point: Point, mapsViewModel: MapsViewModel, markers: Markers?=null){
    val pointManager = mapView.annotations.createPointAnnotationManager()
    val annotationOptions = PointAnnotationOptions()
        .withPoint(Point.fromLngLat(point.longitude(), point.latitude()))
        .withIconImage("marker-icon-id")

    val  mark = pointManager.create(annotationOptions)
    pointManager.addClickListener{clickMarker ->
        if(mark.id == clickMarker.id){
            Toast.makeText(mapView.context, "Marker ${point.longitude()}", Toast.LENGTH_LONG ).show()

            updateCamera(mapView = mapView, point = point, mapsViewModel.LOG_TAG)
        }
        Toast.makeText(mapView.context, "Sukses Menambah Marker", Toast.LENGTH_SHORT).show()

        true
    }
    mapsViewModel.updateStatusmarkerIsAdded(false)
}
private fun updateCamera(mapView: MapView, point: Point, LOG_TAG: String = "testMapbox", bearing: Double? = 90.0, zoomLevel:Double? = 18.9, startDelay:Long? = 500L){
    val mapAnimationOptionsBuilder = MapAnimationOptions.Builder().startDelay( startDelay ?: 800L)
    val isvalid =  mapView.mapboxMap.isValid()
    if(isvalid){
        Log.d(LOG_TAG, "${isvalid}")
        mapView.apply {
            mapView.camera.easeTo(
                CameraOptions.Builder()
                    .center(point)
                    .pitch(45.0)
                    .bearing(bearing)
                    .zoom(zoomLevel)
                    .padding(EdgeInsets(0.0, 0.0, 0.0, 0.0))
                    .build(),
                mapAnimationOptionsBuilder.build()
            )
        }
    }else{
        Log.d(LOG_TAG, "${isvalid}")
    }
}


