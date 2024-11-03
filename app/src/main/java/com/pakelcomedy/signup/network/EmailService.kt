package com.pakelcomedy.signup.network

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

// Define a data class for the email request payload
data class EmailRequest(
    val email: String,
    val subject: String,
    val message: String
)

// Define the API service interface
interface EmailApiService {
    @POST("send-verification-email") // Update with your actual endpoint
    fun sendVerificationEmail(@Body emailRequest: EmailRequest): Call<Void> // Adjust the response type as needed
}

// Implement the EmailService class
class EmailService(private val emailApiService: EmailApiService) {

    // Method to send a verification email
    fun sendVerificationEmail(email: String) {
        val subject = "Email Verification"
        val message = "Please click the link to verify your email."

        val emailRequest = EmailRequest(email, subject, message)
        emailApiService.sendVerificationEmail(emailRequest).enqueue(object : retrofit2.Callback<Void> {
            override fun onResponse(call: Call<Void>, response: retrofit2.Response<Void>) {
                if (response.isSuccessful) {
                    // Handle success, e.g., show a toast or log
                    println("Verification email sent successfully to $email")
                } else {
                    // Handle failure, e.g., log the error
                    println("Failed to send verification email: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                // Handle failure
                println("Error sending email: ${t.message}")
            }
        })
    }
}