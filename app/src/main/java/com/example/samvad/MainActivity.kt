package com.example.samvad

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.samvad.presentation.callscreen.CallScreen
import com.example.samvad.presentation.homescreen.HomeScreen
import com.example.samvad.presentation.navigation.WhatsAppNavigationSystem
import com.example.samvad.presentation.splashscreen.SplashScreen
import com.example.samvad.presentation.updatescreen.TopBar
import com.example.samvad.presentation.updatescreen.UpdateScreen
import com.example.samvad.presentation.userregistrationscreen.UserRegistrationScreen
import com.example.samvad.presentation.welcomescreen.WelcomeScreen
import com.example.samvad.ui.theme.SamvadTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SamvadTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    WhatsAppNavigationSystem()
                }
            }
        }
    }
}

