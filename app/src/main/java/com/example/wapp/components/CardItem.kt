package com.example.wapp.components


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.wapp.RouteApp


@OptIn(ExperimentalLayoutApi::class)
@Composable

fun CardItemsCount(navController: NavController){

    val dataText = listOf(
        RouteApp.UsersScreeen,
        RouteApp.AdminWisata,
        RouteApp.AdminKuliner,
        RouteApp.ProfileScreen
    )

    FlowRow(
            maxItemsInEachRow = 2,
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            dataText.forEachIndexed { index, data ->
                Box(
                    modifier = Modifier
                        .weight(.5f)
                        .shadow(1.dp, RoundedCornerShape(20.dp), clip = true)
                        .clip(RoundedCornerShape(10.dp))
                        .background(MaterialTheme.colorScheme.onPrimary)
                        .height(150.dp)
                        .clickable {
                            navController.navigate(data.routeid)
                        }
                ){
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        Text(text =data.title, style = TextStyle(
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 23.sp,
                            fontWeight = FontWeight.Normal,
                        ))

                    }
                }
            }
        }

}
