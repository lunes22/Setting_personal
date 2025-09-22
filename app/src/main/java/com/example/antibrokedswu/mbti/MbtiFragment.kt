package com.example.antibrokedswu

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment

class MbtiFragment : Fragment(R.layout.fragment_mbti) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val nextButton: Button = view.findViewById(R.id.btn_next)

        nextButton.setOnClickListener {
            val intent = Intent(requireContext(), MyConsumeMbtiActivity::class.java)
            startActivity(intent)
        }
    }
}