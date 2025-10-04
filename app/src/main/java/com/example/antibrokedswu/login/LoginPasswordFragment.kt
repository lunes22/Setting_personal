package com.example.antibrokedswu.login

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.antibrokedswu.LoginMessageFragment
import com.example.antibrokedswu.R
import com.example.antibrokedswu.database.DBHelper
import com.example.antibrokedswu.databinding.FragmentLoginPasswordBinding

class LoginPasswordFragment : Fragment(R.layout.fragment_login_password) {
    private var _binding: FragmentLoginPasswordBinding? = null
    private val binding get() = _binding!!

    // DB 및 전달받은 데이터 저장
    private lateinit var dbHelper: DBHelper
    private var email: String = ""
    private var isUserExist: Boolean = false // 사용자 존재 여부 (true: 로그인, false: 회원가입)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentLoginPasswordBinding.bind(view)
        dbHelper = DBHelper(requireContext()) // DB 헬퍼 초기화

        val fragmentArguments = arguments // arguments 변수를 지역 변수에 할당하여 명확히 합니다.

        if (fragmentArguments != null) {
            // null 병합 연산자 (?:)를 사용하여 null 반환 시 빈 문자열("")로 대체
            email = fragmentArguments.getString("email") ?: ""
            isUserExist = fragmentArguments.getBoolean("isUserExist", false)
        }

        // ⭐ 추출된 이메일 값이 비어있는지 즉시 확인하는 로그 (디버깅용)
        if (email.isEmpty()) {
            Log.e("EMAIL_CHECK", "Bundle에서 이메일 값을 가져오는 데 최종 실패했습니다. arguments=${fragmentArguments != null}")
            // 사용자에게 메시지를 보여주거나 이전 화면으로 돌아가는 방어 코드를 추가하는 것이 좋습니다.
            Toast.makeText(requireContext(), "이메일 정보가 유실되어 이전 화면으로 돌아갑니다.", Toast.LENGTH_LONG).show()

            // 데이터 유실 시 이전 화면으로 복귀
            parentFragmentManager.beginTransaction()
                .replace(R.id.container, LoginByEmailFragment())
                .commit()
            return // onViewCreated 종료
        } else {
            Log.d("EMAIL_CHECK", "Bundle에서 이메일 값 확인 완료: $email")
        }

        // 1. LoginByEmailFragment에서 전달받은 데이터 추출
        arguments?.let {
            email = it.getString("email") ?: ""
            isUserExist = it.getBoolean("isUserExist", false)
        }

        // ⭐ 2. 추출된 이메일 값이 비어있는지 즉시 확인하는 로그를 추가합니다.
        if (email.isEmpty()) {
            Log.e("EMAIL_CHECK", "Bundle에서 이메일 값을 가져오는 데 실패했습니다.")
        } else {
            Log.d("EMAIL_CHECK", "Bundle에서 이메일 값 확인: $email")
        }

        // 2. 사용자 존재 여부에 따라 UI 동적 변경
        setupUI()

        binding.btnBack.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.container, LoginByEmailFragment())
                .addToBackStack(null)
                .commit()
        }

        // 3. 확인 버튼 로직 (로그인 또는 회원가입)
        binding.btnOK.setOnClickListener {
            val password = binding.inputPW.text.toString()

            if (password.isEmpty()) {
                Toast.makeText(requireContext(), "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (isUserExist) {
                // ✅ 3-1. 사용자 존재 -> 로그인 인증 시도
                handleLogin(email, password)
            } else {
                // ✅ 3-2. 사용자 미존재 -> 회원가입 시도
                val checkPassword = binding.checkPW.text.toString()
                handleRegister(email, password, checkPassword)
            }

            parentFragmentManager.beginTransaction()
                .replace(R.id.container, LoginMessageFragment())
                .addToBackStack(null)
                .commit()
        }
    }

    private fun setupUI() {
        // UI 요소 찾기 (XML의 visibility=gone 요소)
        // '비밀번호 확인' 제목 TextView (visibility: gone 였던 뷰)
        val tvPwCheckTitle = binding.checkInfo // 👈 XML에서 추가한 ID 사용
        // '다시 한 번 입력해주세요' EditText (visibility: gone 였던 뷰)
        val etPwCheck = binding.checkPW
        // '다시 만나서 반가워요!' 메시지 TextView
        val tvWelcomeMessage = binding.inputInfo // 👈 XML에서 추가한 ID 사용

        if (isUserExist) {
            // 로그인 모드: 비밀번호 확인 필드를 숨김
            tvPwCheckTitle.visibility = View.GONE
            etPwCheck.visibility = View.GONE

            // 환영 메시지를 '로그인' 모드에 맞게 설정
            tvWelcomeMessage.text = "다시 만나서 반가워요!"
            binding.btnOK.text = "로그인"

        } else {
            // 회원가입 모드: 비밀번호 확인 필드를 표시
            tvPwCheckTitle.visibility = View.VISIBLE
            etPwCheck.visibility = View.VISIBLE

            // 환영 메시지를 '회원가입' 모드에 맞게 설정
            tvWelcomeMessage.text = "새로운 비밀번호를 설정해주세요."
            binding.btnOK.text = "회원가입"
        }
    }

    private fun handleLogin(email: String, passwordRaw: String) {
        // DBHelper의 checkUserByEmail 함수를 사용하여 로그인 인증
        if (dbHelper.checkUserByEmail(email, passwordRaw)) {
            // 로그인 성공
            Toast.makeText(requireContext(), "${email}님 환영합니다!", Toast.LENGTH_SHORT).show()
            navigateToDoneFragment()
        } else {
            // 로그인 실패
            Toast.makeText(requireContext(), "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun handleRegister(email: String, passwordRaw: String, checkPassword: String) {
        if (passwordRaw != checkPassword) {
            Toast.makeText(requireContext(), "비밀번호 확인이 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
            return
        }
        if (passwordRaw.length < 6) {
            Toast.makeText(requireContext(), "비밀번호는 6자 이상이어야 합니다.", Toast.LENGTH_SHORT).show()
            return
        }

        // DBHelper의 addUserWithEmail 함수를 사용하여 새 사용자 등록
        if (dbHelper.addUserWithEmail(passwordRaw, email)) {
            // 회원가입 성공
            Toast.makeText(requireContext(), "회원가입 성공!", Toast.LENGTH_SHORT).show()
            navigateToDoneFragment()
        } else {
            // 회원가입 실패 (이론적으로는 중복 이메일 체크가 LoginByEmailFragment에서 됐으므로 발생하지 않아야 함)
            Toast.makeText(requireContext(), "회원가입에 실패했습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun navigateToDoneFragment() {
        // 다음 화면으로 넘어가기 (로그인/회원가입 성공 시)
        parentFragmentManager.beginTransaction()
            .replace(R.id.container, LoginMessageDoneFragment())
            .addToBackStack(null)
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}