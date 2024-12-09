package com.example.wapp.screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.wapp.components.BottomMenu
import com.example.wapp.components.MenuItems
import com.example.wapp.screen.auth.AuthViewModel

@Composable
fun HomeComponents(paddingValues: PaddingValues,
                   navController: NavController,
                   authViewModel: AuthViewModel
){
    val scroll = rememberScrollState()

    val userInfo by authViewModel.userInfo.collectAsState()

    var error by remember {
        mutableStateOf("")
    }




    BoxWithConstraints(modifier = Modifier
        .fillMaxSize()
        .padding(bottom = paddingValues.calculateBottomPadding()))
    {
        val maxheight = maxHeight
        Box(modifier = Modifier
            .fillMaxWidth()
            .height(maxheight)){
            Column(modifier = Modifier.fillMaxSize()){
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.onPrimary)
                    .padding(horizontal = 10.dp)
                    .padding(top = 60.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(15.dp)

                ){
                    Icon(imageVector = Icons.Filled.Home, modifier = Modifier.size(30.dp), tint = MaterialTheme.colorScheme.primary, contentDescription = "")
                    Text(text = "Selamat Datang", style = TextStyle(color = MaterialTheme.colorScheme.primary, fontSize = 20.sp, fontWeight = FontWeight.SemiBold))
                }
                Spacer(modifier = Modifier.height(30.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .verticalScroll(scroll)
                        .padding(horizontal = 16.dp,)
                ) {

                    Text(text = "Petunjuk Penggunaan", style = TextStyle(
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Light
                    )
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(text = "Halaman Home Anda Dapat Menambahkan Peta pada Halaman Peta Dunia Dengan Cara Clik Tahan Akan Ada Form Untuk Pengisian Data",
                        style = TextStyle(
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Justify,
                            fontWeight = FontWeight.Light
                        ),
                    )

                    Spacer(modifier = Modifier.height(24.dp))
                    userInfo?.let {
                        MenuItems(navController =  navController, role = it.role)

                    }
                }
            }
        }



    }
}