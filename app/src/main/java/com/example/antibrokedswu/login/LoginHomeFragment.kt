package com.example.antibrokedswu

import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.antibrokedswu.databinding.FragmentLoginHomeBinding


class LoginHomeFragment : Fragment(R.layout.fragment_login_home) {
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
            showCustomToast(requireContext(), "현재 준비 중인 서비스 입니다 :)!")
//            parentFragmentManager.beginTransaction()
//                .replace(R.id.container, LoginByEmailFragment())
//                .addToBackStack(null)
//                .commit()
        }

        binding.btnKakao.setOnClickListener {
            showCustomToast(requireContext(), "현재 준비 중인 서비스 입니다 :)!")
////            parentFragmentManager.beginTransaction()
////                .replace(R.id.container, LoginByEmailFragment())
////                .addToBackStack(null)
////                .commit()
        }

        binding.btnNaver.setOnClickListener { //현재는 이메일 로그인으로 연결, 추후 기능 수정
            showCustomToast(requireContext(), "현재 준비 중인 서비스 입니다 :)!")
//            parentFragmentManager.beginTransaction()
//                .replace(R.id.container, LoginByEmailFragment())
//                .addToBackStack(null)
//                .commit()
        }
    }

    fun showCustomToast(context: Context, message: String) {
        val inflater = LayoutInflater.from(context)
        val layout: View = inflater.inflate(R.layout.custom_toast, null)

        val textView = layout.findViewById<TextView>(R.id.toast_text)
        textView.text = message

        val toast = Toast(context)

        toast.duration = Toast.LENGTH_SHORT

        toast.setGravity(Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL, 0, 100)

        toast.view = layout

        toast.show()
    }
}