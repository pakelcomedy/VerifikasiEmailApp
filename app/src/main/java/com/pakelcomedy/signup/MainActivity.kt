package com.pakelcomedy.signup.ui

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.pakelcomedy.signup.R

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()

        // Check the verification status when the activity is created
        checkUserVerificationStatus()
    }

    private fun checkUserVerificationStatus() {
        val user = auth.currentUser
        val prefs = getSharedPreferences("SignUpPrefs", Context.MODE_PRIVATE)
        val isEmailVerified = prefs.getBoolean("isEmailVerified", false)

        if (user != null && isEmailVerified) {
            // User is logged in and email is verified, navigate to SignUpInputFragment
            findNavController(R.id.nav_host_fragment).navigate(R.id.signUpInputFragment)
        } else {
            // User is either not logged in or email is not verified, show SignUpFragment
            findNavController(R.id.nav_host_fragment).navigate(R.id.signUpFragment)
        }
    }
}
