package com.example.antibrokedswu

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.security.MessageDigest

// 데이터베이스 관련 상수 정의
private const val DATABASE_NAME = "users.db"
private const val DATABASE_VERSION = 1
private const val TABLE_NAME = "users"
private const val COLUMN_ID = "id"
private const val COLUMN_USERNAME = "username"
private const val COLUMN_PASSWORD_HASH = "password_hash"

class DBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = """
            CREATE TABLE $TABLE_NAME (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_USERNAME TEXT UNIQUE,
                $COLUMN_PASSWORD_HASH TEXT
            )
        """.trimIndent()
        db?.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    // 비밀번호 해시 함수 (안전을 위해 SHA-256 사용)
    private fun hashPassword(password: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(password.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }

    // 새 사용자 추가
    fun addUser(username: String, passwordRaw: String): Boolean {
        val db = this.writableDatabase
        val passwordHash = hashPassword(passwordRaw)
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
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT 1 FROM $TABLE_NAME WHERE $COLUMN_USERNAME = ?", arrayOf(username))
        val exists = cursor.moveToFirst()
        cursor.close()
        db.close()
        return exists
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