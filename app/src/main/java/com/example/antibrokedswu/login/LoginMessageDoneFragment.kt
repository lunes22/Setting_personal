package com.example.antibrokedswu.login

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.antibrokedswu.HomeFragment
import com.example.antibrokedswu.R
import com.example.antibrokedswu.database.DBHelper // DBHelper 추가
import com.example.antibrokedswu.databinding.FragmentLoginMessageDoneBinding


class LoginMessageDoneFragment :  Fragment(R.layout.fragment_login_message_done){
    private var _binding: FragmentLoginMessageDoneBinding? = null
    private val binding get() = _binding!!

    // DB 닫기를 위한 DBHelper 인스턴스 (필요 시점에만 사용)
    private var dbHelper: DBHelper? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentLoginMessageDoneBinding.bind(view)

        // DB 닫기를 위해 헬퍼 인스턴스를 저장 (Context가 유효할 때)
        dbHelper = DBHelper(requireContext())

        binding.btnToHome.setOnClickListener {
            // HomeFragment() 및 TabLayout 표시 로직은 그대로 유지
            parentFragmentManager.beginTransaction()
                .replace(R.id.container, HomeFragment()) // 가정한 HomeFragment
                .addToBackStack(null)
                .commit()
            // (activity as? MainActivity)?.findViewById<TabLayout>(R.id.tabLayout)?.visibility = View.VISIBLE
        }
    }

    // 🌟 Fragment가 완전히 파괴될 때 DB 연결을 닫습니다.
    override fun onDestroy() {
        super.onDestroy()
        dbHelper?.close() // DB 연결 해제
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
