package com.example.siori.ui.relations

import android.annotation.SuppressLint
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.webkit.WebViewAssetLoader
import org.json.JSONObject

private const val VIEWER_URL = "https://appassets.androidplatform.net/assets/mermaid/viewer.html"
private const val ASSET_PREFIX = "https://appassets.androidplatform.net/"

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun MermaidDiagram(diagram: String, modifier: Modifier = Modifier) {
    val latest = rememberUpdatedState(diagram)
    AndroidView(
        modifier = modifier,
        factory = { context ->
            WebView(context).apply {
                settings.javaScriptEnabled = true
                settings.setSupportZoom(true)
                settings.builtInZoomControls = true
                settings.displayZoomControls = false
                settings.loadWithOverviewMode = true
                settings.useWideViewPort = true
                settings.allowFileAccess = false
                settings.allowContentAccess = false
                settings.blockNetworkLoads = true
                val loader = WebViewAssetLoader.Builder()
                    .addPathHandler("/assets/", WebViewAssetLoader.AssetsPathHandler(context))
                    .build()
                webViewClient = object : WebViewClient() {
                    override fun shouldInterceptRequest(
                        view: WebView,
                        request: WebResourceRequest,
                    ): WebResourceResponse? = loader.shouldInterceptRequest(request.url)

                    override fun shouldOverrideUrlLoading(
                        view: WebView,
                        request: WebResourceRequest,
                    ): Boolean = !request.url.toString().startsWith(ASSET_PREFIX)

                    override fun onPageFinished(view: WebView, url: String) {
                        view.evaluateJavascript(renderCall(latest.value), null)
                    }
                }
                loadUrl(VIEWER_URL)
            }
        },
        update = { webView ->
            if (webView.progress == 100) {
                webView.evaluateJavascript(renderCall(diagram), null)
            }
        },
        onRelease = { it.destroy() },
    )
}

private fun renderCall(diagram: String): String =
    "window.renderDiagram(" + JSONObject.quote(diagram) + ")"
