package id.antasari.p4appnavigation_230104040079.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CreditCard
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import id.antasari.p4appnavigation_230104040079.data.UserPreferencesRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivityCScreen(onSend: (String, String) -> Unit) {
    // State untuk UI
    var name by rememberSaveable { mutableStateOf("") }
    var nim by rememberSaveable { mutableStateOf("") }

    // Persiapan untuk Snackbar dan DataStore
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val userPrefsRepo = remember { UserPreferencesRepository(context) }

    // ✅ CEKLIST: Memuat data dari DataStore saat layar pertama kali dibuka
    LaunchedEffect(Unit) {
        // Ambil data terakhir yang tersimpan dan isi ke TextField
        name = userPrefsRepo.userName.first()
        nim = userPrefsRepo.userNim.first()
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(paddingValues) // Gunakan padding dari Scaffold
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        "Data Input Form",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )

                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Name") },
                        leadingIcon = { Icon(Icons.Outlined.Person, null) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = nim,
                        onValueChange = { nim = it },
                        label = { Text("Student ID") },
                        leadingIcon = { Icon(Icons.Outlined.CreditCard, null) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        // Hanya izinkan input angka jika ingin lebih ketat
                        // keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )

                    CodeBlock(
                        """
                        // Di Compose Navigation – kita kirim via argumen route
                        // Data juga disimpan ke DataStore untuk persistensi
                        """.trimIndent()
                    )

                    Button(
                        onClick = {
                            val trimmedName = name.trim()
                            val trimmedNim = nim.trim()

                            // ✅ CEKLIST: Validasi input dan tampilkan Snackbar jika invalid
                            if (trimmedNim.any { !it.isDigit() }) {
                                scope.launch {
                                    snackbarHostState.showSnackbar("NIM harus berupa angka!")
                                }
                            } else {
                                // ✅ CEKLIST: Simpan data ke DataStore sebelum navigasi
                                scope.launch {
                                    userPrefsRepo.save(trimmedName, trimmedNim)
                                    onSend(trimmedName, trimmedNim)
                                }
                            }
                        },
                        enabled = name.isNotBlank() && nim.isNotBlank(),
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text("Send to Detail")
                    }
                }
            }
            InfoCard(
                title = "Intent Extras & DataStore",
                bullets = listOf(
                    "Data dikirim via argumen route",
                    "Data juga disimpan ke DataStore",
                    "Saat kembali, form akan terisi data terakhir"
                )
            )
        }
    }
}


@Composable
fun ActivityDScreen(name: String, nim: String, onResend: () -> Unit) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    "Received Data",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )

                ElevatedCard {
                    ListItem(
                        headlineContent = { Text("Name") },
                        supportingContent = { Text(name) },
                        leadingContent = { Icon(Icons.Outlined.Person, null) }
                    )
                }

                ElevatedCard {
                    ListItem(
                        headlineContent = { Text("Student ID") },
                        supportingContent = { Text(nim) },
                        leadingContent = { Icon(Icons.Outlined.CreditCard, null) }
                    )
                }

                CodeBlock(
                    """
                    // Di Compose Navigation – data dibaca dari argumen backStackEntry
                    """.trimIndent()
                )

                OutlinedButton(onClick = onResend) {
                    Text("Resend / Edit")
                }
            }
        }

        InfoCard(
            title = "Data Flow",
            bullets = listOf(
                "Activity C: user input",
                "Data dikemas (argumen route)",
                "Activity D: tampilkan hasil"
            )
        )
    }
}

/* === Komponen kecil lokal === */

@Composable
private fun CodeBlock(text: String) {
    Surface(
        tonalElevation = 2.dp,
        shape = MaterialTheme.shapes.medium
    ) {
        Text(
            text = text,
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun InfoCard(title: String, bullets: List<String>) {
    Card(
        modifier = Modifier.padding(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(title, style = MaterialTheme.typography.titleMedium)
            bullets.forEach { Text("• $it") }
        }
    }
}