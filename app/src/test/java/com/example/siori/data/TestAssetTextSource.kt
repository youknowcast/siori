package com.example.siori.data

class TestAssetTextSource(private val files: Map<String, String>) : AssetTextSource {
    override fun readText(path: String): String =
        files[path] ?: throw AssetMissingException(path)

    override fun list(path: String): List<String> =
        files.keys.filter { it.startsWith("$path/") }
            .map { it.removePrefix("$path/").substringBefore('/') }
            .distinct()
}
