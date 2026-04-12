package com.example.baitapdidongcuoiki.data.repository

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) {
    /**
     * Hàm đăng ký tài khoản mới bằng Email và Mật khẩu
     * Sau khi chạy xong, tài khoản sẽ xuất hiện trong tab Authentication trên Firebase Console
     */
    suspend fun register(email: String, password: String): Result<Unit> {
        return try {
            // Lệnh này gửi thông tin lên Firebase
            firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            Result.success(Unit)
        } catch (e: Exception) {
            // Trả về lỗi nếu đăng ký thất bại (ví dụ: email đã tồn tại, mật khẩu quá yếu...)
            Result.failure(e)
        }
    }

    /**
     * Hàm đăng nhập
     */
    suspend fun login(email: String, password: String): Result<Unit> {
        return try {
            firebaseAuth.signInWithEmailAndPassword(email, password).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}