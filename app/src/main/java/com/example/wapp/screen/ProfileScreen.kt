package com.example.wapp.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ChangeCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter

import com.example.wapp.data.User
import com.example.wapp.screen.auth.AuthViewModel


@Composable
fun ProfileScreen(navController: NavController, authViewModel: AuthViewModel){
    val sizeImage = 1980 * 2

    val imageLoader = ImageLoader(LocalContext.current)
    val painter = rememberAsyncImagePainter(model = "https://picsum.photos/${sizeImage}", imageLoader = imageLoader)
    val scrollState = rememberScrollState()
    var user by remember{
        mutableStateOf(User(id = "",
            name = "",
            phone = "",
            email = "",
            lastLogin = "",
            role = "",
            createdAt = "",
            updatedAt = ""
        ))
    }
    var error by remember {
        mutableStateOf("")
    }

    LaunchedEffect(Unit){
        authViewModel.fetchUser(
            onError = {
                error = it
            },
            onSuccess = {
                user = it
            }
        )
    }

    Scaffold { paddingValues ->
       Box(modifier = Modifier
           .fillMaxSize()
           .padding(paddingValues)
       ){
           Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter){
               Row (modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(20.dp), verticalAlignment = Alignment.CenterVertically){
                   IconButton(onClick = { navController.popBackStack() }) {
                       Icon(imageVector = Icons.Filled.ArrowBackIosNew, tint = MaterialTheme.colorScheme.primary, contentDescription = "")
                   }
                   Text(text = "Profile", style = TextStyle(
                       fontSize = 25.sp,
                       fontWeight = FontWeight.SemiBold,
                       color = MaterialTheme.colorScheme.primary)
                   )

               }
           }
           Box(modifier = Modifier.fillMaxSize().padding(horizontal = 25.dp)){
               Column(
                   modifier = Modifier
                       .padding(top = 80.dp)
                       .verticalScroll(scrollState),
                   verticalArrangement = Arrangement.spacedBy(30.dp)
               ){
                   Box(modifier = Modifier
                       .fillMaxWidth()
                       .height(220.dp)
                       .clip(RoundedCornerShape(10.dp))
                       .background(
                           Color.LightGray
                       )){
                       Image(painter = painter, contentDescription = "", modifier = Modifier.fillMaxWidth(),contentScale = ContentScale.Crop)
                   }
                   Row(
                       verticalAlignment = Alignment.CenterVertically,
                       horizontalArrangement = Arrangement.SpaceBetween,
                       modifier = Modifier.fillMaxWidth()
                   ){
                       Column {
                           Text(text = "Nama Pengguna", style = TextStyle(
                               fontSize = 18.sp,
                               fontWeight = FontWeight.Light,
                               color = MaterialTheme.colorScheme.primary)
                           )
                           Spacer(modifier = Modifier.height(2.dp))
                           Text(text = user.name, style = TextStyle(
                               fontSize = 20.sp,
                               fontWeight = FontWeight.Normal,
                               color = MaterialTheme.colorScheme.primary)
                           )
                       }
                       IconButton(onClick = { /*TODO*/ }) {
                           Icon(imageVector = Icons.Filled.ChangeCircle, contentDescription = "")
                       }

                   }
                   Row(
                       verticalAlignment = Alignment.CenterVertically,
                       horizontalArrangement = Arrangement.SpaceBetween,
                       modifier = Modifier.fillMaxWidth()
                   ){
                       Column {
                           Text(text = "Email Pengguna", style = TextStyle(
                               fontSize = 18.sp,
                               fontWeight = FontWeight.Light,
                               color = MaterialTheme.colorScheme.primary)
                           )
                           Spacer(modifier = Modifier.height(2.dp))
                           Text(text = user.email, style = TextStyle(
                               fontSize = 20.sp,
                               fontWeight = FontWeight.Normal,
                               color = MaterialTheme.colorScheme.primary)
                           )
                       }
                       IconButton(onClick = { /*TODO*/ }) {
                           Icon(imageVector = Icons.Filled.ChangeCircle, contentDescription = "")
                       }
                   }
                   Row(
                       verticalAlignment = Alignment.CenterVertically,
                       horizontalArrangement = Arrangement.SpaceBetween,
                       modifier = Modifier.fillMaxWidth()
                   ){
                       Column {
                           Text(text = "Phone Number", style = TextStyle(
                               fontSize = 18.sp,
                               fontWeight = FontWeight.Light,
                               color = MaterialTheme.colorScheme.primary)
                           )
                           Spacer(modifier = Modifier.height(2.dp))
                           Text(text = user.phone, style = TextStyle(
                               fontSize = 20.sp,
                               fontWeight = FontWeight.Normal,
                               color = MaterialTheme.colorScheme.primary)
                           )
                       }
                       IconButton(onClick = { /*TODO*/ }) {
                           Icon(imageVector = Icons.Filled.ChangeCircle, contentDescription = "")
                       }

                   }
                   Row(
                       verticalAlignment = Alignment.CenterVertically,
                       horizontalArrangement = Arrangement.SpaceBetween,
                       modifier = Modifier.fillMaxWidth()
                   ){

                       Column {
                           Text(text = "Bergabung Pada", style = TextStyle(
                               fontSize = 18.sp,
                               fontWeight = FontWeight.Light,
                               color = MaterialTheme.colorScheme.primary)
                           )
                           Spacer(modifier = Modifier.height(2.dp))
                           Text(text = user.createdAt, style = TextStyle(
                               fontSize = 20.sp,
                               fontWeight = FontWeight.Normal,
                               color = MaterialTheme.colorScheme.primary)
                           )
                       }
                       IconButton(onClick = { /*TODO*/ }) {
                           Icon(imageVector = Icons.Filled.ChangeCircle, contentDescription = "")
                       }
                   }
                   Row(
                       verticalAlignment = Alignment.CenterVertically,
                       horizontalArrangement = Arrangement.SpaceBetween,
                       modifier = Modifier.fillMaxWidth()
                   ){
                       Column {
                           Text(text = "Login Terakhir", style = TextStyle(
                               fontSize = 18.sp,
                               fontWeight = FontWeight.Light,
                               color = MaterialTheme.colorScheme.primary)
                           )
                           Spacer(modifier = Modifier.height(2.dp))
                           Text(text = user.lastLogin, style = TextStyle(
                               fontSize = 20.sp,
                               fontWeight = FontWeight.Normal,
                               color = MaterialTheme.colorScheme.primary)
                           )
                       }
                       IconButton(onClick = { /*TODO*/ }) {
                           Icon(imageVector = Icons.Filled.ChangeCircle, contentDescription = "")
                       }

                   }

               }
           }
       }
   }
}