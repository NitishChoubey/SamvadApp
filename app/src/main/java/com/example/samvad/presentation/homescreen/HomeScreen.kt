package com.example.samvad.presentation.homescreen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.samvad.R
import com.example.samvad.presentation.bottomnavigation.BottomNavigation

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
@Preview(showSystemUi = true)
fun HomeScreen() {

    val chatData = listOf(
        ChatDesignModel(
            R.drawable.p1,
            name = "Dave Gray",
            time = "10:00 A.M.",
            message = "Hi"
        ),
        ChatDesignModel(
            image = R.drawable.p2,
            name = "Smith",
            time = "9:00 P.M.",
            message = "Hello"
        ),
        ChatDesignModel(
            image = R.drawable.p3,
            name = "David",
            time = "9:00 P.M.",
            message = "Hello"
        ),
        ChatDesignModel(
            image = R.drawable.p4,
            name = "Vance",
            time = "9:00 P.M.",
            message = "Hello"
        ),
        ChatDesignModel(
            image = R.drawable.p5,
            name = "Nick",
            time = "9:00 P.M.",
            message = "Hello"
        ),
        ChatDesignModel(
            image = R.drawable.p6,
            name = "Denis",
            time = "9:00 P.M.",
            message = "Hello"
        ),
        ChatDesignModel(
            image = R.drawable.p7,
            name = "Maxwell",
            time = "9:00 P.M.",
            message = "Hello"
        ),
        ChatDesignModel(
            image = R.drawable.p8,
            name = "Aaron",
            time = "9:00 P.M.",
            message = "Hello"
        ),
        ChatDesignModel(
            image = R.drawable.p9,
            name = "Cook",
            time = "9:00 P.M.",
            message = "Hello"
        ),
        ChatDesignModel(
            image = R.drawable.p10,
            name = "Boult",
            time = "9:00 P.M.",
            message = "Hello"
        ),
        ChatDesignModel(
            image = R.drawable.p11,
            name = "Josh",
            time = "9:00 P.M.",
            message = "Hello"
        ),
        ChatDesignModel(
            image = R.drawable.p12,
            name = "Ben",
            time = "9:00 P.M.",
            message = "Hello"
        ),


        )


    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /*TODO*/ },
                containerColor = colorResource(id = R.color.light_green),
                modifier = Modifier.size(65.dp),
                contentColor = Color.Black
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.add_chat_icon),
                    contentDescription = null,
                    modifier = Modifier.size(28.dp)
                )
            }
        },

        bottomBar = {
            BottomNavigation()
        }
    ) { //it is used here to set the bottom bar  navigation , top app bar and the floating button
        Column(modifier = Modifier.padding(it)) {

            Spacer(modifier = Modifier.height(16.dp))
            Box(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "WhatsApp",
                    fontSize = 28.sp,
                    color = colorResource(id = R.color.light_green),
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 16.dp),
                    fontWeight = FontWeight.Bold

                )

                Row(modifier = Modifier.align(Alignment.CenterEnd)) {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            painter = painterResource(id = R.drawable.camera),
                            contentDescription = null,
                            modifier = Modifier.size(24.dp),
                        )

                    }
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            painter = painterResource(id = R.drawable.search),
                            contentDescription = null,
                            modifier = Modifier.size(24.dp),
                        )


                    }
                    IconButton(onClick = {}) {
                        Icon(
                            painter = painterResource(id = R.drawable.more),
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
            HorizontalDivider()

            LazyColumn() {
                items(chatData) {
                    ChatDesign(chatDesignModel = it)
                }
            }
        }

        //LazyColumn() --> vertical scroll feature , recycler view jitna jarurat hai screen pe dikhaane ka utna hi content dikhaaega screen pe


    }
}