package com.example.wapp.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wapp.screen.IntroScreenState


@Composable
fun IntroComponent(introScreenState: IntroScreenState){
    Box(modifier = Modifier.fillMaxSize()){
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()

        ){
            Spacer(modifier = Modifier.height(50.dp))
            Text(text = introScreenState.title,
                style = TextStyle(lineHeight = TextUnit.Unspecified),
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold,
                fontStyle = FontStyle.Italic,
                fontSize = 30.sp
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(text =introScreenState.subTitle,
                style = TextStyle(
                    lineHeight = 20.sp,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Light,

                    )
            )

            Spacer(modifier = Modifier.height(100.dp))
            Image(painter = painterResource(id =  introScreenState.idRes), contentDescription = "loading image", modifier = Modifier.size(300.dp))
        }
    }

}
