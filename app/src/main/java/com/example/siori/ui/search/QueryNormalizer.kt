package com.example.siori.ui.search

object QueryNormalizer {
    fun normalize(input: String): String {
        val folded = buildString {
            for (ch in input) {
                val mapped = when {
                    ch == '\u3000' -> ' '
                    ch in '\uFF01'..'\uFF5E' -> ch - 0xFEE0
                    ch in '\u30A1'..'\u30F3' -> ch - 0x60
                    ch == '\u30F4' -> '\u3094'
                    else -> ch
                }
                append(mapped)
            }
        }
        return folded.lowercase()
    }

    fun matches(query: String, name: String, aliases: List<String>): Boolean {
        val normalizedQuery = normalize(query).trim()
        if (normalizedQuery.isEmpty()) return false
        val haystack = (listOf(name) + aliases).map { normalize(it) }
        return haystack.any { it.contains(normalizedQuery) }
    }
}
