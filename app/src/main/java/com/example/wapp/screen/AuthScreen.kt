package com.example.wapp.screen

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.wapp.R


@ExperimentalSharedTransitionApi
@Composable
fun AuthScreen(navController: NavController, sharedTransisionScope: SharedTransitionScope, animatedVisibilityScope: AnimatedVisibilityScope){
    with(sharedTransisionScope){
        Box(modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.onPrimary)
            .padding(bottom = 100.dp, top = 70.dp, start = 50.dp, end = 50.dp),
            contentAlignment = Alignment.TopCenter
        ){
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center){
                Spacer(modifier = Modifier.height(40.dp))
                Text(text = "Selamat Datang", style = TextStyle(
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Normal,
                    fontSize = 30.sp
                )
                )
                Spacer(modifier = Modifier.height(30.dp))
                Button(onClick = { navController.navigate("SigninScreen") }, modifier = Modifier.fillMaxWidth(),  colors = ButtonDefaults.buttonColors(
                    Color.Transparent), border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
                ) {
                    Text(text = "Sign in", fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.primary,modifier = Modifier
                        .padding(vertical = 7.dp)
                        .sharedElement(
                            sharedTransisionScope.rememberSharedContentState(key = "Text-SignIn"),
                            animatedVisibilityScope = animatedVisibilityScope
                        ))
                }
                Spacer(modifier = Modifier.height(10.dp))
                Button(onClick = {navController.navigate("SignupScreen")},modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(
                    MaterialTheme.colorScheme.primary)) {
                    Text(text = "Sign Up", fontWeight = FontWeight.Normal,
                        modifier = Modifier.padding(vertical = 7.dp)
                            .sharedElement(
                                sharedTransisionScope.rememberSharedContentState(key = "Text-SignUp"),
                                animatedVisibilityScope = animatedVisibilityScope
                            ),
                        color = MaterialTheme.colorScheme.onPrimary)
                }
            }
            Box (
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.BottomCenter
            ){
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ){
                    Text(text = "Login menggunakan Social Media", style = TextStyle(
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Light
                    ), modifier = Modifier.padding(bottom = 10.dp))
                    Row(verticalAlignment = Alignment.CenterVertically,  horizontalArrangement = Arrangement.spacedBy(10.dp)){
                        Button(onClick = { /*TODO*/ } , colors = ButtonDefaults.buttonColors( containerColor = MaterialTheme.colorScheme.onPrimary)) {
                            Row(verticalAlignment = Alignment.CenterVertically){
                                Icon(painter = painterResource(id = R.drawable.asset2), tint = Color.Blue, modifier = Modifier
                                    .size(30.dp), contentDescription ="FB" )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(text = "Facebook",  color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Light)
                            }
                        }
                        Button(onClick = { /*TODO*/ } , colors = ButtonDefaults.buttonColors(
                            MaterialTheme.colorScheme.onPrimary)) {
                            Row(verticalAlignment = Alignment.CenterVertically){
                                Icon(painter = painterResource(id = R.drawable.asset4), tint = Color.Red, modifier = Modifier
                                    .size(30.dp), contentDescription ="FB" )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(text = "Google",  color = Color.Red, fontWeight = FontWeight.Light)
                            }
                        }
                    }
                }
            }
        }
    }
}