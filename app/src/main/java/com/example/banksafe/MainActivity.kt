package com.example.banksafe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices.NEXUS_5
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.banksafe.ui.theme.BankSafeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BankSafeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = Modifier.background(Color.Red)
    )
}

val Columna = Modifier
    .fillMaxWidth()
    .fillMaxSize()
    .border(2.dp, Color.Red)

val Fila = Modifier
    .fillMaxWidth()
    .height(40.dp)
    .border(2.dp, Color.Blue)

val Columna2 = Modifier
    .border(2.dp, Color.Black)

@Composable
fun Home(){
    Box(
        modifier = Modifier
            .fillMaxSize()

    ){
        Column (
            modifier = Columna,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Row (
                modifier = Fila
            ) {
                Text(
                    text = "Hola a todos"
                )
            }
            Row (
                modifier = Fila,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column (
                    modifier = Columna2
                ) {
                    Text(
                        text = "1"
                    )
                    Text(
                        text = "1"
                    )
                }
                Column (
                    modifier = Columna2
                ) {
                    Text(
                        text = "1"
                    )
                    Text(
                        text = "2"
                    )
                }
                Column (
                    modifier = Columna2
                ) {
                    Text(
                        text = "1"
                    )
                    Text(
                        text = "3"
                    )
                }
            }
            Row (
                modifier = Fila
            ) {
                Text(
                    text = "Hola a todos"
                )
            }
        }
    }
}

//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//fun GreetingPreview() {
//    BankSafeTheme {
//        Greeting("Android")
//    }
//}

//@Preview(showBackground = true, showSystemUi = true, device = NEXUS_5)
//@Composable()
//fun preview(){
//    Home()
//}