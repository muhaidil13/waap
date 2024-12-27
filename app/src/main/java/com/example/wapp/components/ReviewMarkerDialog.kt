package com.example.wapp.components

import android.Manifest
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Reviews
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wapp.data.Markers
import com.example.wapp.data.Reviews
import com.example.wapp.hideKeyboard
import com.example.wapp.screen.components.InputType
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.mapbox.geojson.Point
import kotlinx.coroutines.launch


@Composable
fun ReviewMarkerDialog(userId: String, markerId: String, onDismis:() -> Unit, onSubmit: (Reviews) -> Unit, ) {
    val review = remember {
        mutableStateOf("")
    }

    var currentRating by remember { mutableStateOf(3f) }

    val scrollState = rememberScrollState()

    val isButtonEnabled = remember {
        mutableStateOf(false)
    }
    LaunchedEffect(review.value){
        isButtonEnabled.value = review.value.isNotEmpty()
    }

    val context = LocalContext.current

    val courutineScope = rememberCoroutineScope()

    AlertDialog(
        modifier = Modifier.clickable {
            courutineScope.launch {
                hideKeyboard(context)
            }
        },
        onDismissRequest = {onDismis() },
        confirmButton = {
            Button(
                onClick = {
                onSubmit(
                   Reviews(
                       markerId = markerId,
                       userId = userId,
                       rating = currentRating,
                       review = review.value
                   )
                ) },) {
                Text(text = "Kirim")
            }

        },
        title = {
            Text(text = "Tambahkan Review Anda", color = MaterialTheme.colorScheme.primary, style = TextStyle(
                fontWeight = FontWeight.SemiBold,
                fontSize = 25.sp)
            )  },
        icon = { Icon(imageVector = Icons.Outlined.Reviews, modifier = Modifier.size(50.dp), tint = MaterialTheme.colorScheme.primary , contentDescription = "Add") },
        text = {
            Column (
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(scrollState),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ){
                StarRating(
                    rating = currentRating,
                    onRatingChange = { newRating ->
                        currentRating = newRating
                    }
                )
                Column{
                    Text(text = "Review", color = MaterialTheme.colorScheme.primary, style = TextStyle(
                        fontWeight = FontWeight.Normal,
                        fontSize = 16.sp)
                    )
                    InputFieldCustom(onValueChange = {
                        review.value = it
                    }, value =  review.value, placeHolder = "Review Anda", onKeyEvent = {

                    },
                        type = InputType.Text,
                        focusRequester = null,
                        maxLine = Int.MAX_VALUE
                    )
                }

            }

        }
    )
}