package com.example.siori.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class AssetSioriRepositoryTest {
    private val source = TestAssetTextSource(
        mapOf(
            "catalog.json" to """{"schemaVersion":1,"works":[{"id":"w1","title":"本","author":"著者"}]}""",
            "works/w1/characters.json" to """{"schemaVersion":1,"workId":"w1","characters":[{"id":"a","name":"A","description":"d"}]}""",
            "works/w1/relations.mmd" to "flowchart TD\n  a[\"A\"]",
        )
    )
    private val repo = AssetSioriRepository(source, io = Dispatchers.Unconfined)

    @Test
    fun loadsCatalog() = runTest {
        assertEquals("w1", repo.loadCatalog().single().id)
    }

    @Test
    fun loadsCharacters() = runTest {
        assertEquals("A", repo.loadCharacters("w1").single().name)
    }

    @Test
    fun loadsRelations() = runTest {
        assertEquals("flowchart TD\n  a[\"A\"]", repo.loadRelations("w1"))
    }
}
