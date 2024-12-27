package com.example.wapp.screen.components

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.wapp.data.Photo
import com.example.wapp.screen.maps.MapsViewModel


@Composable
fun PhotoComponents( navController: NavController, mapsViewModel: MapsViewModel){
    //mapsViewModel.getAllImage()

    val images by mapsViewModel.images.collectAsState()

    LaunchedEffect(Unit){
        mapsViewModel.getAllImage()
    }
    if (images == null){
        Text("Tidak Ada Gambar")
    }


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
                        imageVector = Icons.Filled.PhotoLibrary,
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                Text(text = "Galery", style = TextStyle(
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Light,
                    fontSize = 20.sp)
                )
            }
        }
        LazyColumn(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp, top = 120.dp, bottom = maxh * .1f),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ){
            images?.let {  nav ->
               items(nav){ items ->
                    val painter = rememberAsyncImagePainter(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(items.imageUrl)
                            .crossfade(true)
                            .listener(
                                onSuccess = { _, _ ->
                                    Log.d("ImageDebug", "Image loaded successfully")
                                },
                                onError = { _, throwable ->
                                    Log.e("ImageDebug", "Error loading image: ${throwable}")
                                }
                            )
                            .build())
                    Box(modifier = Modifier
                        .height(200.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))){

                        Image(
                            painter = painter,
                            contentDescription = "it.id",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                        Box(modifier = Modifier
                            .fillMaxSize()
                            .clickable {
                                val id = items.markerId
                                navController.navigate("Detail/${id}")
                            }
                        ){

                            Row(modifier = Modifier
                                .fillMaxSize()
                                .padding(end = 10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ){

                                Column(modifier = Modifier
                                    .fillMaxSize()
                                    .padding(start = 10.dp, top =10.dp)
                                    .weight(1f), horizontalAlignment = Alignment.Start, verticalArrangement = Arrangement.spacedBy(10.dp)){
                                    Text(
                                        text = "Diupload Oleh: ", style = TextStyle(
                                            color = MaterialTheme.colorScheme.onPrimary,
                                            fontWeight = FontWeight.Normal,
                                            fontSize = 14.sp
                                        )
                                    )
                                    Text(
                                        text = items.uploadedBy , style = TextStyle(
                                            color = MaterialTheme.colorScheme.onPrimary,
                                            fontWeight = FontWeight.SemiBold,
                                            fontSize = 19.sp
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

}