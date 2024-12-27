package com.example.wapp.screen

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.wapp.R
import com.example.wapp.components.IntroComponent


data class IntroScreenState(
    val idRes: Int = 0,
    val title: String = "",
    val subTitle: String = ""
)

@Composable
fun IntroScreen(navController: NavController){
    var indexPage by remember{
        mutableStateOf(0)
    }

    var  introScreenState by remember {
        mutableStateOf(IntroScreenState())
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.onPrimary
    ){ innerPadding ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 35.dp, vertical = 10.dp)
            .padding(innerPadding)){
            Column {
                AnimatedContent(targetState = indexPage, label = "animatePageSection",) { target ->
                    when(target){
                        0 -> {
                            introScreenState = introScreenState.copy(
                                idRes = R.drawable.asset7,
                                title = "Mencari Lokasi Wisata",
                                subTitle = "Memudahkan Pencarian Lokasi Wisata Dengan Mudah Dan Akurat"
                            )
                        }
                        1 -> {
                            introScreenState = introScreenState.copy(
                                idRes = R.drawable.asset7,
                                title = "Mencari Lokasi Kuliner",
                                subTitle = "Memudahkan Pencarian Lokasi Kuliner Dengan Mudah Dan Akurat."
                            )
                        }
                        2 -> {
                            introScreenState = introScreenState.copy(
                                idRes = R.drawable.asset6,
                                title = "Sistem Dengan Rekomendasi",
                                subTitle = "Memudahkan Pencarian Dengan Sistem Rekomendasi"
                            )
                        }
                        3 -> {
                            introScreenState = introScreenState.copy(
                                idRes = R.drawable.asset8,
                                title = "Lokasi Disekitar Anda",
                                subTitle = "Lacak Wisata Dan Kuliner Sekitar Anda"
                            )
                        }

                        else -> {

                        }
                    }
                    IntroComponent(introScreenState)
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
            Box(modifier = Modifier
                .padding(bottom = 30.dp)
                .align(Alignment.BottomCenter)
            ){
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally){

                    if(indexPage!= 3){
                        Button(onClick = {
                            indexPage ++
                        }, colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)) {

                            Text(text = if (indexPage == 0) "Mulai" else "Lanjutkan", modifier = Modifier
                                .fillMaxWidth()
                                .padding(7.dp), style = TextStyle(
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.Normal,
                                fontSize = 16.sp
                            )
                            )
                        }
                        if(indexPage != 0){
                            TextButton(onClick = { indexPage-- }) {
                                Text(text = "Kembali", color = MaterialTheme.colorScheme.primary)
                            }
                        }
                    }else{
                        Button(onClick = {
                            navController.navigate("authScreen")
                        }, colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)) {
                            Text(text = "Lets Get Start", modifier = Modifier
                                .fillMaxWidth()
                                .padding(7.dp), style = TextStyle(
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.Normal,
                                color = MaterialTheme.colorScheme.onPrimary,
                                fontSize = 16.sp
                            )
                            )
                        }
                        TextButton(onClick = { indexPage-- }) {
                            Text(text = "Kembali", color = MaterialTheme.colorScheme.primary)
                        }
                    }
                }
            }
        }
    }
}