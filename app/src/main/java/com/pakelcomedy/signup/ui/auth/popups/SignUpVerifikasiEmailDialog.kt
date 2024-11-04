package com.pakelcomedy.signup.ui.auth.popups

import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.pakelcomedy.signup.R
import com.pakelcomedy.signup.databinding.DialogSignUpVerifikasiEmailBinding

class SignUpVerifikasiEmailDialog : BottomSheetDialogFragment() {

    private var _binding: DialogSignUpVerifikasiEmailBinding? = null
    private val binding get() = _binding!!

    private val timerDuration: Long = 120000 // 2 minutes in milliseconds
    private lateinit var countDownTimer: CountDownTimer

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogSignUpVerifikasiEmailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Start countdown timer
        startCountDownTimer()

        binding.tvResend.setOnClickListener {
            resendVerificationEmail()
        }

        // Start verification status check
        checkEmailVerification()
    }

    private fun startCountDownTimer() {
        countDownTimer = object : CountDownTimer(timerDuration, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val secondsRemaining = (millisUntilFinished / 1000).toInt()
                binding.tvTimer.text = String.format("%02d:%02d", secondsRemaining / 60, secondsRemaining % 60)
            }

            override fun onFinish() {
                binding.tvTimer.text = "00:00"
                binding.tvResend.visibility = View.VISIBLE // Show resend button when timer finishes
            }
        }.start()
    }

    private fun checkEmailVerification() {
        val user = FirebaseAuth.getInstance().currentUser
        user?.reload()?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                if (user.isEmailVerified) {
                    // Update the verification status in SharedPreferences
                    requireContext().getSharedPreferences("SignUpPrefs", Context.MODE_PRIVATE)
                        .edit().putBoolean("isEmailVerified", true).apply()

                    onEmailVerified() // Navigate to SignUpInputFragment
                } else {
                    // Re-run the verification check after a short delay
                    view?.postDelayed({ checkEmailVerification() }, 5000)
                }
            } else {
                Toast.makeText(requireContext(), "Failed to check email verification status.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun resendVerificationEmail() {
        val user = FirebaseAuth.getInstance().currentUser
        user?.sendEmailVerification()?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(requireContext(), "Verification email sent to ${user.email}", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Failed to resend verification email.", Toast.LENGTH_SHORT).show()
            }
        }

        // Reset timer and hide resend button
        countDownTimer.cancel()
        startCountDownTimer()
        binding.tvResend.visibility = View.GONE
    }

    private fun onEmailVerified() {
        // Set a flag for verified status
        requireContext().getSharedPreferences("SignUpPrefs", Context.MODE_PRIVATE)
            .edit().putBoolean("isEmailVerified", true).apply()

        findNavController().navigate(R.id.action_signUpVerifikasiEmailDialog_to_signUpInputFragment)
        dismiss()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Clean up binding reference to avoid memory leaks
    }

    override fun onDestroy() {
        super.onDestroy()
        countDownTimer.cancel() // Cancel the timer to avoid leaks
    }
}