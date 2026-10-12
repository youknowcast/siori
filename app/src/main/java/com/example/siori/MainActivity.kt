package com.example.siori

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.siori.data.AndroidAssetTextSource
import com.example.siori.data.AssetSioriRepository
import com.example.siori.ui.SioriApp
import com.example.siori.ui.theme.SioriTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val repository = AssetSioriRepository(AndroidAssetTextSource(assets))
        setContent {
            SioriTheme {
                SioriApp(repository)
            }
        }
    }
}
