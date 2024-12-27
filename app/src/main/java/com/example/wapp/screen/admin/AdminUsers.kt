package com.example.wapp.screen.admin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wapp.components.UserCard
import com.example.wapp.screen.auth.AuthViewModel

@Composable
fun AdminUsers(authViewModel: AuthViewModel){
    val listState = rememberLazyListState()
    val context = LocalContext.current
    val users = authViewModel.users.collectAsState()
    val idCurrentUser = authViewModel.currentUser?.uid

    LaunchedEffect(Unit){
        authViewModel.getAllUsers(context)
    }

    Scaffold { innerPadding ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 10.dp)){
            Column(modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(top = 10.dp, end = 10.dp, start = 10.dp)) {
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "Pengguna Aplikasi Anda:", style = TextStyle(
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Normal,
                        fontSize = 20.sp
                    )
                )
                Spacer(modifier = Modifier.height(20.dp))
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    users.value?.let {
                        items(it) {item ->
                            idCurrentUser?.let { userId ->
                                UserCard(item, userId, authViewModel)

                            }
                        }
                    }?: item {
                        Text(
                            text = "Harap Menunggu Ya Ges", style = TextStyle(
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Normal,
                                fontSize = 20.sp
                            )
                        )
                    }
                }
            }
        }
    }
}