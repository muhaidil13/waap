package com.example.wapp.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wapp.data.Markers
import com.example.wapp.screen.components.InputType
import com.mapbox.geojson.Point


@Composable
fun DeleteMarkerDialog(onDismis:() -> Unit, onSubmit: () -> Unit ) {

    AlertDialog(
        onDismissRequest = {onDismis() },
        confirmButton = {
          Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(15.dp)
          ){
              Button(onClick = {
                  onDismis()
              }, modifier = Modifier.weight(.5f), colors = ButtonDefaults.buttonColors(Color.White)) {
                  Text(text = "Cancel Woy", color = MaterialTheme.colorScheme.primary, style = TextStyle(
                      fontWeight = FontWeight.Normal,
                      fontSize = 15.sp)
                  )
              }
              Button(onClick = {
                  onSubmit()
              },modifier = Modifier.weight(.5f), colors = ButtonDefaults.buttonColors(Color.Red)) {
                  Text(text = "Gass!", color = MaterialTheme.colorScheme.onPrimary, style = TextStyle(
                      fontWeight = FontWeight.Normal,
                      fontSize = 15.sp)
                  )
              }
          }
        },

        title = {
            Text(text = "Hapus Marker ", color = Color.Red, style = TextStyle(
                fontWeight = FontWeight.SemiBold,
                fontSize = 23.sp)
            )  },
        icon = { Icon(imageVector = Icons.Outlined.Delete, modifier = Modifier.size(40.dp), tint = Color.Red , contentDescription = "Add") },
        text = {
            Text(text = "Anda Ingin Menghapus Marker Ini", color = MaterialTheme.colorScheme.primary, style = TextStyle(
                fontWeight = FontWeight.Normal,
                fontSize = 15.sp)
            )
        }
    )
}