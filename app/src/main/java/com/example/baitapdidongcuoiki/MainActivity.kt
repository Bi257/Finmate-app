package com.example.baitapdidongcuoiki

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.baitapdidongcuoiki.ui.DailyMarketViewModel
import com.example.baitapdidongcuoiki.ui.screen.home.*
import com.example.baitapdidongcuoiki.ui.screen.notification.NotificationScreen
import com.example.baitapdidongcuoiki.ui.screen.report.ReportScreen
import com.example.baitapdidongcuoiki.ui.screen.tax.TaxScreen
import com.example.baitapdidongcuoiki.ui.screen.wallet.WalletScreen
import com.example.baitapdidongcuoiki.ui.theme.BaitapdidongcuoikiTheme
import dagger.hilt.android.AndroidEntryPoint
import com.example.baitapdidongcuoiki.ui.screen.login.LoginScreen
import com.example.baitapdidongcuoiki.ui.screen.login.LoginViewModel
import com.example.baitapdidongcuoiki.ui.screen.register.RegisterScreen

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val allGranted = permissions.all { it.value }
        if (allGranted) {
            Toast.makeText(this, "✅ Đã cấp quyền SMS & Notification", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "⚠️ Cần quyền SMS để đọc biến động số dư", Toast.LENGTH_LONG).show()
        }
    }

    private fun checkAndRequestPermissions() {
        val permissions = mutableListOf(Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissions.add(Manifest.permission.POST_NOTIFICATIONS)
        }
        val needRequest = permissions.any {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }
        if (needRequest) requestPermissionLauncher.launch(permissions.toTypedArray())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkAndRequestPermissions()

        setContent {
            BaitapdidongcuoikiTheme {
                val dailyMarketVm: DailyMarketViewModel = hiltViewModel()
                val marketUi by dailyMarketVm.ui.collectAsStateWithLifecycle()

                LaunchedEffect(Unit) { dailyMarketVm.loadAndShowOnce() }

                val navController = rememberNavController()
                val currentBackStack by navController.currentBackStackEntryAsState()
                val currentRoute = currentBackStack?.destination?.route

                val isAuthScreen = currentRoute == "login" || currentRoute == "register"

                Scaffold(
                    containerColor = Color(0xFFFAF9F6),
                    floatingActionButton = {
                        if (currentRoute == "home") {
                            FloatingActionButton(
                                onClick = { navController.navigate("add_transaction") },
                                containerColor = Color(0xFFFFE4E1)
                            ) {
                                Icon(Icons.Default.Add, contentDescription = "Add")
                            }
                        }
                    },
                    bottomBar = {
                        if (!isAuthScreen) {
                            NavigationBar(
                                containerColor = Color.White,
                                tonalElevation = 8.dp,
                                modifier = Modifier.height(85.dp)
                            ) {
                                // 1. KHÔI PHỤC DANH SÁCH CŨ (CÓ THÔNG BÁO, BỎ CÁ NHÂN)
                                val items = listOf("wallet", "report", "home", "tax", "notification")

                                items.forEach { screen ->
                                    val isSelected = currentRoute == screen
                                    val isHome = screen == "home"

                                    NavigationBarItem(
                                        selected = isSelected,
                                        onClick = {
                                            if (currentRoute != screen) {
                                                navController.navigate(screen) {
                                                    popUpTo(navController.graph.findStartDestination().id) {
                                                        saveState = true
                                                    }
                                                    launchSingleTop = true
                                                    restoreState = true
                                                }
                                            }
                                        },
                                        icon = {
                                            Icon(
                                                imageVector = when(screen) {
                                                    "home" -> Icons.Default.Home
                                                    "wallet" -> Icons.Default.AccountBalanceWallet
                                                    "report" -> Icons.Default.Description
                                                    "tax" -> Icons.Default.Calculate
                                                    else -> Icons.Default.Notifications // Icon Thông báo
                                                },
                                                contentDescription = null,
                                                modifier = Modifier.size(if (isHome) 32.dp else 24.dp),
                                                tint = if (isSelected) Color(0xFF9C27B0) else Color.Gray
                                            )
                                        },
                                        label = {
                                            Text(
                                                text = when(screen) {
                                                    "home" -> "Trang chủ"
                                                    "wallet" -> "Ví"
                                                    "report" -> "Báo cáo"
                                                    "tax" -> "Thuế"
                                                    else -> "Thông báo"
                                                },
                                                fontSize = if (isHome) 12.sp else 10.sp,
                                                fontWeight = if (isHome) FontWeight.Bold else FontWeight.Normal,
                                                color = if (isSelected) Color(0xFF9C27B0) else Color.Gray
                                            )
                                        },
                                        colors = NavigationBarItemDefaults.colors(
                                            indicatorColor = if (isHome) Color(0xFFF3E5F5) else MaterialTheme.colorScheme.secondaryContainer
                                        )
                                    )
                                }
                            }
                        }
                    }
                ) { padding ->
                    NavHost(
                        navController = navController,
                        startDestination = "login",
                        modifier = Modifier.padding(if (isAuthScreen) PaddingValues(0.dp) else padding)
                    ) {
                        composable("login") {
                            val loginViewModel: LoginViewModel = hiltViewModel()
                            LoginScreen(viewModel = loginViewModel, navController = navController)
                        }
                        composable("register") {
                            RegisterScreen(navController = navController)
                        }
                        composable("home") {
                            // Khởi tạo lại ViewModel khi vào Home để làm mới dữ liệu theo tài khoản
                            val viewModel: HomeViewModel = hiltViewModel()
                            HomeScreen(viewModel = viewModel, navController = navController)
                        }
                        composable("notification") {
                            NotificationScreen()
                        }
                        composable("add_transaction") {
                            val homeBackStackEntry = remember(navController) { navController.getBackStackEntry("home") }
                            val viewModel: HomeViewModel = hiltViewModel(homeBackStackEntry)
                            AddTransactionScreen(navController = navController, viewModel = viewModel)
                        }
                        composable("wallet") {
                            val homeBackStackEntry = remember(navController) { navController.getBackStackEntry("home") }
                            val viewModel: HomeViewModel = hiltViewModel(homeBackStackEntry)
                            val state by viewModel.state.collectAsState()
                            WalletScreen(transactions = state.transactions, navController = navController)
                        }
                        composable("tax") { TaxScreen() }
                        composable("report") {
                            val homeBackStackEntry = remember(navController) { navController.getBackStackEntry("home") }
                            val homeViewModel: HomeViewModel = hiltViewModel(homeBackStackEntry)
                            val marketViewModel: DailyMarketViewModel = hiltViewModel()
                            ReportScreen(homeViewModel = homeViewModel, marketViewModel = marketViewModel)
                        }
                    }

                    if (marketUi.showDialog) {
                        AlertDialog(
                            onDismissRequest = { if (!marketUi.isLoading) dailyMarketVm.dismissDialog() },
                            title = { Text(marketUi.dialogTitle) },
                            text = {
                                if (marketUi.isLoading) {
                                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                                        CircularProgressIndicator(Modifier.size(48.dp))
                                    }
                                } else { Text(marketUi.message) }
                            },
                            confirmButton = {
                                TextButton(onClick = { dailyMarketVm.dismissDialog() }, enabled = !marketUi.isLoading) {
                                    Text("Đã xem")
                                }
                            },
                            dismissButton = {
                                TextButton(onClick = { dailyMarketVm.refreshMarketData() }, enabled = !marketUi.isLoading) {
                                    Text("Thử lại")
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}