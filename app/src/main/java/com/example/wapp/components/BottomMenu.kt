package com.example.wapp.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wapp.screen.components.BottomMenuScreen


@Composable
fun BottomMenu(activeIndex: Int, onClick: (Int) -> Unit, menuItems: List<BottomMenuScreen>){

    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 20.dp)
        .shadow(1.dp, RoundedCornerShape(20.dp), clip = true)
        .clip(RoundedCornerShape(10.dp))
        .background(MaterialTheme.colorScheme.onPrimary)
        .height(90.dp)
        ,
        verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceEvenly){
        menuItems.forEachIndexed {index, item ->
            val isSelected = activeIndex == index
            val iconTint = if (isSelected) MaterialTheme.colorScheme.primary else Color.Gray
            val textColor = if (isSelected) MaterialTheme.colorScheme.primary else Color.Gray
            Column(modifier = Modifier
                .clip(RoundedCornerShape(100))
                .padding(10.dp)
                .clickable {
                    onClick(index)
                },
                verticalArrangement = Arrangement.spacedBy(10.dp), horizontalAlignment = Alignment.CenterHorizontally){
                Icon(imageVector = item.icon, contentDescription = "", tint = iconTint)
                Text(text = item.title , style= TextStyle(
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Normal,
                    color = textColor
                )
                )
            }
        }
    }
}