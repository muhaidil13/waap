package com.example.wapp

import android.util.Log
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
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
import com.example.wapp.screen.maps.DestinationScreen
import com.example.wapp.screen.maps.MapNavigate
import com.example.wapp.screen.maps.MapsViewModel
import com.example.wapp.screen.maps.NavigasiSucess
import com.google.accompanist.permissions.ExperimentalPermissionsApi
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
               FullMapsScreen(navController = navController, mapsViewModel=mapViewModel, authViewModel = authViewModel)
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

