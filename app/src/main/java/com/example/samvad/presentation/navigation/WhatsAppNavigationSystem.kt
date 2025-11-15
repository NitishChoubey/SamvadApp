package com.example.samvad.presentation.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.samvad.presentation.callscreen.CallScreen
import com.example.samvad.presentation.chatscreen.ChatScreen
import com.example.samvad.presentation.communitiesscreen.CommunitiesScreen
import com.example.samvad.presentation.homescreen.HomeScreen
import com.example.samvad.presentation.profile.UserProfileSetScreen
import com.example.samvad.presentation.settingsscreen.SettingsScreen
import com.example.samvad.presentation.splashscreen.SplashScreen
import com.example.samvad.presentation.updatescreen.UpdateScreen
import com.example.samvad.presentation.userregistrationscreen.UserRegistrationScreen
import com.example.samvad.presentation.viewmodel.BaseViewModel
import com.example.samvad.presentation.welcomescreen.WelcomeScreen

@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun WhatsAppNavigationSystem() {
    //NavHostController is a subclass of NavController
    //val navController: NavHostController = rememberNavController()

    val navController = rememberNavController() //navController --> ye hme navigation me help krega

    //NavHost() --> NavHost jitna bhi composable Screen hai aur Routes hai wo define krta hai ...and konse routes se konse screen pe jana hai wo btata hai...
    NavHost(startDestination = Routes.SplashScreen, navController = navController){   //sbse phele konsa screen hona chahiye wo hai apna "startDestination"
        // navController  --> ye hme navigation krne me help krta hai

        //NavGraph --> NavGraph me  konse Routes se konse screen me jana hai ..hm wo define krte hai isme

       composable<Routes.SplashScreen>{
           SplashScreen(navController)
       }

        composable<Routes.WelcomeScreen> {
            WelcomeScreen(navController)
        }

        composable<Routes.UserRegistrationScreen> {
            UserRegistrationScreen(navController)
        }


        composable<Routes.HomeScreen> {
            val baseViewModel: BaseViewModel = hiltViewModel()
            HomeScreen(navController , baseViewModel)
        }

        composable<Routes.UpdateScreen> {
            UpdateScreen(navController)
        }
        composable<Routes.CommunitiesScreen> {
            CommunitiesScreen(navController)
        }

        composable<Routes.CallScreen>{
            CallScreen(navController)
        }

        composable<Routes.UserProfileSetScreen>{

            UserProfileSetScreen(navHostController = navController)
        }

        composable<Routes.SettingsScreen> {
            SettingsScreen(navController)
        }

        composable(
            route = Routes.ChatScreen.route,
            arguments = listOf(
                navArgument("phoneNumber") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val phoneNumber = backStackEntry.arguments?.getString("phoneNumber") ?: ""
            val baseViewModel: BaseViewModel = hiltViewModel()
            ChatScreen(
                navHostController = navController,
                phoneNumber = phoneNumber,
                baseViewModel = baseViewModel
            )
        }

    }
}