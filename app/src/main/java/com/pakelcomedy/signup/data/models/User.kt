package com.pakelcomedy.signup.data.models

data class User(
    val uid: String,
    val namaPengguna: String,
    val password: String, // Ensure this is hashed before storage
    val email: String,
    val profilePic: ByteArray?, // For the profile picture, it's stored as a medium blob
    val role: UserRole,
    val kredensial: String? = null, // Optional field for credentials
    val namaLengkap: String
)

enum class UserRole {
    admin,
    penulis,
    pembaca,
    peninjau
}
