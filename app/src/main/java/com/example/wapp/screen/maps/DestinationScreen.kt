package com.example.wapp.screen.maps

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import android.view.inputmethod.InputMethodManager
import androidx.annotation.ArrayRes
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState

import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RenderEffect
import androidx.compose.ui.graphics.Shader
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import coil.ImageLoader
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.wapp.MainViewModel
import com.example.wapp.components.InputSugestions
import com.example.wapp.components.ReviewMarkerDialog
import com.example.wapp.components.StarRating
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationServices.*
import com.mapbox.geojson.Point
import kotlinx.coroutines.launch


@Composable
@ExperimentalLayoutApi
fun DestinationScreen(
    mapsViewModel: MapsViewModel,
    navController: NavController,
    id: String,
    userId: String
) {

    val context = LocalContext.current
    val fusedLocationProviderClient = getFusedLocationProviderClient(context)
    val locationUser = remember {
        mutableStateOf<Point?>(null)
    }


    LaunchedEffect(Unit) {
        mapsViewModel.getMarkersById(id = id)
        mapsViewModel.getListOfReviewByIdMarker(id)
        mapsViewModel.getPhoto(id)
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
                locationUser.value = Point.fromLngLat(location.longitude, location.latitude)

                location?.let {
                    locationUser.value = Point.fromLngLat(location.longitude, location.latitude)
                }
            }.addOnFailureListener {
                locationUser.value = null
            }
        }
    }
    locationUser.value?.let {
        DestinasionContent(mapsViewModel, navController, id, userId, it)

    }
}

