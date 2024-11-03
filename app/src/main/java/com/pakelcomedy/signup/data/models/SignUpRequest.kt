package com.pakelcomedy.signup.data.models

data class SignUpRequest(
    val uid: String,                 // Unique identifier for the user
    val nama_lengkap: String,        // Full name of the user
    val nama_pengguna: String,       // Username chosen by the user
    val email: String,               // User's email address
    val password: String? = null,    // Password is now nullable
    val profile_pic: String? = null, // Profile picture, nullable
    val role: UserRole,              // User's role
    val kredensial: String? = null   // Additional credentials, nullable
)
