package com.example.siori.data

import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Test

class SerializationTest {
    private val json = Json { ignoreUnknownKeys = true }

    @Test
    fun parsesCatalog() {
        val text = """
            {"schemaVersion":1,"works":[{"id":"sample-novel","title":"作品名","author":"著者名"}]}
        """.trimIndent()
        val catalog = json.decodeFromString<Catalog>(text)
        assertEquals(1, catalog.schemaVersion)
        assertEquals("sample-novel", catalog.works.single().id)
        assertEquals("著者名", catalog.works.single().author)
    }

    @Test
    fun parsesCharactersWithDefaultAliases() {
        val text = """
            {"schemaVersion":1,"workId":"sample-novel","characters":[{"id":"alice","name":"アリス","description":"主人公。"}]}
        """.trimIndent()
        val file = json.decodeFromString<CharactersFile>(text)
        assertEquals("sample-novel", file.workId)
        assertEquals(emptyList<String>(), file.characters.single().aliases)
        assertEquals("主人公。", file.characters.single().description)
    }
}
