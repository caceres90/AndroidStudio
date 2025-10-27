package com.example.banksafe

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices.NEXUS_5
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun main1(){
    Box(){
        Column () {
            Text(
                text = "My name is Jhon Alexander"
            )
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = "Image person"
            )
            Text(
                text = "I'm a Front End developer"
            )
        }
        Box(){
            footerBar()
        }
    }
}

@Composable
fun footerBar(){
    Row(){
        items()
        items()
        items()
    }
}

@Composable
fun items(){
    Box(){

    }
}

@Preview(showBackground = true, showSystemUi = true, device = NEXUS_5)
@Composable()
fun preview(){
    main1()
}