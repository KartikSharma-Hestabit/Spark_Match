package com.hestabit.sparkmatch

import android.app.Application
import android.util.Log
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class SparkMatch : Application() {
    private val TAG = "SparkMatchApp"

    override fun onCreate() {
        super.onCreate()

        // Initialize Firebase first, before any other operations
        initializeFirebase()
    }

    private fun initializeFirebase() {
        try {
            // Initialize Firebase
            if (FirebaseApp.getApps(this).isEmpty()) {
                val app = FirebaseApp.initializeApp(this)
                Log.d(TAG, "Firebase initialized successfully: ${app?.name}")
            } else {
                Log.d(TAG, "Firebase was already initialized")
            }

            // Test if Firebase Auth can be instantiated
            val auth = FirebaseAuth.getInstance()
            if (true) {
                Log.d(TAG, "Firebase Auth initialized successfully")

                // Set up Firebase Auth listener for debugging
                auth.addAuthStateListener { firebaseAuth ->
                    if (firebaseAuth.currentUser != null) {
                        Log.d(TAG, "User is signed in: ${firebaseAuth.currentUser?.uid}")
                    } else {
                        Log.d(TAG, "No user is currently signed in")
                    }
                }
            } else {
                Log.e(TAG, "Firebase Auth instance is null after initialization")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to initialize Firebase", e)
        }
    }
}