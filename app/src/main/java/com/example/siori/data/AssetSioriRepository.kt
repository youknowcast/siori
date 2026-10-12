package com.example.siori.data

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

class AssetSioriRepository(
    private val source: AssetTextSource,
    private val json: Json = Json { ignoreUnknownKeys = true },
    private val io: CoroutineDispatcher = Dispatchers.IO,
) : SioriRepository {

    override suspend fun loadCatalog(): List<Work> = withContext(io) {
        val text = source.readText("catalog.json")
        try {
            json.decodeFromString<Catalog>(text).works
        } catch (e: Exception) {
            throw AssetParseException("catalog.json", e)
        }
    }

    override suspend fun loadCharacters(workId: String): List<Character> = withContext(io) {
        val path = "works/$workId/characters.json"
        val text = source.readText(path)
        try {
            json.decodeFromString<CharactersFile>(text).characters
        } catch (e: Exception) {
            throw AssetParseException(path, e)
        }
    }

    override suspend fun loadRelations(workId: String): String = withContext(io) {
        source.readText("works/$workId/relations.mmd")
    }
}
