package com.example.siori.data

import kotlinx.serialization.Serializable

@Serializable
data class Work(
    val id: String,
    val title: String,
    val author: String,
)

@Serializable
data class Catalog(
    val schemaVersion: Int,
    val works: List<Work>,
)
