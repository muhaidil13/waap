package com.example.wapp.screen.components

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.wapp.data.Photo
import com.example.wapp.screen.maps.MapsViewModel

@Composable
fun PhotoComponents(images:List<Photo>?){
    if (images == null){
        Text("Tidak Ada Gambar")
    }
    images?.let {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.padding(horizontal = 10.dp)
        ){
            items(it){ items ->
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
                    .clip(RoundedCornerShape(20.dp))){

                    Image(
                        painter = painter,
                        contentDescription = "it.id",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }

    }

}