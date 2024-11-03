package com.pakelcomedy.signup.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.pakelcomedy.signup.R
import com.pakelcomedy.signup.databinding.FragmentSignUpInputBinding

class SignUpInputFragment : Fragment() {

    private var _binding: FragmentSignUpInputBinding? = null
    private val binding get() = _binding!!

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
            // Handle saving the password logic
            val newPassword = binding.etNewPassword.text.toString()
            val confirmPassword = binding.etConfirmPassword.text.toString()

            if (newPassword == confirmPassword) {
                // Proceed with sign-up logic, e.g., API call to create an account
                // After successful sign-up, navigate to the next fragment or activity
            } else {
                // Show error message to the user
                binding.etConfirmPassword.error = "Passwords do not match"
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Clean up binding reference to avoid memory leaks
    }
}