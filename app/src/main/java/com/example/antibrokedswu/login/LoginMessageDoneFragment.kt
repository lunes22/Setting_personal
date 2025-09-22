package com.example.antibrokedswu
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.antibrokedswu.databinding.FragmentLoginMessageDoneBinding


class LoginMessageDoneFragment :  Fragment(R.layout.fragment_login_message_done){
    private var _binding: FragmentLoginMessageDoneBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentLoginMessageDoneBinding.bind(view)

        binding.btnToHome.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.container, HomeFragment())
                .addToBackStack(null)
                .commit()
        }
    }
}