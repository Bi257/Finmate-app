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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import androidx.compose.runtime.collectAsState
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    // ==================== QUYỀN SMS & NOTIFICATION ====================
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val allGranted = permissions.all { it.value }
        if (allGranted) {
            Toast.makeText(this, "✅ Đã cấp quyền SMS & Notification", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(
                this,
                "⚠️ Cần quyền SMS để đọc biến động số dư và thông báo",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun checkAndRequestPermissions() {
        val permissions = mutableListOf(
            Manifest.permission.RECEIVE_SMS,
            Manifest.permission.READ_SMS
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissions.add(Manifest.permission.POST_NOTIFICATIONS)
        }

        val needRequest = permissions.any {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }

        if (needRequest) {
            requestPermissionLauncher.launch(permissions.toTypedArray())
        }
    }
    // ============================================================

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        checkAndRequestPermissions()

        setContent {
            BaitapdidongcuoikiTheme {
                val activity = LocalContext.current as ComponentActivity
                val dailyMarketVm: DailyMarketViewModel = hiltViewModel(activity)
                val marketUi by dailyMarketVm.ui.collectAsStateWithLifecycle()

                LaunchedEffect(Unit) {
                    dailyMarketVm.loadAndShowOnce()
                }

                val navController = rememberNavController()
                val currentBackStack by navController.currentBackStackEntryAsState()
                val currentRoute = currentBackStack?.destination?.route

                Box(modifier = Modifier.fillMaxSize()) {
                    Scaffold(
                        containerColor = MaterialTheme.colorScheme.background,
                        floatingActionButton = {
                            if (currentRoute == "home") {
                                // 🔥 NÚT THÊM GIAO DỊCH DẠNG EXTENDED (CÓ ICON + CHỮ)
                                ExtendedFloatingActionButton(
                                    onClick = { navController.navigate("add_transaction") },
                                    containerColor = Color(0xFFE91E63), // hồng đậm
                                    contentColor = Color.White,
                                    elevation = FloatingActionButtonDefaults.elevation(8.dp),
                                    shape = RoundedCornerShape(28.dp),
                                    modifier = Modifier.padding(end = 16.dp, bottom = 16.dp)
                                ) {
                                    Icon(Icons.Default.Add, contentDescription = null)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Thêm giao dịch", fontSize = 14.sp, fontWeight = FontWeight.Medium)
                                }
                            }
                        },
                        bottomBar = {
                            NavigationBar(
                                containerColor = Color.White,
                                tonalElevation = 0.dp,
                                modifier = Modifier
                                    .padding(horizontal = 12.dp, vertical = 8.dp)
                                    .shadow(8.dp, RoundedCornerShape(24.dp))
                                    .clip(RoundedCornerShape(24.dp))
                            ) {
                                fun navigate(route: String) {
                                    navController.navigate(route) {
                                        popUpTo("home") { saveState = true }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                                NavigationBarItem(
                                    selected = currentRoute == "home",
                                    onClick = { navigate("home") },
                                    icon = { Icon(Icons.Default.Home, null) },
                                    label = { Text("Trang chủ") },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = Color(0xFF9C27B0),
                                        selectedTextColor = Color(0xFF9C27B0),
                                        unselectedIconColor = Color(0xFFB39DDB),
                                        unselectedTextColor = Color(0xFFB39DDB)
                                    )
                                )
                                NavigationBarItem(
                                    selected = currentRoute == "wallet",
                                    onClick = { navigate("wallet") },
                                    icon = { Icon(Icons.Default.AccountBalanceWallet, null) },
                                    label = { Text("Ví") },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = Color(0xFF9C27B0),
                                        selectedTextColor = Color(0xFF9C27B0),
                                        unselectedIconColor = Color(0xFFB39DDB),
                                        unselectedTextColor = Color(0xFFB39DDB)
                                    )
                                )
                                NavigationBarItem(
                                    selected = currentRoute == "report",
                                    onClick = { navigate("report") },
                                    icon = { Icon(Icons.Default.Description, null) },
                                    label = { Text("Báo cáo") },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = Color(0xFF9C27B0),
                                        selectedTextColor = Color(0xFF9C27B0),
                                        unselectedIconColor = Color(0xFFB39DDB),
                                        unselectedTextColor = Color(0xFFB39DDB)
                                    )
                                )
                                NavigationBarItem(
                                    selected = currentRoute == "tax",
                                    onClick = { navigate("tax") },
                                    icon = { Icon(Icons.Default.Calculate, null) },
                                    label = { Text("Thuế") },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = Color(0xFF9C27B0),
                                        selectedTextColor = Color(0xFF9C27B0),
                                        unselectedIconColor = Color(0xFFB39DDB),
                                        unselectedTextColor = Color(0xFFB39DDB)
                                    )
                                )
                                NavigationBarItem(
                                    selected = currentRoute == "notification",
                                    onClick = { navigate("notification") },
                                    icon = { Icon(Icons.Default.Notifications, null) },
                                    label = { Text("Thông báo") },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = Color(0xFF9C27B0),
                                        selectedTextColor = Color(0xFF9C27B0),
                                        unselectedIconColor = Color(0xFFB39DDB),
                                        unselectedTextColor = Color(0xFFB39DDB)
                                    )
                                )
                            }
                        }
                    ) { padding ->
                        NavHost(
                            navController = navController,
                            startDestination = "home",
                            modifier = Modifier.padding(padding)
                        ) {
                            composable("home") {
                                val viewModel: HomeViewModel = hiltViewModel()
                                HomeScreen(viewModel = viewModel, navController = navController)
                            }

                            composable("add_transaction") {
                                val homeBackStackEntry = remember(navController) {
                                    navController.getBackStackEntry("home")
                                }
                                val viewModel: HomeViewModel = hiltViewModel(homeBackStackEntry)
                                AddTransactionScreen(navController = navController, viewModel = viewModel)
                            }

                            composable("wallet") {
                                val homeBackStackEntry = remember(navController) {
                                    navController.getBackStackEntry("home")
                                }
                                val viewModel: HomeViewModel = hiltViewModel(homeBackStackEntry)
                                val state by viewModel.state.collectAsState()
                                WalletScreen(transactions = state.transactions, navController = navController)
                            }

                            composable("tax") { TaxScreen() }

                            composable("report") {
                                val homeBackStackEntry = remember(navController) {
                                    navController.getBackStackEntry("home")
                                }
                                val homeViewModel: HomeViewModel = hiltViewModel(homeBackStackEntry)
                                ReportScreen(
                                    homeViewModel = homeViewModel,
                                    marketViewModel = dailyMarketVm
                                )
                            }

                            composable("notification") { NotificationScreen() }
                        }
                    }

                    if (marketUi.showDialog) {
                        AlertDialog(
                            onDismissRequest = {
                                if (!marketUi.isLoading) dailyMarketVm.dismissDialog()
                            },
                            title = { Text(marketUi.dialogTitle) },
                            text = {
                                if (marketUi.isLoading) {
                                    Box(
                                        modifier = Modifier.fillMaxWidth(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        CircularProgressIndicator(Modifier.size(48.dp))
                                    }
                                } else {
                                    Text(marketUi.message)
                                }
                            },
                            confirmButton = {
                                TextButton(
                                    onClick = { dailyMarketVm.dismissDialog() },
                                    enabled = !marketUi.isLoading
                                ) {
                                    Text("Đã xem")
                                }
                            },
                            dismissButton = {
                                TextButton(
                                    onClick = { dailyMarketVm.refreshMarketData() },
                                    enabled = !marketUi.isLoading
                                ) {
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