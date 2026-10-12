package com.example.siori.data

import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File

class MermaidViewerAssetTest {
    private val assetsRoot = File("src/main/assets")

    @Test
    fun viewerReferencesBundledMermaidAndDefinesRenderFunction() {
        val html = File(assetsRoot, "mermaid/viewer.html").readText()
        assertTrue(html.contains("mermaid.min.js"))
        assertTrue(html.contains("window.renderDiagram"))
    }

    @Test
    fun mermaidBundleIsPresentAndNonTrivial() {
        val bundle = File(assetsRoot, "mermaid/mermaid.min.js")
        assertTrue(bundle.isFile)
        assertTrue(bundle.length() > 1_000_000)
    }
}
