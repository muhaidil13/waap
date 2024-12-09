package com.example.wapp

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.firebase.FirebaseApp
import com.mapbox.navigation.base.options.NavigationOptions
import com.mapbox.navigation.core.MapboxNavigation
import com.mapbox.navigation.core.lifecycle.MapboxNavigationApp
import com.mapbox.navigation.core.lifecycle.requireMapboxNavigation


@ExperimentalSharedTransitionApi
@ExperimentalLayoutApi
@ExperimentalPermissionsApi
@ExperimentalMaterial3Api
class MainActivity : ComponentActivity() {
    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
        object : ActivityResultCallback<Boolean> {
            override fun onActivityResult(result: Boolean) {
                if (result) {
                    Toast.makeText(applicationContext, "Isin Berhasil Diberikan", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(applicationContext, "Aplikasi Ini Memelukan Isin  ", Toast.LENGTH_SHORT).show()

                }
            }
        }
    )

    private fun requestPermission(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            if(ActivityCompat.checkSelfPermission(applicationContext, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED){
                permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }

        if(ActivityCompat.checkSelfPermission(applicationContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(applicationContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            permissionLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
        }

    }



    private val mapboxNavigation: MapboxNavigation by requireMapboxNavigation(
        onInitialize = this::initNavigation
    )
    private fun initNavigation(){
        MapboxNavigationApp.setup(
            NavigationOptions.
            Builder(this).build()
        )
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestPermission()
        FirebaseApp.initializeApp(this)

        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                NavigationApp(mapboxNavigation)
            }
        }
    }
}


