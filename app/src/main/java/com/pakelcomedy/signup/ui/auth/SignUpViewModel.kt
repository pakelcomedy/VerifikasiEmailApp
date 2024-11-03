package com.pakelcomedy.signup.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pakelcomedy.signup.data.models.SignUpRequest
import com.pakelcomedy.signup.data.models.UserRole
import com.pakelcomedy.signup.network.ApiResponse
import com.pakelcomedy.signup.network.ApiService
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpViewModel : ViewModel() {

    private val apiService = ApiService.create()

    private val _signUpResult = MutableLiveData<ApiResponse<String>?>()
    val signUpResult: LiveData<ApiResponse<String>?> get() = _signUpResult

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    fun validatePassword(newPassword: String, confirmPassword: String): Boolean {
        return when {
            newPassword.isEmpty() -> {
                _errorMessage.value = "Password cannot be empty"
                false
            }
            confirmPassword.isEmpty() -> {
                _errorMessage.value = "Please confirm your password"
                false
            }
            newPassword != confirmPassword -> {
                _errorMessage.value = "Passwords do not match"
                false
            }
            else -> true
        }
    }

    fun signUpUser(uid: String, username: String, email: String, fullName: String, password: String, role: UserRole) {
        val signUpRequest = SignUpRequest(
            uid = uid,
            nama_pengguna = username,
            email = email,
            nama_lengkap = fullName,
            password = password,
            role = role
        )

        viewModelScope.launch {
            apiService.createUser(signUpRequest).enqueue(object : Callback<ApiResponse<String>> {
                override fun onResponse(call: Call<ApiResponse<String>>, response: Response<ApiResponse<String>>) {
                    if (response.isSuccessful && response.body() != null) {
                        _signUpResult.value = response.body()
                    } else {
                        _errorMessage.value = "Sign up failed. Please try again."
                    }
                }

                override fun onFailure(call: Call<ApiResponse<String>>, t: Throwable) {
                    _errorMessage.value = "Network error: ${t.message}"
                }
            })
        }
    }

    // Reset signup result after processing, useful for SingleLiveEvent pattern
    fun resetSignUpResult() {
        _signUpResult.value = null
    }
}