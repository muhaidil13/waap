package com.example.wapp.screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.wapp.data.Markers
import com.example.wapp.screen.auth.AuthViewModel
import com.example.wapp.screen.maps.MapsViewModel

@Composable
fun HistoryComponents(
    mapsViewModel: MapsViewModel,
    authViewModel: AuthViewModel,
    navController: NavController
){
    val navhist = mapsViewModel.listnavigationHistory.collectAsState()
    val marker = mapsViewModel.marker.collectAsState()
    LaunchedEffect(Unit){
        authViewModel.currentUser?.let {
            mapsViewModel.getAllNavigationHistoryUser(it.uid)
        }
    }
    val scrollState = rememberScrollState()
    val scrollStateChild = rememberScrollState()
    BoxWithConstraints(modifier = Modifier.fillMaxSize()){
        val maxh = maxHeight
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(top = 15.dp), contentAlignment = Alignment.TopCenter){
            Row(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.onPrimary)
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
                    .padding(top = 40.dp, bottom = 30.dp)
                ,
                verticalAlignment = Alignment.CenterVertically,
            ){
                IconButton(onClick = {

                }) {
                    Icon(
                        imageVector = Icons.Filled.Favorite,
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                Text(text = "History", style = TextStyle(
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Light,
                    fontSize = 20.sp
                )
                )
            }
        }
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp, top = 120.dp, bottom = 20.dp)

            .verticalScroll(scrollState)
        ){


            navhist.value?.let {  nav ->
               nav.forEach {
                   val isExpand = remember{
                       mutableStateOf(false)
                   }
                   val cardHeight = if(isExpand.value) maxh * .3f else maxh * .06f
                   Box(modifier = Modifier
                       .height(cardHeight)
                       .background(MaterialTheme.colorScheme.onPrimary)
                       .fillMaxWidth()
                       .clickable {
                           isExpand.value = !isExpand.value
                           mapsViewModel.getMarkersById(it.destinationId)
                       }
                   ){
                       Column(modifier = Modifier
                           .fillMaxSize()
                           ,
                           verticalArrangement = Arrangement.spacedBy(15.dp),){
                           Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically){
                               Text(
                                   text = it.startTime, style = TextStyle(
                                       color = MaterialTheme.colorScheme.primary,
                                       fontWeight = FontWeight.Normal,
                                       fontSize = 15.sp
                                   )
                               )
                               Icon(imageVector = Icons.Filled.KeyboardArrowDown, tint = MaterialTheme.colorScheme.primary, contentDescription = "")
                           }
                           marker.value?.let {  mark ->
                               if(isExpand.value){
                                   Column(
                                       modifier = Modifier
                                           .fillMaxSize()
                                           .padding(horizontal = 20.dp)
                                           .verticalScroll(scrollStateChild),
                                       verticalArrangement = Arrangement.spacedBy(3.dp),
                                   ){
                                      Row {
                                          Text(text = "Tujuan Anda ", style = TextStyle(
                                              fontSize = 15.sp,
                                              fontWeight = FontWeight.SemiBold,
                                              color = MaterialTheme.colorScheme.primary)
                                          )
                                          Text(text = mark.locationName, style = TextStyle(
                                              fontSize = 15.sp,
                                              fontWeight = FontWeight.SemiBold,
                                              color = MaterialTheme.colorScheme.primary)
                                          )
                                      }
                                       Spacer(modifier = Modifier.height(10.dp))
                                       Column (
                                           modifier = Modifier
                                               .fillMaxWidth()
                                               .padding(5.dp)
                                           ,
                                           verticalArrangement = Arrangement.spacedBy(3.dp)
                                       ){
                                           Row {
                                               Text(text = "Jarak Tempuh", style = TextStyle(
                                                   fontSize = 12.sp,
                                                   fontWeight = FontWeight.Normal,
                                                   color = MaterialTheme.colorScheme.primary)
                                               )
                                               Text(text = it.totalJarak, style = TextStyle(
                                                   fontSize = 12.sp,
                                                   fontWeight = FontWeight.Normal,
                                                   color = MaterialTheme.colorScheme.primary)
                                               )
                                           }
                                           Row {
                                               Text(text = "Waktu Tempuh", style = TextStyle(
                                                   fontSize = 12.sp,
                                                   fontWeight = FontWeight.Normal,
                                                   color = MaterialTheme.colorScheme.primary)
                                               )
                                               Text(text = it.totalWaktu, style = TextStyle(
                                                   fontSize = 12.sp,
                                                   fontWeight = FontWeight.Normal,
                                                   color = MaterialTheme.colorScheme.primary)
                                               )
                                           }
                                           Row {
                                               Text(text = "Di Mulai Pada", style = TextStyle(
                                                   fontSize = 12.sp,
                                                   fontWeight = FontWeight.Normal,
                                                   color = MaterialTheme.colorScheme.primary)
                                               )
                                               Text(text = it.startTime, style = TextStyle(
                                                   fontSize = 12.sp,
                                                   fontWeight = FontWeight.Normal,
                                                   color = MaterialTheme.colorScheme.primary)
                                               )
                                           }
                                           Row {
                                               Text(text = "Berakhir Pada", style = TextStyle(
                                                   fontSize = 12.sp,
                                                   fontWeight = FontWeight.Normal,
                                                   color = MaterialTheme.colorScheme.primary)
                                               )
                                               Text(text = it.endTime!!, style = TextStyle(
                                                   fontSize = 12.sp,
                                                   fontWeight = FontWeight.Normal,
                                                   color = MaterialTheme.colorScheme.primary)
                                               )
                                           }

                                       }
                                   }
                               }
                           }

                       }

                   }
               }
            }

        }
    }
}