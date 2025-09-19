package com.example.antibrokedswu

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import kotlin.jvm.java

class SurveyActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_survey)

        val nextButton: Button = findViewById(R.id.btn_next)

        nextButton.setOnClickListener {
            val intent = Intent(this, MyConsumeMbtiActivity::class.java)

            startActivity(intent)
        }
    }
}