package id.antasari.p4appnavigation_230104040079.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Chat
import androidx.compose.material.icons.outlined.Dashboard
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import id.antasari.p4appnavigation_230104040079.nav.Route

/* ---------------- Tabs Enum ---------------- */
sealed class HubTab(val label: String, val icon: androidx.compose.ui.graphics.vector.ImageVector) {
    data object Dashboard : HubTab("Dashboard", Icons.Outlined.Dashboard)
    data object Messages : HubTab("Messages", Icons.Outlined.Chat)
    data object Profile : HubTab("Profile", Icons.Outlined.AccountCircle)
}

/* ---------------- Main Hub Screen ---------------- */
@Composable
fun HubScreen(nav: NavHostController, tab: HubTab) {
    val selected = tab

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = selected is HubTab.Dashboard,
                    onClick = {
                        nav.navigate(Route.HubDashboard.path) {
                            popUpTo(Route.Hub.path) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    icon = { Icon(HubTab.Dashboard.icon, null) },
                    label = { Text(HubTab.Dashboard.label) }
                )

                NavigationBarItem(
                    selected = selected is HubTab.Messages,
                    onClick = {
                        nav.navigate(Route.HubMessages.path) {
                            popUpTo(Route.Hub.path) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    icon = { Icon(HubTab.Messages.icon, null) },
                    label = { Text(HubTab.Messages.label) }
                )

                NavigationBarItem(
                    selected = selected is HubTab.Profile,
                    onClick = {
                        nav.navigate(Route.HubProfile.path) {
                            popUpTo(Route.Hub.path) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    icon = { Icon(HubTab.Profile.icon, null) },
                    label = { Text(HubTab.Profile.label) }
                )
            }
        }
    ) { padding ->
        when (tab) {
            is HubTab.Dashboard -> DashboardTab(Modifier.padding(padding))
            // ✅ PERUBAHAN 2: Panggil MessagesTab dengan lambda yang mengirim ID
            is HubTab.Messages -> MessagesTab(Modifier.padding(padding)) { mid ->
                nav.navigate(Route.HubMsgDetail.of(mid))
            }
            is HubTab.Profile -> ProfileTab(Modifier.padding(padding))
        }
    }
}

/* ---------------- Tabs Implementation ---------------- */

@Composable
private fun DashboardTab(mod: Modifier = Modifier) {
    Column(
        mod.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Dashboard Fragment", style = MaterialTheme.typography.titleMedium)
        Card {
            Column(
                Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("Welcome!", fontWeight = FontWeight.SemiBold)
                Text("This screen represents a dashboard inside a single Activity hosting multiple fragments/tabs.")
            }
        }

        InfoCard(
            "Hints",
            listOf(
                "Each tab maps to a fragment-like screen",
                "Bottom navigation switches tabs within the same Activity",
                "Back preserves tab state unless the stack is cleared"
            )
        )
    }
}

// ✅ PERUBAHAN 1: Ubah parameter onOpenDetail untuk menerima String
@Composable
private fun MessagesTab(mod: Modifier = Modifier, onOpenDetail: (String) -> Unit) {
    Column(
        mod.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Messages Fragment", style = MaterialTheme.typography.titleMedium)

        // Pesan 1
        ElevatedCard(
            modifier = Modifier.fillMaxWidth().clickable { onOpenDetail("1") } // Kirim ID "1"
        ) {
            ListItem(
                leadingContent = { Icon(Icons.Outlined.Chat, null) },
                headlineContent = { Text("Android System") },
                supportingContent = { Text("Device setup completed successfully.") },
                trailingContent = { Text("2m") }
            )
        }

        // Pesan 2
        ElevatedCard(
            modifier = Modifier.fillMaxWidth().clickable { onOpenDetail("2") } // Kirim ID "2"
        ) {
            ListItem(
                leadingContent = { Icon(Icons.Outlined.Chat, null) },
                headlineContent = { Text("Compose Tips") },
                supportingContent = { Text("Use Scaffold with TopAppBar and NavigationBar for app structure.") },
                trailingContent = { Text("1h") }
            )
        }

        // Pesan 3
        ElevatedCard(
            modifier = Modifier.fillMaxWidth().clickable { onOpenDetail("3") } // Kirim ID "3"
        ) {
            ListItem(
                leadingContent = { Icon(Icons.Outlined.Chat, null) },
                headlineContent = { Text("Release Notes") },
                supportingContent = { Text("Material 3 components power modern UI on Android.") },
                trailingContent = { Text("ytd") }
            )
        }
    }
}

// ✅ PERUBAHAN 3: Ubah MessageDetailScreen secara keseluruhan
@Composable
fun MessageDetailScreen(id: String, onBack: () -> Unit, mod: Modifier = Modifier) {
    // Tentukan konten pesan berdasarkan ID yang diterima
    val content = when (id) {
        "1" -> "System: Device setup completed successfully."
        "2" -> "Tips: Use Scaffold + TopAppBar + NavigationBar for clean layouts."
        else -> "Unknown message #$id"
    }

    Column(
        mod.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Card {
            Column(
                Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Message Detail",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(Modifier.height(16.dp))
                Text(content) // Tampilkan konten pesan yang sesuai
                Spacer(Modifier.height(24.dp))
                Button(onClick = onBack) {
                    Text("Back to Messages")
                }
            }
        }
    }
}

@Composable
private fun ProfileTab(mod: Modifier = Modifier) {
    Column(
        mod.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Profile Fragment", style = MaterialTheme.typography.titleMedium)
        Card {
            Column(
                Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ElevatedCard {
                    ListItem(
                        leadingContent = { Icon(Icons.Outlined.AccountCircle, null) },
                        headlineContent = { Text("Muhammad Riduwan") }, // Menggunakan nama dari proyek Anda sebelumnya
                        supportingContent = { Text("God of War") }
                    )
                }
                // ... (Sisa kode ProfileTab tetap sama)
            }
        }
    }
}

/* ------- Kartu Info kecil ------- */
@Composable
private fun InfoCard(title: String, bullets: List<String>) {
    Card {
        Column(
            Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(title, style = MaterialTheme.typography.titleMedium)
            bullets.forEach { Text("- $it") }
        }
    }
}