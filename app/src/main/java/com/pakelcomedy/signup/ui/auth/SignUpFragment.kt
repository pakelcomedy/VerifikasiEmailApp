package com.pakelcomedy.signup.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.pakelcomedy.signup.R
import com.pakelcomedy.signup.databinding.FragmentSignUpBinding

class SignUpFragment : Fragment() {

    // Declare a nullable binding variable
    private var _binding: FragmentSignUpBinding? = null
    // Create a non-nullable property for binding
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment using View Binding
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set a click listener on the confirm button
        binding.btnConfirm.setOnClickListener {
            // Navigate to the SignUpInputFragment on button click
            findNavController().navigate(R.id.action_signUpFragment_to_signUpVerifikasiEmailDialog)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Clean up binding reference to avoid memory leaks
        _binding = null
    }
}