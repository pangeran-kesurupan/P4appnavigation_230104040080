package id.antasari.p4appnavigation_230104040079

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.fillMaxSize
import id.antasari.p4appnavigation_230104040079.nav.NavGraph
import id.antasari.p4appnavigation_230104040079.ui.theme.P4appnavigation_230104040079Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Theme sesuai paket dan NIM kamu
            P4appnavigation_230104040079Theme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    NavGraph()
                }
            }
        }
    }
}
