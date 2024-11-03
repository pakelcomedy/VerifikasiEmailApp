package com.pakelcomedy.signup.network

import com.pakelcomedy.signup.data.models.SignUpRequest
import com.pakelcomedy.signup.data.models.User
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

// Define the API response wrapper class
data class ApiResponse<T>(
    val success: Boolean,
    val data: T?,
    val message: String?
)

// API service interface
interface ApiService {

    @POST("signup_api.php")
    fun createUser(@Body signUpRequest: SignUpRequest): Call<ApiResponse<String>>

    @GET("signup_api.php")
    fun getUser(@Query("uid") uid: String): Call<ApiResponse<User>>

    @PUT("signup_api.php")
    fun updateUser(@Body user: User): Call<ApiResponse<String>>

    @DELETE("signup_api.php")
    fun deleteUser(@Query("uid") uid: String): Call<ApiResponse<String>>

    companion object {
        private const val BASE_URL = "https://9d7b-2001-448a-5122-6860-880c-eae2-4ee0-ec45.ngrok-free.app/TestSignUp/" // Update to your API URL

        fun create(): ApiService {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(ApiService::class.java)
        }
    }
}
