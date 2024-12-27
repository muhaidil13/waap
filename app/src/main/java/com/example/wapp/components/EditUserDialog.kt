package com.example.wapp.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wapp.data.User
import com.example.wapp.hideKeyboard
import com.example.wapp.screen.components.InputType
import kotlinx.coroutines.launch


@Composable
fun EditUserDialog( user:User, onDismis:() -> Unit, onSubmit: (User) -> Unit,) {
    val scrollState = rememberScrollState()
    val name = remember {
        mutableStateOf( user.name)
    }

    val email  = remember {
        mutableStateOf(user.email)
    }
    val phone = remember {
        mutableStateOf(user.phone)
    }

    val role = remember{
        mutableStateOf( user.role == "user")
    }


    val isButtonEnabled = remember {
        mutableStateOf(false)
    }
    LaunchedEffect(name.value, phone.value){
        isButtonEnabled.value = name.value.isNotEmpty() && phone.value.isNotEmpty()
    }
    val courutineScope = rememberCoroutineScope()


    val context = LocalContext.current


    AlertDialog(
        modifier = Modifier.clickable {
            courutineScope.launch {
                hideKeyboard(context)
            }

        },
        onDismissRequest = {onDismis() },
        confirmButton = {
            Button(
                modifier = Modifier.windowInsetsPadding(WindowInsets.ime),

                onClick = {
                    val userInfo = user.copy(
                        name = name.value,
                        email = email.value,
                        phone = phone.value,
                        role =  if(role.value ) "user" else "admin"
                    )
                    onSubmit(userInfo)
                },) {
                Text(text = "Selesai")
            }

        },
        title = {
            Text(text = "Edit User!!", color = MaterialTheme.colorScheme.primary, style = TextStyle(
                fontWeight = FontWeight.SemiBold,
                fontSize = 25.sp)
            )  },
        icon = { Icon(imageVector = Icons.Outlined.Add, modifier = Modifier.size(50.dp), tint = MaterialTheme.colorScheme.primary , contentDescription = "Add") },
        text = {
            Column (
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(scrollState),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ){
                Column{
                    Text(text = "Nama Pengguna", color = MaterialTheme.colorScheme.primary, style = TextStyle(
                        fontWeight = FontWeight.Normal,
                        fontSize = 16.sp)
                    )
                    InputFieldCustom(onValueChange = {
                        name.value = it
                    }, value = name.value, placeHolder = "Nama Pengguna", onKeyEvent = {

                    },
                        type = InputType.Text,
                        focusRequester = null
                    )
                }
                Column{
                    Text(text = "Email Pengguna", color = MaterialTheme.colorScheme.primary, style = TextStyle(
                        fontWeight = FontWeight.Normal,
                        fontSize = 16.sp)
                    )
                    Text(text = email.value, color = MaterialTheme.colorScheme.primary, style = TextStyle(
                        fontWeight = FontWeight.Normal,
                        fontSize = 16.sp)
                    )

                }
                Column{
                    Text(text = "Phone Pengguna", color = MaterialTheme.colorScheme.primary, style = TextStyle(
                        fontWeight = FontWeight.Normal,
                        fontSize = 16.sp
                    )
                    )
                    InputFieldCustom(onValueChange = {
                        phone.value = it
                    }, value = phone.value, placeHolder = "Phone Pengguna", onKeyEvent = {

                    },
                        type = InputType.Text,
                        focusRequester = null
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ){
                    Text(text = "Sebagai :", color = MaterialTheme.colorScheme.primary, style = TextStyle(
                        fontWeight = FontWeight.Normal,
                        fontSize = 16.sp)
                    )
                    Text(text = if(role.value) "user" else "admin", color = MaterialTheme.colorScheme.primary, style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp)
                    )
                }
                Column {
                    Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically){
                        RadioButton(selected = role.value, onClick = {
                            role.value = true
                        })
                        Text(text = "User", color = if (role.value) Color.Blue else Color.Black)
                        RadioButton(selected = !role.value, onClick = {
                            role.value = false
                        })
                        Text(text = "Admin", color = if (!role.value) Color.Blue else Color.Black)
                    }
                }

            }

        }
    )
}