@ExperimentalLayoutApi
@Composable
fun  DestinasionContent(mapsViewModel: MapsViewModel, navController: NavController, id: String, userId: String, point: Point){
    val mark by mapsViewModel.marker.collectAsState()
    val images by mapsViewModel.photos.collectAsState()
    val reviews = mapsViewModel.reviews.collectAsState()
    val scrollState = rememberScrollState()
    val listState = rememberLazyListState()
    val courutineScope = rememberCoroutineScope()
    val reviewDialog = remember {
        mutableStateOf(false)
    }
    val currentIndex = remember {
        mutableIntStateOf(0)
    }
    val context = LocalContext.current

    LaunchedEffect (listState){
        snapshotFlow { listState.firstVisibleItemIndex }
            .collect{
                currentIndex.intValue = it
            }
    }


    Scaffold { innerPadding ->
        BoxWithConstraints(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            val maxw = maxWidth
            Box(modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 20.dp, horizontal = 20.dp), contentAlignment = Alignment.BottomCenter){
                Column(modifier = Modifier
                    .fillMaxWidth()){
                    Button( modifier = Modifier.fillMaxWidth(), onClick = {
                        navController.navigate("navigasi/${id}/${point.latitude()}/${point.longitude()}",)
                    }) {
                        Text(text = "Start Navigate")
                    }
                }
            }

            Column (modifier = Modifier.padding(bottom = 100.dp)){
                if (mark == null) {
                    Text(
                        text = "Lokasi Tidak Tersedia",
                        style = TextStyle(
                            color = Color.Black,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                } else {
                    mark?.let { marker ->
                        Column(modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(scrollState)
                            .padding(vertical = 20.dp), verticalArrangement = Arrangement.spacedBy(30.dp)){
                            Column(verticalArrangement = Arrangement.spacedBy(10.dp),){
                                Column(modifier = Modifier.padding(horizontal = 30.dp), verticalArrangement = Arrangement.spacedBy(10.dp)){
                                    Row{
                                        IconButton(onClick = { navController.popBackStack() }) {
                                            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(40.dp) , contentDescription = "")
                                        }
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Column(horizontalAlignment = Alignment.Start){
                                            Text(
                                                text = "Informasi Tujuan",
                                                style = TextStyle(
                                                    color = MaterialTheme.colorScheme.primary,
                                                    fontSize = 22.sp,
                                                    fontWeight = FontWeight.SemiBold
                                                )
                                            )
                                            Text(
                                                text = "Detail Dari Tujuan Ada",
                                                style = TextStyle(
                                                    color = Color.Black,
                                                    fontSize = 12.sp,
                                                    fontWeight = FontWeight.Normal
                                                )
                                            )
                                        }

                                    }
                                    Spacer(modifier = Modifier.height(5.dp))
                                    Column(horizontalAlignment = Alignment.Start, ){
                                        Text(
                                            text = "Nama Tempat :",
                                            style = TextStyle(
                                                color = Color.Black,
                                                fontSize = 18.sp,
                                                fontWeight = FontWeight.SemiBold
                                            )
                                        )
                                        Text(
                                            text = marker.locationName,
                                            style = TextStyle(
                                                color = Color.Black,
                                                fontSize = 18.sp,
                                                fontWeight = FontWeight.Normal
                                            )
                                        )

                                    }
                                    Column(horizontalAlignment = Alignment.Start, ){
                                        Text(
                                            text = "Nama Jalan :",
                                            style = TextStyle(
                                                color = Color.Black,
                                                fontSize = 18.sp,
                                                fontWeight = FontWeight.SemiBold
                                            )
                                        )
                                        Text(
                                            text = marker.streetName,
                                            style = TextStyle(
                                                color = Color.Black,
                                                fontSize = 18.sp,
                                                fontWeight = FontWeight.Normal
                                            )
                                        )

                                    }

                                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically){
                                        Text(
                                            text = "Rating :",
                                            style = TextStyle(
                                                color = Color.Black,
                                                fontSize = 18.sp,
                                                fontWeight = FontWeight.SemiBold
                                            )
                                        )
                                        Text(
                                            text = "4.5",
                                            style = TextStyle(
                                                color = Color.Black,
                                                fontSize =18.sp,
                                                fontWeight = FontWeight.Normal
                                            )
                                        )

                                    }
                                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically){
                                        Text(
                                            text = "Dibuat Tanggal :",
                                            style = TextStyle(
                                                color = Color.Black,
                                                fontSize = 18.sp,
                                                fontWeight = FontWeight.SemiBold
                                            )
                                        )
                                        Text(
                                            text = marker.createdAt,
                                            style = TextStyle(
                                                color = Color.Black,
                                                fontSize =18.sp,
                                                fontWeight = FontWeight.Normal
                                            )
                                        )
                                    }
                                    Column {
                                        Text(
                                            text ="Deskripsi",
                                            style = TextStyle(
                                                color = Color.Black,
                                                fontSize = 18.sp,
                                                fontWeight = FontWeight.SemiBold
                                            )
                                        )
                                        Text(
                                            text =marker.description,
                                            style = TextStyle(
                                                color = Color.Black,
                                                fontSize = 18.sp,
                                                fontWeight = FontWeight.Normal
                                            )
                                        )
                                    }
                                    Text(
                                        text ="Foto",
                                        style = TextStyle(
                                            color = Color.Black,
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    )
                                }
                                images?.let{ imgs->
                                    LazyRow (
                                        state = listState,
                                        horizontalArrangement = Arrangement.spacedBy(20.dp),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 15.dp)
                                            .height(230.dp),
                                        contentPadding = PaddingValues(top = 20.dp, start = 20.dp, bottom = 10.dp, end = 20.dp)
                                    ){
                                       itemsIndexed( imgs, key = {_ ,item -> item.hashCode()}){ index,items ->
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
                                           val isCurrent = index == currentIndex.intValue
                                           CardImage(painter = painter, parrentWidth = maxw, iscurrent = isCurrent){
                                               currentIndex.intValue = index
                                               courutineScope.launch {
                                                   listState.animateScrollToItem(index)
                                               }
                                           }
                                        }
                                        item { 
                                            Spacer(modifier = Modifier.width(16.dp))
                                        }
                                    }
                                }


                                Column(modifier = Modifier.padding(horizontal = 30.dp), verticalArrangement = Arrangement.spacedBy(10.dp)){
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ){
                                        Text(
                                            text ="Reviews",
                                            style = TextStyle(
                                                color = Color.Black,
                                                fontSize = 18.sp,
                                                fontWeight = FontWeight.SemiBold
                                            )
                                        )
                                        TextButton(onClick = {
                                            reviewDialog.value = true
                                        }, border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)) {
                                            Text(
                                                text ="Buat Review",
                                                style = TextStyle(
                                                    color = MaterialTheme.colorScheme.primary,
                                                    fontSize = 18.sp,
                                                    fontWeight = FontWeight.Normal
                                                )
                                            )
                                        }

                                    }
                                }
                                reviews.value?.let {  review ->

                                    LazyColumn(
                                        verticalArrangement = Arrangement.spacedBy(20.dp),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 15.dp)
                                            .height(230.dp),
                                        contentPadding = PaddingValues(top = 20.dp, start = 20.dp, bottom = 10.dp, end = 20.dp)
                                    ){
                                        if (review.isEmpty()){
                                            item{
                                                Text(
                                                    text = "Tidak Ada Review ",
                                                    style = TextStyle(
                                                        color = MaterialTheme.colorScheme.primary,
                                                        fontSize = 12.sp,
                                                        fontWeight = FontWeight.Normal
                                                    )
                                                )
                                            }
                                        }
                                        items(review){
                                            Column(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(vertical = 10.dp),
                                                verticalArrangement = Arrangement.spacedBy(15.dp)
                                            ){
                                                Column {
                                                    Row(
                                                        horizontalArrangement = Arrangement.spacedBy(15.dp),
                                                    ){
                                                        Icon(imageVector = Icons.Filled.Person, modifier =  Modifier.size(30.dp), tint = MaterialTheme.colorScheme.primary, contentDescription = "User")
                                                        StarRating(rating = it.rating.toFloat())
                                                    }
                                                    Text(
                                                        text = it.createdAt!!,
                                                        style = TextStyle(
                                                            color = MaterialTheme.colorScheme.primary,
                                                            fontSize = 12.sp,
                                                            fontWeight = FontWeight.Normal
                                                        )
                                                    )
                                                }
                                                Text(
                                                    text = it.review,
                                                    style = TextStyle(
                                                        color = MaterialTheme.colorScheme.primary,
                                                        fontSize = 14.sp,
                                                        fontWeight = FontWeight.Normal
                                                    )
                                                )

                                            }
                                        }
                                    }
                                }
                            }
                        }
                        if(reviewDialog.value){
                            ReviewMarkerDialog(
                                userId = userId,
                                markerId = id,
                                onDismis = {
                                    reviewDialog.value = false
                                },
                                onSubmit = { review ->
                                    mapsViewModel.sendReview(review, context = context)
                                    reviewDialog.value = false
                                    mapsViewModel.getListOfReviewByIdMarker(id)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun CardImage(painter: AsyncImagePainter, parrentWidth: Dp, iscurrent:Boolean, onClick:() -> Unit){
    val animatedScale by animateFloatAsState(targetValue = if (iscurrent) 1.1f else 1f)
    val blurEffect by animateFloatAsState(targetValue = if (iscurrent) 1f else 0.5f)
    val animatedOffsetX by animateDpAsState(targetValue = if (iscurrent) 0.dp else 10.dp)
    val animatedOffsetY by animateDpAsState(targetValue = if (iscurrent) 0.dp else 5.dp)

    Box(
        modifier = Modifier
            .clickable { onClick() }
            .graphicsLayer {
                scaleX = animatedScale
                scaleY = animatedScale
            }
            .offset(x = animatedOffsetX, y = animatedOffsetY)
            .fillMaxHeight()
            .width(parrentWidth * .8f)
            .clip(RoundedCornerShape(20.dp))
    ) {
        Image(
            painter = painter,
            contentDescription = "Image",
            modifier = Modifier
                .fillMaxSize()
                .alpha(blurEffect)
                .graphicsLayer {
                    alpha = if (iscurrent) 1f else 0.5f
                },
            contentScale = ContentScale.Crop
        )
    }
}
