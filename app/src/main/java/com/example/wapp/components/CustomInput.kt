package com.example.wapp.components

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.wapp.screen.components.InputType


@Composable
fun InputFieldCustom(
    onValueChange: (String) -> Unit,
    value: String,
    placeHolder: String,
    modifier: Modifier = Modifier,
    isIcon: Boolean = false,
    maxLine: Int = Int.MAX_VALUE,
    onKeyEvent: () -> Unit,
    type: InputType?,
    focusRequester: FocusRequester? = null // Add FocusRequester parameter

) {
    var passwordVisible by rememberSaveable { mutableStateOf(false) }


    val keyboardOptioons = when(type){
        InputType.Email -> {
            KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Email,

            )
        }
        InputType.Phone ->{
            KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Phone,

            )
        }
        InputType.Text -> {
            KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text,

            )
        }
        null -> {
            KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text,

            )
        }
    }

    val keyboardActions = KeyboardActions(

        onNext = {
            onKeyEvent()
        }
    )



    Column {
        OutlinedTextField(
            value = value,
            maxLines = maxLine,
            shape = RoundedCornerShape(10.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = Color.Gray,
            ),
            visualTransformation = if (passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,
            placeholder = {
                Text(
                    text = placeHolder,
                    style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray)
                )
            },
            prefix = {
                when(type){
                    InputType.Email -> Icon(
                        imageVector = Icons.Filled.Email,
                        contentDescription = ""
                    )
                    InputType.Phone -> Text(text = "ID", style = TextStyle(
                        color = MaterialTheme.colorScheme.primary
                    ))
                    else -> {}
                }
            },
            trailingIcon = {
                if (isIcon) {
                    val image = if (passwordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = image, contentDescription = "Toggle Password Visibility")
                    }
                }
            },
            keyboardOptions = keyboardOptioons,
            keyboardActions =keyboardActions ,
            onValueChange = onValueChange,
            modifier = if (maxLine != Int.MAX_VALUE) {
                modifier
                    .fillMaxWidth()
                    .windowInsetsPadding(WindowInsets.ime)
                    .height(180.dp)
                    .focusRequester(focusRequester ?: FocusRequester()) // Apply the FocusRequester



            } else {
                modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester ?: FocusRequester()) // Apply the FocusRequester

            }
        )
    }
}
