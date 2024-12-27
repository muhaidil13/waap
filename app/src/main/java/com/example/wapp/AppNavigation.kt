package com.example.wapp

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Point
import android.location.LocationManager
import android.provider.Settings
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.wapp.data.NavigationHistory
import com.example.wapp.screen.components.BottomMenuScreen
import com.example.wapp.screen.AuthScreen
import com.example.wapp.screen.maps.FullMapsScreen
import com.example.wapp.screen.HomeScreen
import com.example.wapp.screen.IntroScreen
import com.example.wapp.screen.KulinerScreen
import com.example.wapp.screen.ProfileScreen
import com.example.wapp.screen.auth.SignInScreen
import com.example.wapp.screen.auth.SignUpScreen
import com.example.wapp.screen.SplashScreen
import com.example.wapp.screen.WisataScreen
import com.example.wapp.screen.admin.AdminDashboard
import com.example.wapp.screen.admin.AdminKulinerScreen
import com.example.wapp.screen.admin.AdminUsers
import com.example.wapp.screen.admin.AdminWisataScreen
import com.example.wapp.screen.auth.AuthViewModel
import com.example.wapp.screen.components.DetailHistory
import com.example.wapp.screen.components.InputType
import com.example.wapp.screen.maps.DestinationScreen
import com.example.wapp.screen.maps.MapNavigate
import com.example.wapp.screen.maps.MapsViewModel
import com.example.wapp.screen.maps.NavigasiSucess
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.android.gms.location.LocationServices
import com.mapbox.navigation.core.MapboxNavigation
import kotlinx.coroutines.launch


@ExperimentalSharedTransitionApi
@ExperimentalMaterial3Api
@ExperimentalLayoutApi
@ExperimentalPermissionsApi
@Composable
fun NavigationApp(mapboxNavigation: MapboxNavigation){
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = viewModel()
    val mapViewModel: MapsViewModel = viewModel()
    mapViewModel.setMapboxNavigation(mapboxNavigation)
    Navigation(navController = navController,  authViewModel =  authViewModel, mapViewModel=mapViewModel)
}


@ExperimentalSharedTransitionApi
@ExperimentalMaterial3Api
@ExperimentalLayoutApi
@ExperimentalPermissionsApi
@Composable
fun Navigation(navController: NavHostController, authViewModel: AuthViewModel, mapViewModel: MapsViewModel){
    var isLoggedIn by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        val user = authViewModel.currentUser
        isLoggedIn = user != null
    }
    val courutineScope = rememberCoroutineScope()
    SharedTransitionLayout {
        val firstRoute = if (isLoggedIn) BottomMenuScreen.HomePage.route else RouteApp.AuthScreen.routeid
        val userID = authViewModel.currentUser?.uid

       NavHost(navController = navController, startDestination= firstRoute){
           composable(RouteApp.SplashScreen.routeid){
               SplashScreen {
                   navController.navigate(RouteApp.IntroScreen.routeid){
                       popUpTo(RouteApp.SplashScreen.routeid){inclusive = true}
                   }
               }
           }
           composable(RouteApp.IntroScreen.routeid){
               IntroScreen(navController = navController)
           }
           composable(RouteApp.AuthScreen.routeid) {
               AuthScreen(navController,  animatedVisibilityScope = this@composable, sharedTransisionScope = this@SharedTransitionLayout)
           }
           composable(RouteApp.SigninScreen.routeid){
               SignInScreen(navController = navController, animatedVisibilityScope = this@composable, sharedTransisionScope = this@SharedTransitionLayout, authViewModel = authViewModel)
           }
           composable(RouteApp.SignUpScreen.routeid){
               SignUpScreen(navController = navController,
                   animatedVisibilityScope = this@composable,
                   sharedTransisionScope = this@SharedTransitionLayout,
                   authViewModel = authViewModel
               )
           }
           composable(RouteApp.ProfileScreen.routeid){
               ProfileScreen(navController, authViewModel=authViewModel)
           }
           composable(BottomMenuScreen.HomePage.route){
               HomeScreen(navController = navController, mapsViewModel = mapViewModel, authViewModel= authViewModel)
           }
           composable(RouteApp.FullMapScreen.routeid){
               FullMapScreenRoute(navController = navController, mapViewModel = mapViewModel, authViewModel=authViewModel)
           }
           composable(RouteApp.KulinerScreen.routeid){
               KulinerScreen(navController = navController, mapsViewModel = mapViewModel)
           }
           composable(RouteApp.WisataScreen.routeid){
               WisataScreen(navController = navController, mapsViewModel = mapViewModel)
           }
           composable(RouteApp.AdminHome.routeid) {
               AdminDashboard(navController = navController, mapsViewModel = mapViewModel)
           }
           composable(RouteApp.AdminWisata.routeid){
               AdminWisataScreen(mapsViewModel = mapViewModel, navController = navController)
           }
           composable(RouteApp.AdminKuliner.routeid){
               AdminKulinerScreen(mapsViewModel = mapViewModel, navController = navController)
           }
           composable(RouteApp.NavigasiSuccess.routeid){
               NavigasiSucess(navController = navController)
           }
           

           composable(
               "navigasi/{idDestinasi}/{latitude}/{longitude}",
               arguments = listOf(
                   navArgument("idDestinasi") { type = NavType.StringType },
                   navArgument("latitude") { type = NavType.StringType },
                   navArgument("longitude") { type = NavType.StringType }
               )
           ) { navBackStackEntry ->
               val id = navBackStackEntry.arguments?.getString("idDestinasi")
               val latitude = navBackStackEntry.arguments?.getString("latitude")?.toDoubleOrNull()
               val longitude = navBackStackEntry.arguments?.getString("longitude")?.toDoubleOrNull()

               userID?.let { uid ->
                   if (latitude != null && longitude != null && id != null) {
                       MapNavigate(mapsViewModel = mapViewModel, id = id, userId= uid ,navController = navController, latitude = latitude, longitude = longitude)

                   } else {
                       Log.e("Navigation", "Invalid latitude or longitude")
                   }
               }

           }

           composable(
               "Detail/{id}",
               arguments = listOf(
                   navArgument("id") { type = NavType.StringType },
               )
           ) { navBackStackEntry ->
               val id = navBackStackEntry.arguments?.getString("id")
               id?.let {
                   DestinationScreen(mapsViewModel= mapViewModel, navController = navController, id=it, userId=userID!!)
               }
           }
           composable(
               "DetailHistory/{destinationId}",
               arguments = listOf(
                   navArgument("destinationId") { type = NavType.StringType }
               )
           ) { navBackStackEntry ->
               val navId = navBackStackEntry.arguments?.getString("destinationId")
               val item = remember {
                   mutableStateOf<NavigationHistory?>(null)
               }


               if(navId != null){
                   LaunchedEffect (Unit){
                       item.value =  mapViewModel.getNavigationById(navId)

                   }
                   Log.d("Nav ID is", navId)


                   item.value?.let {
                       Log.d("Nav ID is", it.endTime!!)
                       DetailHistory(item = it, mapsViewModel= mapViewModel, navController)
                   }

               }

           }
           
           composable(RouteApp.UsersScreeen.routeid) {
               AdminUsers(authViewModel = authViewModel)
           }
       }
    }
}


