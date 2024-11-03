package com.pakelcomedy.signup.ui.auth.popups

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
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

        // Initialize and start the countdown timer
        startCountDownTimer()

        binding.tvResend.setOnClickListener {
            // Handle resend email logic
            resendVerificationEmail()
        }
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

    private fun resendVerificationEmail() {
        // Logic to resend the verification email
        // For example, make a network request to send the email again

        // Reset timer after resending
        countDownTimer.cancel()
        startCountDownTimer()
        binding.tvResend.visibility = View.GONE // Hide resend button until the timer finishes again

        // Optional: After resending, you could also navigate to SignUpInputFragment if needed
        // findNavController().navigate(R.id.action_signUpVerifikasiEmailDialog_to_signUpInputFragment)
    }

    // Call this function to navigate after verifying the email
    private fun onEmailVerified() {
        // Navigate to SignUpInputFragment when email is verified
        findNavController().navigate(R.id.action_signUpVerifikasiEmailDialog_to_signUpInputFragment)
        dismiss() // Dismiss the dialog after navigating
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