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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.*
import com.example.baitapdidongcuoiki.ui.DailyMarketViewModel
import com.example.baitapdidongcuoiki.ui.screen.home.*
import com.example.baitapdidongcuoiki.ui.screen.notification.NotificationScreen
import com.example.baitapdidongcuoiki.ui.screen.report.ReportScreen
import com.example.baitapdidongcuoiki.ui.screen.tax.TaxScreen
import com.example.baitapdidongcuoiki.ui.screen.wallet.WalletScreen
import com.example.baitapdidongcuoiki.ui.screen.login.LoginScreen
import com.example.baitapdidongcuoiki.ui.screen.login.LoginViewModel
import com.example.baitapdidongcuoiki.ui.screen.register.RegisterScreen
import com.example.baitapdidongcuoiki.ui.screen.profile.ProfileScreen
import com.example.baitapdidongcuoiki.ui.theme.BaitapdidongcuoikiTheme
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var auth: FirebaseAuth

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val allGranted = permissions.all { it.value }
        if (allGranted) {
            Toast.makeText(this, "✅ Đã cấp quyền", Toast.LENGTH_SHORT).show()
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
                val startDest = remember { if (auth.currentUser != null) "home" else "login" }

                Scaffold(
                    containerColor = Color(0xFFFAF9F6),
                    floatingActionButton = {
                        if (currentRoute == "home") {
                            FloatingActionButton(
                                onClick = { navController.navigate("add_transaction") },
                                containerColor = MaterialTheme.colorScheme.primary
                            ) { Icon(Icons.Default.Add, null) }
                        }
                    },
                    bottomBar = {
                        if (!isAuthScreen) {
                            BottomNavigationBar(navController, currentRoute)
                        }
                    }
                ) { padding ->
                    NavHost(
                        navController = navController,
                        startDestination = startDest,
                        modifier = Modifier.padding(if (isAuthScreen) PaddingValues(0.dp) else padding)
                    ) {
                        composable("login") {
                            LoginScreen(viewModel = hiltViewModel(), navController = navController)
                        }
                        composable("register") {
                            RegisterScreen(navController = navController)
                        }
                        composable("home") {
                            val viewModel: HomeViewModel = hiltViewModel()
                            LaunchedEffect(Unit) { viewModel.syncFromCloud() }
                            HomeScreen(viewModel = viewModel, navController = navController)
                        }
                        composable("profile") { ProfileScreen(navController = navController) }
                        composable("notification") { NotificationScreen() }
                        composable("add_transaction") {
                            AddTransactionScreen(navController = navController, viewModel = hiltViewModel())
                        }
                        composable("wallet") {
                            val viewModel: HomeViewModel = hiltViewModel()
                            val state by viewModel.state.collectAsState()
                            WalletScreen(transactions = state.transactions, navController = navController)
                        }
                        composable("tax") { TaxScreen() }
                        composable("report") {
                            ReportScreen(homeViewModel = hiltViewModel(), marketViewModel = hiltViewModel())
                        }
                    }

                    if (marketUi.showDialog) {
                        MarketAlertDialog(marketUi, dailyMarketVm)
                    }
                }
            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: androidx.navigation.NavHostController, currentRoute: String?) {
    NavigationBar(containerColor = Color.White, tonalElevation = 8.dp, modifier = Modifier.height(85.dp)) {
        val items = listOf("wallet", "report", "home", "tax", "notification")
        items.forEach { screen ->
            val isSelected = currentRoute == screen
            val isHome = screen == "home"
            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    if (currentRoute != screen) {
                        navController.navigate(screen) {
                            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
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
                            else -> Icons.Default.Notifications
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
                }
            )
        }
    }
}

@Composable
fun MarketAlertDialog(marketUi: com.example.baitapdidongcuoiki.ui.DailyMarketUiState, dailyMarketVm: DailyMarketViewModel) {
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
            TextButton(onClick = { dailyMarketVm.dismissDialog() }) { Text("Đã xem") }
        }
    )
}