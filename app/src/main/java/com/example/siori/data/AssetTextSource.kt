package com.example.siori.data

interface AssetTextSource {
    fun readText(path: String): String
    fun list(path: String): List<String>
}
