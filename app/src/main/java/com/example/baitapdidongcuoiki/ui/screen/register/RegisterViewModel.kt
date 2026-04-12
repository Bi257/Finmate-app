package com.example.baitapdidongcuoiki.ui.screen.register

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val auth: FirebaseAuth // Hilt sẽ tự lấy FirebaseAuth từ FirebaseModule bạn đã tạo
) : ViewModel() {

    fun register(email: String, pass: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        if (email.isEmpty() || pass.isEmpty()) {
            onError("Vui lòng điền đầy đủ thông tin")
            return
        }

        auth.createUserWithEmailAndPassword(email, pass)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onSuccess()
                } else {
                    onError(task.exception?.message ?: "Đăng ký thất bại")
                }
            }
    }
}