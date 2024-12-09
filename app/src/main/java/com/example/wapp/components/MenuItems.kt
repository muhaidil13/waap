package com.example.wapp.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowColumn
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.TextSnippet
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Interests
import androidx.compose.material.icons.filled.LocalDining
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MoreTime
import androidx.compose.material.icons.filled.PanoramaFishEye
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Support
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.wapp.RouteApp
import com.mapbox.maps.extension.style.expressions.dsl.generated.length


data class menuItem(
    val title: String,
    val icon: ImageVector,
    val onClick: () -> Unit,
    val destination: String
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MenuItems(navController: NavController, role: String){
    val menuItemsData = listOf(
        listOf(
            if(role == "admin") menuItem(
                icon = Icons.Filled.Interests,
                title = "Admin",
                onClick = {},
                destination = RouteApp.AdminHome.routeid
            ) else menuItem(
                icon = Icons.Filled.Person,
                title = "Profile",
                onClick = {},
                destination = RouteApp.ProfileScreen.routeid
            ),
            menuItem(
                icon = Icons.Filled.LocationOn,
                title = "Full Maps",
                onClick = {},
                destination = RouteApp.FullMapScreen.routeid
            ),
        ),
        listOf(
            menuItem(
            icon = Icons.Filled.PanoramaFishEye,
            title = "Wisata",
            onClick = {},
            destination = RouteApp.WisataScreen.routeid
        ),
            menuItem(
                icon = Icons.Filled.LocalDining,
                title = "Kuliner",
                onClick = {},
                destination = RouteApp.KulinerScreen.routeid
            )
        )
    )
    val padding = 30.dp
    val paddinghorizontal = 9.dp
    val fontsize = 15.sp
    val iconSize = 35.dp
    val color1 = Color.White
    Column(
        modifier = Modifier.padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = "Menu", style = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.primary
            )
        )
        Spacer(modifier = Modifier.height(5.dp))

        menuItemsData.forEach {  menuItems: List<menuItem> ->
            Row(modifier = Modifier.fillMaxWidth(),  verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp) )
            {
                menuItems.forEach {
                    Column(modifier = Modifier
                        .weight(.5f)
                        .shadow(1.dp, RoundedCornerShape(10.dp), clip = true)
                        .clip(RoundedCornerShape(10.dp))
                        .background(color1)
                        .clickable {
                            navController.navigate(it.destination)
                        },
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        Column (
                            modifier = Modifier.padding(vertical = padding, horizontal = paddinghorizontal),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ){
                            Icon(imageVector = it.icon, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(iconSize), contentDescription = it.title)
                            Spacer(modifier = Modifier.height(5.dp))
                            Text(text = it.title, style = TextStyle(
                                fontSize = fontsize,
                                fontWeight = FontWeight.Normal,
                                color = MaterialTheme.colorScheme.primary)
                            )
                        }
                    }
                }

            }

        }

    }

}
