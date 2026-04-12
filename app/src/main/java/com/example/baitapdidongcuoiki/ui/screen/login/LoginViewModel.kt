package com.example.baitapdidongcuoiki.ui.screen.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor() : ViewModel() {

    // Sử dụng 'by' để truy cập trực tiếp giá trị biến thay vì .value
    var username by mutableStateOf("")
    var password by mutableStateOf("")
    var loginStatus by mutableStateOf("")

    fun onLoginClick() {
        if (username.isBlank() || password.isBlank()) {
            loginStatus = "Vui lòng nhập đầy đủ tài khoản và mật khẩu!"
        } else {
            // Giả lập logic đăng nhập
            if (username == "admin" && password == "123") {
                loginStatus = "Đăng nhập thành công!"
            } else {
                loginStatus = "Sai tài khoản hoặc mật khẩu!"
            }
        }
    }
}