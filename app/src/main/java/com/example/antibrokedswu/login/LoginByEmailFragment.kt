package com.example.antibrokedswu.login

import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.antibrokedswu.LoginHomeFragment
import com.example.antibrokedswu.R
import com.example.antibrokedswu.database.DBHelper
import com.example.antibrokedswu.databinding.FragmentLoginByEmailBinding

class LoginByEmailFragment : Fragment(R.layout.fragment_login_by_email) {
    // 1. DBHelper 인스턴스 선언
    private lateinit var dbHelper: DBHelper

    private var _binding: FragmentLoginByEmailBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentLoginByEmailBinding.bind(view)

        // 2. DBHelper 초기화
        dbHelper = DBHelper(requireContext()) // Fragment의 Context 사용




        binding.btnBack.setOnClickListener { // 이전 화면으로 돌아가기
            parentFragmentManager.beginTransaction()
                .replace(R.id.container, LoginHomeFragment())
                .addToBackStack(null)
                .commit()
        }



        binding.btnOK.setOnClickListener {
            val email = binding.ownEmail.text.toString().trim()

            // 3. 이메일 유효성 검사
            if (email.isEmpty()) {
                Toast.makeText(requireContext(), "이메일을 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(requireContext(), "유효한 이메일 형식이 아닙니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 4. DB에서 이메일 존재 여부 확인
            // getUserByEmail을 사용하여 사용자가 존재하는지 확인합니다.
            val user = dbHelper.getUserByEmail(email)
            val isUserExist = user != null

            // 5. 다음 Fragment로 전달할 Bundle 생성
            val bundle = Bundle().apply {
                putString("email", email)
                // 사용자가 존재하면 '로그인', 존재하지 않으면 '회원가입' 모드임을 알립니다.
                putBoolean("isUserExist", isUserExist)
            }

            // 6. LoginPasswordFragment로 전환, Fragment인스턴스를 생성
            val nextFragment = LoginPasswordFragment()
            nextFragment.arguments = bundle // 번들을 인스턴스에 할당

            parentFragmentManager.beginTransaction()
//              .replace(R.id.container, LoginPasswordFragment()) 가정한 이전 Fragment -> 두번째 인스턴스 생성으로 인하여 오류!!
//                //이 인스턴스를 화면에 표시하는데 argument를 할당 받은적이 없어 argument==null이 되었음.
//                //따라서 null 또는 이메일 값을 찾지 못하게 됨.
                .replace(R.id.container, nextFragment)
                .addToBackStack(null)
                .commit()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}