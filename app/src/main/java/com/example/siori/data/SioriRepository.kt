package com.example.siori.data

interface SioriRepository {
    suspend fun loadCatalog(): List<Work>
    suspend fun loadCharacters(workId: String): List<Character>
    suspend fun loadRelations(workId: String): String
}
