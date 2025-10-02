package com.example.antibrokedswu

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.antibrokedswu.databinding.FragmentLoginByEmailBinding

class LoginByEmailFragment : Fragment(R.layout.fragment_login_by_email) {
    //private lateinit var dbHelper: DBHelper
    //private lateinit var idEditText: EditText

    private var _binding: FragmentLoginByEmailBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentLoginByEmailBinding.bind(view)

        binding.btnBack.setOnClickListener { //이전 화면으로 돌아가기
            parentFragmentManager.beginTransaction()
                .replace(R.id.container, LoginHomeFragment())
                .addToBackStack(null)
                .commit()
        }

        //이메일 입력

        binding.btnOK.setOnClickListener { // 다음 화면으로 넘어가기
            parentFragmentManager.beginTransaction()
                .replace(R.id.container, LoginPasswordFragment())
                .addToBackStack(null)
                .commit()
        }
    }

    

}