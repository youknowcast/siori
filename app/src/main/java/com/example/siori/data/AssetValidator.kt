package com.example.siori.data

import kotlinx.serialization.json.Json

object MermaidNodeIds {
    private val nodePattern = Regex("""(?:^|[\s\->|])([A-Za-z0-9_:]+)\s*[\[\(\{]""")

    fun referencedCharacterIds(mermaid: String): List<String> =
        nodePattern.findAll(mermaid)
            .map { it.groupValues[1] }
            .filterNot { it.startsWith("aux:") }
            .distinct()
            .toList()
}

class AssetValidator(
    private val source: AssetTextSource,
    private val json: Json = Json { ignoreUnknownKeys = true },
) {
    fun validate(): List<String> {
        val problems = mutableListOf<String>()
        val catalog = try {
            json.decodeFromString<Catalog>(source.readText("catalog.json"))
        } catch (e: Exception) {
            return listOf("catalog.json: ${e.message}")
        }

        val ids = catalog.works.map { it.id }
        ids.groupingBy { it }.eachCount().filterValues { it > 1 }.keys.forEach {
            problems += "duplicate work id: $it"
        }

        ids.forEach { workId ->
            problems += validateWork(workId, catalog.schemaVersion)
        }

        val known = ids.toSet()
        source.list("works").filterNot { it in known }.forEach {
            problems += "orphan work directory: $it"
        }

        return problems
    }

    private fun validateWork(workId: String, catalogSchema: Int): List<String> {
        val problems = mutableListOf<String>()
        val charactersPath = "works/$workId/characters.json"
        val relationsPath = "works/$workId/relations.mmd"

        val file = try {
            json.decodeFromString<CharactersFile>(source.readText(charactersPath))
        } catch (e: Exception) {
            problems += "missing work directory or characters.json for: $workId"
            return problems
        }

        if (file.schemaVersion != catalogSchema) {
            problems += "schemaVersion mismatch in $charactersPath"
        }
        if (file.workId != workId) {
            problems += "workId mismatch in $charactersPath: ${file.workId}"
        }

        file.characters.groupingBy { it.id }.eachCount().filterValues { it > 1 }.keys.forEach {
            problems += "duplicate character id in $workId: $it"
        }

        val mermaid = try {
            source.readText(relationsPath)
        } catch (e: Exception) {
            problems += "missing relations.mmd for: $workId"
            return problems
        }

        val known = file.characters.map { it.id }.toSet()
        MermaidNodeIds.referencedCharacterIds(mermaid).filterNot { it in known }.forEach {
            problems += "unknown mermaid node in $workId: $it"
        }

        return problems
    }
}