@ExperimentalMaterial3Api
@Composable
fun FullMapScreenRoute(navController: NavController, mapViewModel: MapsViewModel, authViewModel: AuthViewModel) {
    val context = LocalContext.current
    val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

    val userLocation = remember { mutableStateOf<com.mapbox.geojson.Point?>(null) }

    val permissionGranted = remember { mutableStateOf(false) }

    val setting = remember {
        mutableStateOf(isLocationEnabled(context))
    }


    val markers = mapViewModel.markers.collectAsState()
    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            permissionGranted.value = isGranted
        }
    )

    LaunchedEffect(Unit) {
        // Memeriksa izin lokasi
        if (ContextCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED) {
            // Jika izin belum diberikan, meminta izin
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        } else {
            permissionGranted.value = true
        }
        mapViewModel.getAllMarkers()

    }

    LaunchedEffect(permissionGranted.value, setting.value) {
        if (permissionGranted.value && setting.value) {
            fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
                location?.let {
                    val point = com.mapbox.geojson.Point.fromLngLat(it.longitude, it.latitude)
                    userLocation.value = point
                } ?: run {
                    Log.d("Location", "Location is null")
                }
            }.addOnFailureListener {
                Log.e("Location", "Failed to get location")
            }
        }
    }

    if (userLocation.value == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                if (!setting.value) {
                    CircularProgressIndicator()
                    Text(
                        text = "Fitur Lokasi Anda Tidak Aktif",
                        style = TextStyle(
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Normal,
                            fontSize = 20.sp
                        )
                    )
                    Button(onClick = {
                        checkLocationSettings(context)
                    }) {
                        Text(
                            text = "Aktifkan",
                            style = TextStyle(
                                color = MaterialTheme.colorScheme.onPrimary,
                                fontWeight = FontWeight.Normal,
                                fontSize = 20.sp
                            )
                        )
                    }
                }
            }
        }
    } else {
        val marker = markers.value ?: listOf()
        FullMapsScreen(
            navController = navController,
            mapsViewModel = mapViewModel,
            authViewModel = authViewModel,
            userLocation = userLocation.value!!,
            markers = marker
        )
    }
}

fun isLocationEnabled(context: Context): Boolean {
    val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
            locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
}
fun checkLocationSettings(context: Context) {
    val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        context.startActivity(intent)
    }
}



