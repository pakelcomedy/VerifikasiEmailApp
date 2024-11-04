package com.pakelcomedy.signup.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.pakelcomedy.signup.R

class HomeFragment : Fragment() {

    // Variabel binding didefinisikan tanpa tipe khusus binding (View Binding)
    private lateinit var binding: View

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate layout menggunakan View Binding standar
        binding = inflater.inflate(R.layout.home, container, false)
        return binding
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser

        if (currentUser != null) {
            // Mengatur TextView dan ImageView secara manual
            val tvFullName = view.findViewById<TextView>(R.id.tvFullName)
            val tvEmail = view.findViewById<TextView>(R.id.tvEmail)
            val ivProfilePicture = view.findViewById<ImageView>(R.id.ivProfilePicture)

            tvFullName.text = currentUser.displayName ?: "Full Name Unavailable"
            tvEmail.text = currentUser.email ?: "Email Unavailable"

            // Menggunakan Glide untuk memuat gambar profil
            currentUser.photoUrl?.let { uri ->
                Glide.with(this)
                    .load(uri)
                    .circleCrop() // Membuat gambar menjadi lingkaran
                    .into(ivProfilePicture)
            } ?: run {
                // Placeholder jika tidak ada foto
                ivProfilePicture.setImageResource(R.drawable.ic_launcher_foreground)
            }
        } else {
            // Handle jika pengguna tidak terautentikasi
            val tvFullName = view.findViewById<TextView>(R.id.tvFullName)
            val tvEmail = view.findViewById<TextView>(R.id.tvEmail)
            tvFullName.text = "No user data"
            tvEmail.text = "Please sign in"
        }
    }
}