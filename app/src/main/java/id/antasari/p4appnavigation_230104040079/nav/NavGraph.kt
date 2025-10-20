package id.antasari.p4appnavigation_230104040079.nav

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navigation
import id.antasari.p4appnavigation_230104040079.R // <-- IMPORT BARU
import id.antasari.p4appnavigation_230104040079.screens.*
import java.net.URLDecoder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavGraph() {
    val nav = rememberNavController()

    Scaffold(
        topBar = {
            // --- PERUBAHAN UTAMA DIMULAI DI SINI ---
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                navigationIcon = {
                    val canNavigateBack = nav.previousBackStackEntry != null
                    if (canNavigateBack) {
                        IconButton(onClick = { nav.popBackStack() }) {
                            Icon(
                                Icons.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                },
                title = {
                    val routeNow = nav.currentBackStackEntryAsState().value?.destination?.route.orEmpty()
                    val isHome = routeNow == Route.Home.path

                    if (isHome) {
                        // HANYA di Home: Tampilkan Logo + "Navio"
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                painter = painterResource(R.drawable.ic_navio),
                                contentDescription = "Navio",
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(
                                text = stringResource(R.string.app_name), // Mengambil "Navio" dari string resources
                                color = MaterialTheme.colorScheme.onPrimary,
                                style = MaterialTheme.typography.titleLarge
                            )
                        }
                    } else {
                        // Layar lain: tampilkan judul dinamis seperti biasa
                        Text(
                            text = currentTitle(nav),
                            color = MaterialTheme.colorScheme.onPrimary,
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                }
            )
            // --- BATAS PERUBAHAN ---
        }
    ) { padding ->
        NavHost(
            modifier = Modifier.padding(padding),
            navController = nav,
            startDestination = Route.Home.path
        ) {

            // 🏠 Home
            composable(Route.Home.path) { HomeScreen(nav) }

            // 🔹 Activity A → Activity B
            composable(Route.ActivityA.path) {
                ActivityAScreen(onOpen = { nav.navigate(Route.ActivityB.path) })
            }
            composable(Route.ActivityB.path) { ActivityBScreen() }

            // 🔹 Activity C → Activity D (kirim & terima data)
            composable(Route.ActivityC.path) {
                ActivityCScreen(
                    onSend = { name, nim ->
                        nav.navigate(Route.ActivityD.make(name, nim))
                    }
                )
            }

            composable(
                route = Route.ActivityD.path,
                arguments = listOf(
                    navArgument("name") { type = NavType.StringType },
                    navArgument("studentId") { type = NavType.StringType }
                )
            ) { backStack ->
                val name = URLDecoder.decode(backStack.arguments?.getString("name") ?: "", "utf-8")
                val nim = URLDecoder.decode(backStack.arguments?.getString("studentId") ?: "", "utf-8")

                ActivityDScreen(
                    name = name,
                    nim = nim,
                    onResend = { nav.popBackStack() }
                )
            }

            // 🔹 Step navigation demo
            composable(Route.Step1.path) {
                StepScreen(
                    step = 1,
                    onNext = { nav.navigate(Route.Step2.path) },
                    onClearToHome = {
                        nav.navigate(Route.Home.path) {
                            popUpTo(Route.Home.path) { inclusive = false }
                            launchSingleTop = true
                        }
                    }
                )
            }

            composable(Route.Step2.path) {
                StepScreen(
                    step = 2,
                    onNext = { nav.navigate(Route.Step3.path) },
                    onClearToHome = {
                        nav.navigate(Route.Home.path) {
                            popUpTo(Route.Home.path) { inclusive = false }
                            launchSingleTop = true
                        }
                    }
                )
            }

            composable(Route.Step3.path) {
                StepScreen(
                    step = 3,
                    onNext = null,
                    onClearToHome = {
                        nav.navigate(Route.Home.path) {
                            popUpTo(Route.Home.path) { inclusive = false }
                            launchSingleTop = true
                        }
                    }
                )
            }

            // 🧩 HUB (Nested Graph)
            navigation(
                startDestination = Route.HubDashboard.path,
                route = Route.Hub.path
            ) {
                composable(Route.HubDashboard.path) { HubScreen(nav, HubTab.Dashboard) }
                composable(Route.HubMessages.path) { HubScreen(nav, HubTab.Messages) }
                composable(Route.HubProfile.path) { HubScreen(nav, HubTab.Profile) }

                composable(
                    route = Route.HubMsgDetail.path,
                    arguments = listOf(navArgument("id") { type = NavType.StringType })
                ) { backStack ->
                    val id = backStack.arguments?.getString("id").orEmpty()
                    MessageDetailScreen(id = id, onBack = { nav.popBackStack() })
                }
            }
        }
    }
}

// Fungsi TopBar yang lama sudah tidak diperlukan lagi dan bisa dihapus.

@Composable
private fun currentTitle(nav: NavHostController): String {
    val route = nav.currentBackStackEntryAsState().value?.destination?.route ?: ""
    return when {
        route.startsWith("hub/messages/detail") -> "Message Detail"
        route.startsWith("hub/messages") -> "Messages Fragment"
        route.startsWith("hub/profile") -> "Profile Fragment"
        route.startsWith("hub/dashboard") -> "Dashboard Fragment"
        route.startsWith("hub") -> "Activity + Fragment Hub"
        route.startsWith("activity_d") -> "Activity D - Data Display"
        route == Route.ActivityC.path -> "Activity C - Send Data"
        route == Route.ActivityA.path -> "Activity A"
        route == Route.ActivityB.path -> "Launched by Intent"
        route == Route.Step1.path -> "Step 1 of 3"
        route == Route.Step2.path -> "Step 2 of 3"
        route == Route.Step3.path -> "Step 3 of 3"
        else -> "Navigation Lab"
    }
}