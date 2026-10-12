package com.example.siori

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import com.example.siori.ui.theme.SioriTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SioriTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    Text("しおり")
                }
            }
        }
    }
}
