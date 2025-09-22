package com.example.antibrokedswu

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.antibrokedswu.databinding.FragmentLoginMessageBinding


class LoginMessageFragment :  Fragment(R.layout.fragment_login_message){
    private var _binding: FragmentLoginMessageBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentLoginMessageBinding.bind(view)

        binding.btnBack.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.container, LoginPasswordFragment())
                .addToBackStack(null)
                .commit()
        }

//        binding.btnYes.setOnClickListener {
//            parentFragmentManager.beginTransaction()
//                .replace(R.id.container, LoginPasswordFragment()) //**고정지출액 페이지로 연결
//                .addToBackStack(null)
//                .commit()
//        }

        binding.btnNo.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.container, LoginMessageDoneFragment())
                .addToBackStack(null)
                .commit()
        }
    }
}