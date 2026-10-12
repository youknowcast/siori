package com.example.siori.ui.search

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class QueryNormalizerTest {
    @Test
    fun foldsWidthAndCase() {
        assertEquals("abc", QueryNormalizer.normalize("ＡＢＣ"))
    }

    @Test
    fun foldsKatakanaToHiragana() {
        assertEquals("ありす", QueryNormalizer.normalize("アリス"))
    }

    @Test
    fun matchesByName() {
        assertTrue(QueryNormalizer.matches("ありす", "アリス", emptyList()))
    }

    @Test
    fun matchesByAliasPartial() {
        assertTrue(QueryNormalizer.matches("キャロル", "キャロル", listOf("キャロル夫人")))
        assertTrue(QueryNormalizer.matches("夫人", "キャロル", listOf("キャロル夫人")))
    }

    @Test
    fun doesNotMatchUnrelated() {
        assertFalse(QueryNormalizer.matches("ボブ", "アリス", listOf("アリス嬢")))
    }
}
