package com.example.siori.data

class AssetMissingException(path: String) : Exception("Asset not found: $path")

class AssetParseException(path: String, cause: Throwable) : Exception("Failed to parse asset: $path", cause)
