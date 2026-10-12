package com.example.siori.data

import android.content.res.AssetManager

class AndroidAssetTextSource(private val assets: AssetManager) : AssetTextSource {
    override fun readText(path: String): String =
        try {
            assets.open(path).bufferedReader(Charsets.UTF_8).use { it.readText() }
        } catch (e: Exception) {
            throw AssetMissingException(path)
        }

    override fun list(path: String): List<String> =
        assets.list(path)?.toList() ?: emptyList()
}
