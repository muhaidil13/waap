package com.example.wapp.screen.auth

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.wapp.R
import com.example.wapp.components.InputFieldCustom
import com.example.wapp.screen.components.BottomMenuScreen
import com.example.wapp.screen.components.InputType


@ExperimentalSharedTransitionApi
@ExperimentalMaterial3Api
@Composable
fun SignInScreen(navController: NavController, sharedTransisionScope: SharedTransitionScope, animatedVisibilityScope: AnimatedVisibilityScope, authViewModel: AuthViewModel){
    val email = rememberSaveable {
        mutableStateOf("")
    }
    val passwrod = rememberSaveable {
        mutableStateOf("")
    }
    val errorState =  remember{
        mutableStateOf("")
    }
    val emailError = remember{
        mutableStateOf(false)
    }
    val emailPattern = "[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}"

    fun isValidEmail(input: String): Boolean {
        return input.matches(emailPattern.toRegex())
    }
    val context = LocalContext.current

    val isButtonEnabled = remember {
        mutableStateOf(false)
    }

    LaunchedEffect(email.value, passwrod.value, errorState.value){
        isButtonEnabled.value = email.value.isNotEmpty() &&  passwrod.value.isNotEmpty()
    }


    with(sharedTransisionScope){
        Scaffold(topBar = {
            LargeTopAppBar(title = {
                Text(text = "Hello Selamat Datang",
                    color = MaterialTheme.colorScheme.onPrimary)
            },
                colors = TopAppBarDefaults.topAppBarColors(
                    Color.Transparent),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack()}) {
                        Icon(imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowLeft, tint = MaterialTheme.colorScheme.onPrimary, contentDescription ="" )
                    }
                },
                actions = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(imageVector = Icons.Filled.Info, contentDescription ="",tint = MaterialTheme.colorScheme.onPrimary )
                    }
                })
        },
        ){ innerPadding ->
            BoxWithConstraints(modifier = Modifier
                .fillMaxSize()
                .background(Brush.horizontalGradient(listOf(Color(0xFF6C63FF), Color(0xFF1A436B))))
                .padding(top = innerPadding.calculateTopPadding())){
                val maxheigt = maxHeight
                Box(modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
                    .background(Color.White)
                    .padding(bottom = innerPadding.calculateBottomPadding())
                    .padding(top = 30.dp, end = 30.dp, start = 30.dp))
                {
                    Box(modifier = Modifier.fillMaxSize()){
                        Column(modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom = 30.dp), verticalArrangement = Arrangement.Center){
                            Column {
                                Text(text = "Email", color = MaterialTheme.colorScheme.primary, style = TextStyle(
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 16.sp
                                )
                                )
                                InputFieldCustom(
                                    onValueChange = {
                                        email.value = it
                                        emailError.value = !isValidEmail(it)
                                    },
                                    value = email.value,
                                    placeHolder = "Email Anda",
                                    onKeyEvent = {},
                                    type = InputType.Email
                                )
                                if(emailError.value){
                                    Text(text = "Tolong Masukkan Email Dengan Benar", color = Color.Red, style = TextStyle(
                                        fontWeight = FontWeight.Normal,
                                        fontSize = 13.sp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Column {
                                Text(text = "Password", color = MaterialTheme.colorScheme.primary, style = TextStyle(
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 16.sp
                                )
                                )
                                InputFieldCustom(
                                    onValueChange = {
                                        passwrod.value = it
                                    },
                                    value = passwrod.value,
                                    isIcon = true,
                                    placeHolder = "Password",
                                    onKeyEvent = {},
                                    type = InputType.Text
                                )
                                if(errorState.value.isNotEmpty()){
                                    Text(text = errorState.value, color = Color.Red, style = TextStyle(
                                        fontWeight = FontWeight.Normal,
                                        fontSize = 13.sp)
                                    )
                                }

                            }
                            Spacer(modifier = Modifier.height(15.dp))
                            Button(
                                enabled = isButtonEnabled.value ,
                                onClick = {
                                    errorState.value = ""
                                    authViewModel.loginUser(email.value, passwrod.value,
                                        onSuccess = {
                                            Toast.makeText(context, "Berhasil Login", Toast.LENGTH_SHORT).show()
                                            navController.navigate(BottomMenuScreen.HomePage.route) },
                                        onError = {
                                          errorState.value = it
                                        })

                            }, modifier = Modifier.fillMaxWidth()) {
                                Text(text = "Log In", modifier = Modifier.padding(vertical = 6.dp), style = TextStyle(
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 17.sp))
                            }
                            Spacer(modifier = Modifier.height(60.dp))
                            Text(text = "Login menggunakan Social Media", style = TextStyle(
                                color = Color.Black,
                                textAlign = TextAlign.Center
                            ),  modifier = Modifier
                                .padding(bottom = 10.dp)
                                .fillMaxWidth())
                            Spacer(modifier = Modifier.height(10.dp))
                            Row(verticalAlignment = Alignment.CenterVertically,  horizontalArrangement = Arrangement.SpaceAround, modifier = Modifier.fillMaxWidth()){
                                Button(onClick = { /*TODO*/ } , colors = ButtonDefaults.buttonColors( containerColor = Color.Blue)) {
                                    Row(verticalAlignment = Alignment.CenterVertically){
                                        Icon(painter = painterResource(id = R.drawable.asset2), tint = Color.White, modifier = Modifier
                                            .size(30.dp), contentDescription ="FB" )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(text = "Facebook",  color = Color.White, fontWeight = FontWeight.Bold)
                                    }
                                }
                                Button(onClick = { /*TODO*/ } , colors = ButtonDefaults.buttonColors(
                                    Color.Red)) {
                                    Row(verticalAlignment = Alignment.CenterVertically){
                                        Icon(painter = painterResource(id = R.drawable.asset4), tint = Color.White, modifier = Modifier
                                            .size(30.dp), contentDescription ="FB" )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(text = "Google",  color = Color.White, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }

                        }
                        Box(modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom = 50.dp), contentAlignment = Alignment.BottomEnd){
                            Column(horizontalAlignment = Alignment.End){
                                Text(text = "Belum punya Account ?")
                                TextButton(onClick = {
                                    Log.d("Button status", "${isButtonEnabled.value}")
                                    Log.d("Button status", "${email.value}")

                                    Log.d("Button status", "${passwrod.value}")

                                }) {
                                    Text(text = "Buat Sekarang")
                                }
                            }
                        }
                    }
                }
            }

        }
    }
}