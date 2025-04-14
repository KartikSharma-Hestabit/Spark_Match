package com.hestabit.sparkmatch

import android.app.Application
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class SparkMatch : Application() {

    override fun onCreate() {
        super.onCreate()
        initializeFirebase()
    }

    private fun initializeFirebase() {
        try {
            val auth = FirebaseAuth.getInstance()
            auth.addAuthStateListener { firebaseAuth ->
                firebaseAuth.currentUser != null
            }
        } catch (e: Exception) {
            Log.e("Firebase Initialization", "Failed to initialize Firebase", e)
        }
    }
}