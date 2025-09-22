package com.example.antibrokedswu

import android.widget.EditText
import androidx.fragment.app.Fragment
import com.example.antibrokedswu.database.DBHelper

class LoginByEmailFragment :  Fragment(R.layout.fragment_login_by_email){
    private lateinit var dbHelper: DBHelper
    private lateinit var idEditText: EditText

}