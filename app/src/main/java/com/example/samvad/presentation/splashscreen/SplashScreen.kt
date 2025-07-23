package com.example.samvad.presentation.splashscreen
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.compose.ui.res.painterResource

import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.samvad.R
import com.example.samvad.presentation.navigation.Routes
import kotlinx.coroutines.delay

@Composable

fun SplashScreen(navHostController : NavHostController) { //ui code will be in this function . All the UI will render on this screen
    //
    //Jab bhi user app ko open krega toh splash screen user ko 2 seconds ke liye dikhna chahiye....iska logic likhenge ab hmlog
    //iss chij ko acheive krne ke liye we will use "LaunchedEffect"  --> LaunchedEffect ko us krke hmlog SplashScreen ko delay krenge 2 second ke liye
    LaunchedEffect(Unit){
        // LaunchedEffect ke andrr likha hua code kewal 1 hi baar run krega.
        delay(2000)
        // iske baad jb hmara screen delay hoga 2 seconds ke liye iske baad hm kis screen pe jaaenge wo bhi hme add krna hota hai iske andrr ...is chij ko add krne ke liye hme NavController ka help lena hota hai
        navHostController.navigate(Routes.WelcomeScreen){
            popUpTo<Routes.SplashScreen>{inclusive = true}
        }  // yha prr navHostController.navigate() use kraa hai splash screen se Welcome screen pe jaane ke liye
        //now if Welcome Screen pe jaane ke baad user back krta hai toh hme phir se Splash Screen nhi dikhna chahiye ...toh iss chij ko krne ke liye hme "navBackStack" ko clear krna hota hai  ...toh ham navBackStack ko clear krne ke liye use
        //use krenge "popUpTo<>{inclusive = true}"
    }

    Box(modifier = Modifier.fillMaxSize()) {//fillMaxSize() --> Box pure screen ko cover krega
        Image(
            painter = painterResource(R.drawable.app_icon2), contentDescription = null,
            modifier = Modifier
                .size(140.dp)
                .align(Alignment.Center)  // logo ko center pe laaega
        ) //ImageComposable -->  Logo ko screen pe dikhaane ke liye

        Column(modifier = Modifier.align(Alignment.BottomCenter).padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally) {

            Text(text = "From", fontSize = 18.sp, fontWeight = FontWeight.Bold)

            Row {
                Image(
                    painter = painterResource(R.drawable.nitish_creations),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Text(text = "NC" , fontSize = 18.sp , fontWeight = FontWeight.Bold)
            }

        }
    }
}