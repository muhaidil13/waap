package com.example.wapp.screen.auth

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
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
fun SignUpScreen(navController: NavController,
                 sharedTransisionScope: SharedTransitionScope,
                 animatedVisibilityScope: AnimatedVisibilityScope,
                 authViewModel: AuthViewModel
){
    val scrolState = rememberScrollState()
    val username = remember {
        mutableStateOf("")
    }
    val email = remember {
        mutableStateOf("")
    }
    val phoneNumber = remember {
        mutableStateOf("")
    }
    val password = remember {
        mutableStateOf("")
    }
    val confirmPassword = remember {
        mutableStateOf("")
    }
    val errorColor = if (password.value != confirmPassword.value ) MaterialTheme.colorScheme.onError else MaterialTheme.colorScheme.primary


    val phoneNumberFocusRequest = remember {
        FocusRequester()
    }
    val  emailFocusRequest = remember {
        FocusRequester()
    }
    val  passwordFocusRequest = remember {
        FocusRequester()
    }
    val isButtonEnabled = remember {
        mutableStateOf(false)
    }
    val emailPattern = "[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}"
    val phonePattern = "^[0-9]{12,13}$"

    val emailError = remember { mutableStateOf(false) }
    val phoneError = remember { mutableStateOf(false) }

    val showPasswordError = remember{
        mutableStateOf(false)
    }

    fun isValidEmail(input: String): Boolean {
        return input.matches(emailPattern.toRegex())
    }


    fun isValidPhone(input: String): Boolean {
        return input.matches(phonePattern.toRegex())
    }


    val context = LocalContext.current
    SideEffect {
        isButtonEnabled.value = username.value.isNotEmpty() &&
                email.value.isNotEmpty() &&
                phoneNumber.value.isNotEmpty() &&
                password.value.isNotEmpty() &&
                confirmPassword.value == password.value

    }

    with(sharedTransisionScope){
        Scaffold(topBar = {
            LargeTopAppBar(title = { Text(text = "Buat Akun Anda", color = MaterialTheme.colorScheme.onPrimary) }, colors = TopAppBarDefaults.topAppBarColors(
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
            Box(modifier = Modifier
                .fillMaxSize()
                .background(Brush.horizontalGradient(listOf(Color(0xFF6C63FF), Color(0xFF1A436B))))
                .padding(top = innerPadding.calculateTopPadding())){
                Box(modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
                    .background(Color.White)
                    .padding(vertical = 20.dp, horizontal = 30.dp)
                )
                {
                    Column(modifier = Modifier
                        .verticalScroll(scrolState)
                        .windowInsetsPadding(WindowInsets.ime)
                        .padding(bottom = innerPadding.calculateBottomPadding() + 10.dp), verticalArrangement = Arrangement.spacedBy(10.dp)){
                        Spacer(modifier = Modifier.height(20.dp))
                        Text(text = "Sign Up",
                            style = TextStyle(
                                fontSize = 25.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            ),
                            modifier = Modifier.sharedElement(
                                sharedTransisionScope.rememberSharedContentState(key = "Text-SignUp"),
                                animatedVisibilityScope = animatedVisibilityScope
                            ))
                        Spacer(modifier = Modifier.height(20.dp))
                       Column{
                           Text(text = "Nama Anda", color = MaterialTheme.colorScheme.primary, style = TextStyle(
                               fontWeight = FontWeight.Normal,
                               fontSize = 16.sp
                           )
                           )
                           InputFieldCustom(onValueChange = {
                               username.value = it
                           }, value = username.value, placeHolder = "Nama Anda", onKeyEvent = {
                               emailFocusRequest.requestFocus()
                           },
                               type = InputType.Text,
                               focusRequester = null
                           )
                       }
                        Column{
                            Text(text = "Email", color = MaterialTheme.colorScheme.primary, style = TextStyle(
                                fontWeight = FontWeight.Normal,
                                fontSize = 16.sp
                            )
                            )
                            InputFieldCustom(onValueChange = {
                                email.value = it
                                emailError.value=!isValidEmail(it)
                            }, value = email.value, placeHolder = "Email", onKeyEvent = {
                                phoneNumberFocusRequest.requestFocus()
                            }, type = InputType.Email, focusRequester = emailFocusRequest)
                            if(emailError.value){
                                Text(text = "Please enter a valid Email", color = Color.Red, style = TextStyle(
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 13.sp)
                                )
                            }
                        }
                        Column{
                            Text(text = "Nomor Telpon", color = MaterialTheme.colorScheme.primary, style = TextStyle(
                                fontWeight = FontWeight.Normal,
                                fontSize = 16.sp
                            )
                            )
                            InputFieldCustom(onValueChange = {
                                phoneNumber.value = it
                                phoneError.value = !isValidPhone(it)
                            }, value = phoneNumber.value, placeHolder = "Nomor Telpon",
                                onKeyEvent = {
                                    passwordFocusRequest.requestFocus()

                                },
                                type = InputType.Phone
                                )
                            if(phoneError.value){
                                Text(text = "Tolong Masukkan Degan Format 08XXX", color = Color.Red, style = TextStyle(
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 13.sp)
                                )
                                Text(text = "Contoh: 08XXXXXXX", color = Color.Gray, style = TextStyle(
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 13.sp)
                                )
                            }
                        }

                        Column{
                            Text(text = "Password", color = MaterialTheme.colorScheme.primary, style = TextStyle(
                                fontWeight = FontWeight.Normal,
                                fontSize = 16.sp
                            )
                            )
                            InputFieldCustom(onValueChange = {
                                password.value = it
                            }, value = password.value, placeHolder = "Password",
                                isIcon = true,
                                onKeyEvent = {},
                                type = InputType.Text
                            )
                            if(showPasswordError.value){
                                Text(text = "Please Provide Password at Least 6 ", color = Color.Red, style = TextStyle(
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 13.sp)
                                )
                            }
                        }
                        Column{
                            Text(text = "Confirm Password", color =errorColor, style = TextStyle(
                                fontWeight = FontWeight.Normal,
                                fontSize = 16.sp)
                            )
                            InputFieldCustom(onValueChange = {confirmPassword.value = it}, value = confirmPassword.value, isIcon = true,
                                placeHolder = "Confirm",
                                onKeyEvent = {},
                                type = InputType.Text)
                            if(password.value != confirmPassword.value){
                                Text(text = "Password Not Match", color = Color.Red, style = TextStyle(
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 13.sp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(15.dp))
                        Button(onClick = {
                            Log.d("Create Users", "${isButtonEnabled.value}")
                            if(isButtonEnabled.value){
                                if(password.value.length <= 5){
                                    showPasswordError.value = true
                                }else{
                                    authViewModel.registerUser(
                                        name = username.value,
                                        email = email.value,
                                        phone = phoneNumber.value,
                                        onSuccess = {
                                            Toast.makeText(context, "Berhasil Membuat Akun Baru", Toast.LENGTH_LONG).show()
                                            navController.navigate(BottomMenuScreen.HomePage.route)
                                        },
                                        onError = {
                                            Toast.makeText(context, "Gagal Membuat Akun Baru", Toast.LENGTH_LONG).show()
                                        },
                                        password = password.value
                                    )
                                }

                                Log.d("Create Users", "ddodoooo")
                            }else{
                                Log.d("Create Users", "pepepep")
                            }
                        }, modifier = Modifier.fillMaxWidth(),
                            enabled = isButtonEnabled.value) {
                            Text(text = "Sign Up", modifier = Modifier.padding(vertical = 6.dp), style = TextStyle(
                                fontWeight = FontWeight.Normal,
                                fontSize = 17.sp
                            )
                            )
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
                }
            }

        }
    }
}