package com.pakelcomedy.signup.auth

import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.pakelcomedy.signup.R
import com.pakelcomedy.signup.data.models.SignUpRequest
import com.pakelcomedy.signup.data.models.UserRole
import com.pakelcomedy.signup.network.ApiService
import com.pakelcomedy.signup.network.ApiResponse
import com.pakelcomedy.signup.databinding.FragmentSignUpBinding
import com.pakelcomedy.signup.ui.auth.popups.SignUpVerifikasiEmailDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.UUID

class SignUpFragment : Fragment() {

    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!

    private lateinit var apiService: ApiService
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        apiService = ApiService.create() // Initialize the API service
        auth = FirebaseAuth.getInstance() // Initialize Firebase Auth

        binding.btnConfirm.setOnClickListener {
            val username = binding.etUsername.text.toString()
            val email = binding.etEmail.text.toString()
            val fullName = binding.etFullName.text.toString()
            val role = UserRole.pembaca // Use the UserRole enum with proper capitalization

            if (validateInput(username, email, fullName)) {
                val uid = UUID.randomUUID().toString() // Generate a unique UID
                val signUpRequest = SignUpRequest(
                    uid = uid,
                    nama_lengkap = fullName,
                    nama_pengguna = username,
                    email = email,
                    role = role
                )

                // Create user in Firebase
                createUser(email, username)
            } else {
                Toast.makeText(requireContext(), "Please fill in all fields correctly", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun validateInput(username: String, email: String, fullName: String): Boolean {
        return username.isNotEmpty() && email.isNotEmpty() && fullName.isNotEmpty()
    }

    private fun createUser(email: String, username: String) {
        // Use FirebaseAuth to create a user
        auth.createUserWithEmailAndPassword(email, "temporaryPassword") // Use a temporary password
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // User created successfully
                    sendVerificationEmail()
                    // You can also navigate to another fragment here or update UI
                    findNavController().navigate(R.id.action_signUpFragment_to_signUpVerifikasiEmailDialog)
                } else {
                    Toast.makeText(requireContext(), "Sign up failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun sendVerificationEmail() {
        val user = FirebaseAuth.getInstance().currentUser
        user?.sendEmailVerification()
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(requireContext(), "Verification email sent to ${user.email}", Toast.LENGTH_SHORT).show()
                    // Show the verification dialog
                    val dialog = SignUpVerifikasiEmailDialog()
                    dialog.show(parentFragmentManager, "SignUpVerifikasiEmailDialog")
                } else {
                    Toast.makeText(requireContext(), "Failed to send verification email.", Toast.LENGTH_SHORT).show()
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Clean up binding reference to avoid memory leaks
    }
}