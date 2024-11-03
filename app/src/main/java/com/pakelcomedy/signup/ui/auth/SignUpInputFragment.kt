package com.pakelcomedy.signup.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.pakelcomedy.signup.R
import com.pakelcomedy.signup.data.models.SignUpRequest
import com.pakelcomedy.signup.data.models.UserRole
import com.pakelcomedy.signup.databinding.FragmentSignUpInputBinding
import com.pakelcomedy.signup.network.ApiService
import com.pakelcomedy.signup.network.ApiResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpInputFragment : Fragment() {

    private var _binding: FragmentSignUpInputBinding? = null
    private val binding get() = _binding!!

    // Initialize the API service
    private val apiService by lazy { ApiService.create() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpInputBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSavePassword.setOnClickListener {
            val newPassword = binding.etNewPassword.text.toString()
            val confirmPassword = binding.etConfirmPassword.text.toString()

            if (validatePassword(newPassword, confirmPassword)) {
                // Retrieve user info from arguments or shared ViewModel
                val uid = arguments?.getString("uid") ?: ""
                val username = arguments?.getString("username") ?: ""
                val email = arguments?.getString("email") ?: ""
                val fullName = arguments?.getString("fullName") ?: ""
                val role = UserRole.pembaca // Adjust the role as needed

                val signUpRequest = SignUpRequest(
                    uid = uid,
                    nama_pengguna = username,
                    email = email,
                    nama_lengkap = fullName,
                    password = newPassword,
                    role = role
                )

                signUpUser(signUpRequest)
            }
        }
    }

    private fun validatePassword(newPassword: String, confirmPassword: String): Boolean {
        return when {
            newPassword.isEmpty() -> {
                binding.etNewPassword.error = "Password cannot be empty"
                false
            }
            confirmPassword.isEmpty() -> {
                binding.etConfirmPassword.error = "Please confirm your password"
                false
            }
            newPassword != confirmPassword -> {
                binding.etConfirmPassword.error = "Passwords do not match"
                false
            }
            else -> true
        }
    }

    private fun signUpUser(signUpRequest: SignUpRequest) {
        apiService.createUser(signUpRequest).enqueue(object : Callback<ApiResponse<String>> {
            override fun onResponse(call: Call<ApiResponse<String>>, response: Response<ApiResponse<String>>) {
                if (response.isSuccessful && response.body() != null) {
                    // Navigate to the home screen on successful sign-up
                    findNavController().navigate(R.id.action_signUpInputFragment_to_home)
                } else {
                    Toast.makeText(requireContext(), "Sign up failed. Please try again.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ApiResponse<String>>, t: Throwable) {
                Toast.makeText(requireContext(), "Network error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Clean up binding reference to avoid memory leaks
    }
}
