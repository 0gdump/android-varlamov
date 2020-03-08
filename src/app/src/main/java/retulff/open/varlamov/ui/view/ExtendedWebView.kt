package retulff.open.varlamov.ui.view

import android.content.Context
import android.text.Html
import android.webkit.WebChromeClient
import android.webkit.WebView
import org.apache.commons.text.StringEscapeUtils


class ExtendedWebView(context: Context) : WebView(context) {

    init {

        settings.blockNetworkImage = true
        settings.javaScriptEnabled = true

        webChromeClient = WebChromeClient()
    }

    fun getPageSource(): String {

        var html = ""

        evaluateJavascript(
            """
                (function() {
                    alert("test");
                })();
            """.trimMargin()
        )
        { rawValue ->
            val decodedHtml = Html.fromHtml(rawValue).toString()
            html = StringEscapeUtils.unescapeJava(decodedHtml)
        }

        return html
    }
}