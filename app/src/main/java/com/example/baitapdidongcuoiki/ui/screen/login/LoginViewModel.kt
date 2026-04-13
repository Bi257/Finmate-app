package com.example.baitapdidongcuoiki.ui.screen.login

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
class LoginViewModel @Inject constructor(

    private val authRepository: AuthRepository
) : ViewModel() {

    var username by mutableStateOf("")
    var password by mutableStateOf("")
    var loginStatus by mutableStateOf("")

    fun onLoginClick() {
        if (username.isBlank() || password.isBlank()) {
            loginStatus = "Vui lòng nhập đầy đủ tài khoản và mật khẩu!"
            return
        }

        // 2. Sử dụng viewModelScope để chạy hàm suspend (bất đồng bộ)
        viewModelScope.launch {
            loginStatus = "Đang kiểm tra thông tin..."

            // 3. Gọi hàm login thật từ AuthRepository
            val result = authRepository.login(username, password)

            if (result.isSuccess) {
                loginStatus = "Đăng nhập thành công!"
            } else {
                // Hiển thị lỗi thật từ Firebase (ví dụ: User not found hoặc Wrong password)
                val message = result.exceptionOrNull()?.message ?: "Sai tài khoản hoặc mật khẩu!"
                loginStatus = "Lỗi: $message"
            }
        }
    }
}

















































