package com.example.siori.data

import java.io.File

class FileAssetTextSource(private val root: File) : AssetTextSource {
    override fun readText(path: String): String {
        val file = File(root, path)
        if (!file.isFile) throw AssetMissingException(path)
        return file.readText(Charsets.UTF_8)
    }

    override fun list(path: String): List<String> {
        val dir = File(root, path)
        if (!dir.isDirectory) return emptyList()
        return dir.list()?.toList() ?: emptyList()
    }
}
