package com.example.wapp.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wapp.data.User
import com.example.wapp.screen.auth.AuthViewModel

@Composable
fun  UserCard(item: User, currentUserId: String, authViewModel: AuthViewModel){

    val dialogValue = remember {
        mutableStateOf("")
    }
    val showDialog = remember {
        mutableStateOf(false)
    }
    val context = LocalContext.current
    Box(modifier = Modifier
        .fillMaxWidth()
        .shadow(2.dp, shape = RoundedCornerShape(20.dp), clip = false)
        .clip(RoundedCornerShape(20.dp))
        .background(MaterialTheme.colorScheme.onPrimary)
        .height(150.dp)
        .padding(vertical = 10.dp, horizontal = 12.dp))
    {
        Column(verticalArrangement = Arrangement.spacedBy(20.dp)){
            Row(horizontalArrangement = Arrangement.spacedBy(20.dp)){
                Icon(imageVector = Icons.Filled.Person, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(60.dp), contentDescription = "")
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(20.dp)){
                        Text(text = item.name, style = TextStyle(
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Normal,
                            fontSize = 15.sp
                        )
                        )
                        if(item.role == "admin"){
                            Column(
                                modifier = Modifier
                                    .background(MaterialTheme.colorScheme.onPrimary)
                                    .border(
                                        BorderStroke(
                                            2.dp,
                                            color = MaterialTheme.colorScheme.primary
                                        ), shape = RoundedCornerShape(20.dp)
                                    )
                            ){
                                Text(text = item.role, modifier =  Modifier.padding(horizontal = 10.dp, vertical = 5.dp), style = TextStyle(
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 15.sp
                                )
                                )
                            }
                        }
                    }
                    Text(text = item.email, style = TextStyle(
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Normal,
                        fontSize = 20.sp
                    )
                    )
                    Text(text = item.phone, style = TextStyle(
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Normal,
                        fontSize = 15.sp
                    )
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                Column(modifier = Modifier.weight(1f)){
                    Text(text = "Terakhir Login :", style = TextStyle(
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp
                    )
                    )
                    Text(text = item.lastLogin, style = TextStyle(
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Normal,
                        fontSize = 12.sp)
                    )
                }
                Row(horizontalArrangement = Arrangement.spacedBy(20.dp)){
                    if(item.id != currentUserId){

                        IconButton(onClick = {
                            dialogValue.value = "EDIT"
                            showDialog.value = true
                        }) {
                            Icon(
                                imageVector = Icons.Outlined.Edit,
                                tint = Color.Blue,
                                contentDescription = ""
                            )
                        }
                    }else{
                        Text(text = "Akun Anda", style = TextStyle(
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Normal,
                            fontSize = 20.sp)
                        )
                    }

                }
            }
            when(dialogValue.value){
                "EDIT" -> if(showDialog.value){
                    EditUserDialog(user = item, onDismis = {
                        showDialog.value = false
                    }) { user ->

                        val userInfo = item.copy(
                            name = user.name,
                            email = user.email,
                            phone = user.phone,
                            role = user.role
                        )
                        authViewModel.updateUserByUserUid(
                            context =context,
                            item = userInfo
                        )
                        showDialog.value = false
                        authViewModel.getAllUsers(context)

                    }
                }

            }
        }
    }
}