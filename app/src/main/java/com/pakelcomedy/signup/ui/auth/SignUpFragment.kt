package com.pakelcomedy.signup.auth

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.pakelcomedy.signup.R
import com.pakelcomedy.signup.data.models.SignUpRequest
import com.pakelcomedy.signup.data.models.UserRole
import com.pakelcomedy.signup.databinding.FragmentSignUpBinding
import com.pakelcomedy.signup.ui.auth.popups.SignUpVerifikasiEmailDialog

class SignUpFragment : Fragment() {

    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!

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

        // Check email verification status and navigate accordingly
        auth = FirebaseAuth.getInstance()
        checkVerificationStatusAndNavigate()

        binding.btnConfirm.setOnClickListener {
            val username = binding.etUsername.text.toString()
            val email = binding.etEmail.text.toString()
            val fullName = binding.etFullName.text.toString()
            val role = UserRole.pembaca

            if (validateInput(username, email, fullName)) {
                createUser(email, username, fullName, role)
            } else {
                Toast.makeText(requireContext(), "Please fill in all fields correctly", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkVerificationStatusAndNavigate() {
        val prefs = requireContext().getSharedPreferences("SignUpPrefs", Context.MODE_PRIVATE)
        val emailVerificationStarted = prefs.getBoolean("emailVerificationStarted", false)

        if (emailVerificationStarted) {
            val user = auth.currentUser
            user?.reload()?.addOnCompleteListener { task ->
                if (task.isSuccessful && user.isEmailVerified) {
                    prefs.edit().putBoolean("isEmailVerified", true).apply()
                    findNavController().navigate(R.id.action_signUpFragment_to_signUpInputFragment)
                }
            }
        }
    }

    private fun validateInput(username: String, email: String, fullName: String): Boolean {
        return username.isNotEmpty() && email.isNotEmpty() && fullName.isNotEmpty()
    }

    private fun createUser(email: String, username: String, fullName: String, role: UserRole) {
        auth.createUserWithEmailAndPassword(email, "temporaryPassword")
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val firebaseUid = auth.currentUser?.uid
                    if (firebaseUid != null) {
                        val signUpRequest = SignUpRequest(
                            uid = firebaseUid,
                            nama_lengkap = fullName,
                            nama_pengguna = username,
                            email = email,
                            role = role
                        )

                        sendVerificationEmail()
                        findNavController().navigate(R.id.action_signUpFragment_to_signUpVerifikasiEmailDialog)
                    } else {
                        Toast.makeText(requireContext(), "Failed to retrieve UID", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(requireContext(), "Sign up failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun sendVerificationEmail() {
        val user = auth.currentUser
        user?.sendEmailVerification()?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(requireContext(), "Verification email sent to ${user.email}", Toast.LENGTH_SHORT).show()
                // Mark that email verification has started
                saveEmailVerificationStarted(true)
            } else {
                Toast.makeText(requireContext(), "Failed to send verification email.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveEmailVerificationStarted(started: Boolean) {
        requireContext().getSharedPreferences("SignUpPrefs", Context.MODE_PRIVATE)
            .edit().putBoolean("emailVerificationStarted", started).apply()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}