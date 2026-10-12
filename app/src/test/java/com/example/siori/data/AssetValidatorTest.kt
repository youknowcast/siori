package com.example.siori.data

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File

class AssetValidatorTest {
    private val assetsRoot = File("src/main/assets")

    @Test
    fun bundledAssetsAreValid() {
        val problems = AssetValidator(FileAssetTextSource(assetsRoot)).validate()
        assertEquals(emptyList<String>(), problems)
    }

    @Test
    fun detectsDuplicateWorkIds() {
        val source = TestAssetTextSource(
            mapOf(
                "catalog.json" to """{"schemaVersion":1,"works":[{"id":"w","title":"a","author":"b"},{"id":"w","title":"c","author":"d"}]}""",
                "works/w/characters.json" to """{"schemaVersion":1,"workId":"w","characters":[]}""",
                "works/w/relations.mmd" to "flowchart TD",
            )
        )
        val problems = AssetValidator(source).validate()
        assertTrue(problems.any { it.contains("duplicate work id") })
    }

    @Test
    fun detectsMissingWorkDirectory() {
        val source = TestAssetTextSource(
            mapOf("catalog.json" to """{"schemaVersion":1,"works":[{"id":"missing","title":"a","author":"b"}]}""")
        )
        val problems = AssetValidator(source).validate()
        assertTrue(problems.any { it.contains("missing work directory") })
    }

    @Test
    fun detectsDuplicateCharacterIds() {
        val source = TestAssetTextSource(
            mapOf(
                "catalog.json" to """{"schemaVersion":1,"works":[{"id":"w","title":"a","author":"b"}]}""",
                "works/w/characters.json" to """{"schemaVersion":1,"workId":"w","characters":[{"id":"x","name":"n","description":"d"},{"id":"x","name":"m","description":"e"}]}""",
                "works/w/relations.mmd" to "flowchart TD",
            )
        )
        val problems = AssetValidator(source).validate()
        assertTrue(problems.any { it.contains("duplicate character id") })
    }

    @Test
    fun detectsMermaidNodeWithoutCharacter() {
        val source = TestAssetTextSource(
            mapOf(
                "catalog.json" to """{"schemaVersion":1,"works":[{"id":"w","title":"a","author":"b"}]}""",
                "works/w/characters.json" to """{"schemaVersion":1,"workId":"w","characters":[{"id":"x","name":"n","description":"d"}]}""",
                "works/w/relations.mmd" to "flowchart TD\n  x --> ghost[\"ゴースト\"]",
            )
        )
        val problems = AssetValidator(source).validate()
        assertTrue(problems.any { it.contains("unknown mermaid node") })
    }

    @Test
    fun allowsAuxiliaryMermaidNodes() {
        val source = TestAssetTextSource(
            mapOf(
                "catalog.json" to """{"schemaVersion":1,"works":[{"id":"w","title":"a","author":"b"}]}""",
                "works/w/characters.json" to """{"schemaVersion":1,"workId":"w","characters":[{"id":"x","name":"n","description":"d"}]}""",
                "works/w/relations.mmd" to "flowchart TD\n  aux:family[\"一族\"] --> x[\"n\"]",
            )
        )
        val problems = AssetValidator(source).validate()
        assertEquals(emptyList<String>(), problems)
    }

    @Test
    fun detectsSchemaVersionMismatch() {
        val source = TestAssetTextSource(
            mapOf(
                "catalog.json" to """{"schemaVersion":1,"works":[{"id":"w","title":"a","author":"b"}]}""",
                "works/w/characters.json" to """{"schemaVersion":2,"workId":"w","characters":[]}""",
                "works/w/relations.mmd" to "flowchart TD",
            )
        )
        val problems = AssetValidator(source).validate()
        assertTrue(problems.any { it.contains("schemaVersion mismatch") })
    }

    @Test
    fun detectsWorkIdMismatch() {
        val source = TestAssetTextSource(
            mapOf(
                "catalog.json" to """{"schemaVersion":1,"works":[{"id":"w","title":"a","author":"b"}]}""",
                "works/w/characters.json" to """{"schemaVersion":1,"workId":"other","characters":[]}""",
                "works/w/relations.mmd" to "flowchart TD",
            )
        )
        val problems = AssetValidator(source).validate()
        assertTrue(problems.any { it.contains("workId mismatch") })
    }

    @Test
    fun detectsOrphanWorkDirectory() {
        val source = TestAssetTextSource(
            mapOf(
                "catalog.json" to """{"schemaVersion":1,"works":[{"id":"w","title":"a","author":"b"}]}""",
                "works/w/characters.json" to """{"schemaVersion":1,"workId":"w","characters":[]}""",
                "works/w/relations.mmd" to "flowchart TD",
                "works/stray/characters.json" to """{"schemaVersion":1,"workId":"stray","characters":[]}""",
                "works/stray/relations.mmd" to "flowchart TD",
            )
        )
        val problems = AssetValidator(source).validate()
        assertTrue(problems.any { it.contains("orphan work directory") })
    }

    @Test
    fun detectsMissingRelationsFile() {
        val source = TestAssetTextSource(
            mapOf(
                "catalog.json" to """{"schemaVersion":1,"works":[{"id":"w","title":"a","author":"b"}]}""",
                "works/w/characters.json" to """{"schemaVersion":1,"workId":"w","characters":[]}""",
            )
        )
        val problems = AssetValidator(source).validate()
        assertTrue(problems.any { it.contains("missing relations.mmd") })
    }
}
