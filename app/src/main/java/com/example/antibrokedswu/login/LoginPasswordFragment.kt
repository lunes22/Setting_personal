package com.example.antibrokedswu

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.antibrokedswu.databinding.FragmentLoginPasswordBinding

class LoginPasswordFragment : Fragment(R.layout.fragment_login_password) {
    private var _binding: FragmentLoginPasswordBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentLoginPasswordBinding.bind(view)

        binding.btnBack.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.container, LoginByEmailFragment())
                .addToBackStack(null)
                .commit()
        }

        binding.btnOK.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.container, LoginMessageFragment())
                .addToBackStack(null)
                .commit()
        }
    }
}
