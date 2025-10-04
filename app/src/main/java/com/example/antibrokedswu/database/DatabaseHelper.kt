package com.example.antibrokedswu.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import java.security.MessageDigest

// 데이터베이스 관련 상수 정의
private const val DATABASE_NAME = "users.db" //DB 이름
private const val DATABASE_VERSION = 4
private const val TABLE_NAME = "users" //테이블 이름
private const val COLUMN_ID = "id" //PK 설정
//private const val COLUMN_USERNAME = "username" //사용자 이름 컬럼 설정
private const val COLUMN_PASSWORD_HASH = "password_hash" //암호화 된 비번 정의
private const val COLUMN_EMAIL = "email"

class DBHelper(context: Context) : SQLiteOpenHelper
    (context, DATABASE_NAME, null, DATABASE_VERSION) {

    // DB 초기 설정
    override fun onCreate(db: SQLiteDatabase?) { //DB 초기 설정 담당(속성 및 제약조건 등)
        Log.d("DB_LIFE", "onCreate 호출: 테이블 생성 시작") // ⭐ Log 추가
        val createTableQuery = """ 
            CREATE TABLE $TABLE_NAME ( 
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, 
                $COLUMN_EMAIL TEXT UNIQUE,
                $COLUMN_PASSWORD_HASH TEXT
            )
        """.trimIndent()
        db?.execSQL(createTableQuery)
        Log.d("DB_LIFE", "onCreate 호출: 테이블 생성 완료") // ⭐ Log 추가
    }

    //테이블 갱신 시 수정(삭제>재생성)
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        Log.w("DB_LIFE", "onUpgrade 호출: 버전 $oldVersion -> $newVersion")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    // 비밀번호 해시 함수 (안전을 위해 SHA-256 사용)
    private fun hashPassword(password: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(password.toByteArray()) //SHA-256으로 암호화
        return bytes.joinToString("") { "%02x".format(it) } //암호화된 값 16진수 저장
    }

    // 새 사용자 추가
    fun addUserWithEmail(passwordRaw: String, email: String): Boolean {
        // 1. 쓰기가능한 데이터베이스 연결을 얻습니다.
        val db = this.writableDatabase

        // 2. 비밀번호를 안전하게 SHA-256으로 해시합니다.
        val passwordHash = hashPassword(passwordRaw)

        // 3. ContentValues 객체를 사용하여 저장할 데이터를 준비합니다.
        val values = ContentValues().apply {
            put(COLUMN_EMAIL, email) // ✅ 이메일 값을 추가합니다.
            put(COLUMN_PASSWORD_HASH, passwordHash)
        }

        // ⭐ 삽입 전 로그 추가: Content Values 확인
        Log.d("DB_CHECK", "ContentValues: $values")
        var newRowId: Long = -1L


        // ⭐ 트랜잭션과 예외 처리 추가 (Log.e 사용)
        try {
            db.beginTransaction()
            newRowId = db.insert(TABLE_NAME, null, values)

            if (newRowId != -1L) {
                db.setTransactionSuccessful()
                 Log.d("DBHelper", "사용자 등록 성공: ID $newRowId, Email: $email")
            } else {
                // newRowId가 -1L인 경우 (주로 UNIQUE 제약 조건 위반)
                Log.e("DBHelper", "사용자 등록 실패: db.insert()가 -1L 반환. 이메일 중복 가능성.")
            }
        } catch (e: Exception) {
            Log.e("DBHelper", "사용자 등록 중 예외 발생: ${e.message}", e)
            // SQL 실행 중 다른 예외 발생 시 (예: 파일 시스템 문제 등)
            Log.e("DBHelper", "사용자 등록 중 예외 발생: ${e.message}", e)
        } finally {
            Log.d("DB_LIFE", "DB 연결 닫기 시도 (addUserWithEmail)")
            db.endTransaction()
            db.close() // 데이터베이스 연결 닫기
        }

        return newRowId != -1L
    }

    fun getUserByEmail(email: String): UsersDataModel? {
        val db = this.readableDatabase
        val cursor = db.rawQuery(
            "SELECT $COLUMN_ID, $COLUMN_PASSWORD_HASH, $COLUMN_EMAIL FROM $TABLE_NAME WHERE $COLUMN_EMAIL = ?",
            arrayOf(email)
        )

        var userData: UsersDataModel? = null
        if (cursor.moveToFirst()) {
            // 커서에서 데이터 추출 (컬럼 인덱스는 실제 구현에 맞게 조정 필요)
            val id = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ID)) // 👈 Long 타입으로 ID를 가져옵니다.
            val email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL))
            val passwordHash = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PASSWORD_HASH))
            userData = UsersDataModel(
                id,
                email,
                passwordHash
                )
        }
        cursor.close()
        db.close()
        return userData // 존재하면 UserData 객체, 없으면 null 반환
    }

    // 2. 이메일과 비밀번호로 로그인 인증을 확인하는 함수
    fun checkUserByEmail(email: String, passwordRaw: String): Boolean {
        val db = this.readableDatabase
        val passwordHash = hashPassword(passwordRaw) // 기존 hashPassword 함수 사용

        val cursor = db.rawQuery(
            "SELECT 1 FROM $TABLE_NAME WHERE $COLUMN_EMAIL = ? AND $COLUMN_PASSWORD_HASH = ?",
            arrayOf(email, passwordHash)
        )
        val matches = cursor.moveToFirst()
        cursor.close()
        db.close()
        return matches
    }
}