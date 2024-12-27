package com.example.wapp.screen.components

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wapp.R
import com.example.wapp.screen.auth.AuthViewModel

@Composable
fun TentangComponents(paddingValues: PaddingValues, authViewModel: AuthViewModel, onLogout:()-> Unit){
    val context = LocalContext.current
    val scroll = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .verticalScroll(scroll)
            .padding(bottom = 50.dp)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 20.dp)
            .background(MaterialTheme.colorScheme.onPrimary),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(15.dp)

        ){
            Icon(imageVector = Icons.Filled.Person, modifier = Modifier.size(30.dp), tint = MaterialTheme.colorScheme.primary, contentDescription = "")
            Text(text = "Tentang Pembuat", style = TextStyle(color = MaterialTheme.colorScheme.primary, fontSize = 20.sp, fontWeight = FontWeight.SemiBold))
        }
        Spacer(modifier = Modifier.height(10.dp))
        Image(
            painter = painterResource(id = R.drawable.red_mark),
            contentDescription = "Our Team",
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.primaryContainer)
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Muh Farid Wajdi A", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.SemiBold)

        Button(
            onClick = {
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Text(
                text = "See My Social",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            IconButton(onClick = {  }) {
                Icon(
                    imageVector = Icons.Outlined.Favorite,
                    contentDescription = "Oke",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
            IconButton(onClick = {  }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
            IconButton(onClick = {  }) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Orang",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Tentang Saya",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "To deliver the best app experience with passion and creativity!",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Justify
        )

        Box(modifier = Modifier
            .fillMaxSize()
            .padding(bottom = paddingValues.calculateBottomPadding() + 100.dp), contentAlignment = Alignment.BottomCenter){
                Button(onClick = {
                    authViewModel.logout {
                        onLogout()
                        Toast.makeText(context, "Berhasil Keluar", Toast.LENGTH_SHORT).show()
                    }
                }, modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp), colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.onPrimary), border = BorderStroke(1.dp,
                    Color.Red)) {
                    Row( modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically){
                        Icon(imageVector = Icons.AutoMirrored.Filled.ExitToApp, modifier = Modifier.size(30.dp), tint = Color.Red, contentDescription ="" )
                        Text("Keluar", style=TextStyle(
                            color = Color.Red,
                            fontWeight = FontWeight.Normal,
                            fontSize = 20.sp
                        ))
                    }

            }
        }
    }

}