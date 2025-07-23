package com.example.samvad.presentation.navigation

import kotlinx.serialization.Serializable

sealed class Routes {

    @Serializable  // used to convert kotlin objects into JSON format . Serialization is the process of converting data used by an application to a format that can be transferred over a network or stored in a database or a file.
    data object SplashScreen : Routes()  //data object tumhe ek clean, safe, aur structured way deta hai to manage screens and routes in your app.

    @Serializable
    data object WelcomeScreen : Routes()

    @Serializable
    data object UserRegistrationScreen : Routes()

    @Serializable
    data object HomeScreen : Routes()

    @Serializable
    data object UpdateScreen : Routes()

    @Serializable
    data object  CommunitiesScreen : Routes()

    @Serializable
    data object CallScreen : Routes()

    @Serializable
    data object UserProfileSetScreen
}