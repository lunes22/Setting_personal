package com.example.antibrokedswu.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.security.MessageDigest

// 데이터베이스 관련 상수 정의
private const val DATABASE_NAME = "users.db" //DB 이름
private const val DATABASE_VERSION = 1
private const val TABLE_NAME = "users" //테이블 이름
private const val COLUMN_ID = "id" //PK 설정
private const val COLUMN_USERNAME = "username" //사용자 이름 컬럼 설정
private const val COLUMN_PASSWORD_HASH = "password_hash" //암호화 된 비번 정의

class DBHelper(context: Context) : SQLiteOpenHelper
    (context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase?) { //DB 초기 설정 담당(속성 및 제약조건 등)
        val createTableQuery = """ 
            CREATE TABLE $TABLE_NAME ( 
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, 
                $COLUMN_USERNAME TEXT UNIQUE,
                $COLUMN_PASSWORD_HASH TEXT
            )
        """.trimIndent()
        db?.execSQL(createTableQuery)
    }


    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) { //테이블 갱신 시 수정(삭제>재생성)
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    // 비밀번호 해시 함수 (안전을 위해 SHA-256 사용)
    private fun hashPassword(password: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(password.toByteArray()) //SHA-256으로 암호화
        return bytes.joinToString("") { "%02x".format(it) } //암호화된 값 16진수 저장
    }

    // 새 사용자 추가
    fun addUser(username: String, passwordRaw: String): Boolean {
        val db = this.writableDatabase //쓰기 작업할 수 있는 객체 생성
        val passwordHash = hashPassword(passwordRaw) //받은 비번 해시(위의 정의한 함수)
        val values = ContentValues().apply {
            put(COLUMN_USERNAME, username)
            put(COLUMN_PASSWORD_HASH, passwordHash)
        }
        val newRowId = db.insert(TABLE_NAME, null, values)
        db.close()
        return newRowId != -1L
    }

    // 사용자 이름으로 존재 여부 확인
    fun isUserExists(username: String): Boolean {
        val db = this.readableDatabase //읽기 전용으로 DB 열기
        val cursor = db.rawQuery("SELECT 1 FROM $TABLE_NAME WHERE $COLUMN_USERNAME = ?", arrayOf(username))
        val exists = cursor.moveToFirst()
        cursor.close()
        db.close()
        return exists //존재여부 반환 Bool 형
    }

    // 사용자 로그인 확인
    fun checkUser(username: String, passwordRaw: String): Boolean {
        val db = this.readableDatabase
        val passwordHash = hashPassword(passwordRaw)
        val cursor = db.rawQuery("SELECT 1 FROM $TABLE_NAME WHERE $COLUMN_USERNAME = ? AND $COLUMN_PASSWORD_HASH = ?", arrayOf(username, passwordHash))
        val matches = cursor.moveToFirst()
        cursor.close()
        db.close()
        return matches
    }
}