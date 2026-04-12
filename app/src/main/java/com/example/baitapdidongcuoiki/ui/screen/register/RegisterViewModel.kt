package com.example.baitapdidongcuoiki.ui.screen.register

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.baitapdidongcuoiki.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var confirmPassword by mutableStateOf("")
    var registerStatus by mutableStateOf("") // Dùng để báo lỗi hoặc thành công

    fun onRegisterClick() {
        // Kiểm tra định dạng Email cơ bản
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            registerStatus = "Email không đúng định dạng (vd: ngan@gmail.com)"
            return
        }

        if (password.length < 6) {
            registerStatus = "Mật khẩu Firebase yêu cầu ít nhất 6 ký tự"
            return
        }

        if (password != confirmPassword) {
            registerStatus = "Mật khẩu xác nhận không khớp"
            return
        }

        viewModelScope.launch {
            registerStatus = "Đang kết nối Firebase..."
            val result = authRepository.register(email, password)
            if (result.isSuccess) {
                registerStatus = "Đăng ký thành công!"
                // Lúc này Ngân F5 trình duyệt trên máy tính sẽ thấy tài khoản
            } else {
                // Hiển thị mã lỗi cụ thể từ Firebase (vd: Email already exists)
                val errorMsg = result.exceptionOrNull()?.message ?: "Lỗi không xác định"
                registerStatus = "Lỗi: $errorMsg"
            }
        }
    }}