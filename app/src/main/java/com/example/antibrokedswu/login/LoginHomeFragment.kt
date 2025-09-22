package com.example.antibrokedswu

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.antibrokedswu.databinding.FragmentLoginHomeBinding


class LoginHomeFragment :  Fragment(R.layout.fragment_login_home){
    private var _binding: FragmentLoginHomeBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentLoginHomeBinding.bind(view)

        binding.btnLogin.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.container, LoginByEmailFragment())
                .addToBackStack(null)
                .commit()
        }

        binding.btnGoogle.setOnClickListener { //현재는 이메일 로그인으로 연결, 추후 기능 수정
            parentFragmentManager.beginTransaction()
                .replace(R.id.container, LoginByEmailFragment())
                .addToBackStack(null)
                .commit()
        }

        binding.btnKakao.setOnClickListener { //현재는 이메일 로그인으로 연결, 추후 기능 수정
            parentFragmentManager.beginTransaction()
                .replace(R.id.container, LoginByEmailFragment())
                .addToBackStack(null)
                .commit()
        }

        binding.btnNaver.setOnClickListener { //현재는 이메일 로그인으로 연결, 추후 기능 수정
            parentFragmentManager.beginTransaction()
                .replace(R.id.container, LoginByEmailFragment())
                .addToBackStack(null)
                .commit()
        }
    }
}