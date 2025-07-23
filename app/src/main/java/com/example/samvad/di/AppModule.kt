package com.example.samvad.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    //ab hmlog 2 instance banaenge firebase ka isme ...dono instance ko pure project me access kr skte h

    @Provides
    @Singleton
    //ye function authentication ke liye hai
    fun provideFirebaseAuth(): FirebaseAuth {

        return FirebaseAuth.getInstance()
        // ye function authentication result ko return krne ke kaam me aaega
    }

    @Provides
    @Singleton
    //ye function database ke liye hai
    fun provideFirebaseDatabase():FirebaseDatabase{
        return FirebaseDatabase.getInstance()
        //ye function hme firebase ka database return krega

    }
}