package com.example.siori.data

import kotlinx.serialization.Serializable

@Serializable
data class Character(
    val id: String,
    val name: String,
    val aliases: List<String> = emptyList(),
    val description: String,
)

@Serializable
data class CharactersFile(
    val schemaVersion: Int,
    val workId: String,
    val characters: List<Character>,
)
