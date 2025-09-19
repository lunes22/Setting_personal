package com.example.antibrokedswu

data class UsersDataModel(
    val id: Long,
    val username: String,
    val passwordHash: String // 보안을 위해 비밀번호는 해시 값으로 저장합니다.
)
