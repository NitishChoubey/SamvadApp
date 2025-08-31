package com.example.samvad.presentation.updatescreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.samvad.R
import com.example.samvad.presentation.bottomnavigation.BottomNavigation
import com.example.samvad.presentation.navigation.Routes

@Composable

fun UpdateScreen(navHostController : NavHostController) {

    val scrollState = rememberScrollState()
    val sampleStatus = listOf(
        StatusData(image = R.drawable.p5, name = "David", time = "5 min ago"),
        StatusData(image = R.drawable.p8, name = "Aaron", time = "15 min ago"),
        StatusData(image = R.drawable.p11, name = "Josh", time = "15 min ago")
    )

    val sampleChannel = listOf(
        Channels(image = R.drawable.neat_roots , name = "Neat Roots" , description = "Latest news in Tech"),
        Channels(image = R.drawable.rcblogo , name = "RCB" , description = "IPL Franchise") ,
        Channels(image = R.drawable.aajtk , name = "Aaj Tak" , description = "News Channel")
    )

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /*TODO*/ },
                containerColor = colorResource(id = R.color.light_green),
                modifier = Modifier.size(65.dp),
                contentColor = Color.White
            ) {
                Icon(
                    painterResource(id = R.drawable.baseline_photo_camera_24),
                    contentDescription = null
                )
            }
        },
        bottomBar = {
            BottomNavigation(navHostController , selectedItem = 0 , onClick = {index ->
                when(index){
                    0 -> navHostController.navigate(Routes.HomeScreen)
                    1 -> navHostController.navigate(Routes.UpdateScreen)
                    2 -> navHostController.navigate(Routes.CommunitiesScreen)
                    3 -> navHostController.navigate(Routes.CallScreen)
                }

            })
        },
        topBar = {
            TopBar()
        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {

            Text(
                text = "Status",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
            )

            MyStatus()  // it will import the design of "My Status()" function  from the StatusItem.kt file

            sampleStatus.forEach { data ->      // this importt the "StatusItem()" function from the StatusItem.kt file
                StatusItem(statusData = data)

            }

            Spacer(modifier = Modifier.height(16.dp))

            HorizontalDivider(color = Color.Gray)

            Column() {
                Text(
                    text = "Channels",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                )

                Text(
                    text = "Stay updated on topics that matter to you. Find channels to follow below.",
                    color = Color.Black,
                    modifier = Modifier.padding(horizontal  = 16.dp ,vertical  =8.dp )
                )

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = "Find channels to follow" ,
                    color = Color.Black ,
                    modifier = Modifier.padding(horizontal = 16.dp )

                )




            }
            Spacer(modifier =  Modifier.height(16.dp))
            
            sampleChannel.forEach{
                ChannelItemDesign(channels = it)
            }


        }


    }
}