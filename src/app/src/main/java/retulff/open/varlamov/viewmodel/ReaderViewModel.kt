package retulff.open.varlamov.viewmodel

import android.app.Application
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import retulff.open.varlamov.ui.view.ExtendedWebView

class ReaderViewModel(application: Application) : AndroidViewModel(application) {

    //region Fields

    private val url = MutableLiveData<String>()

    fun getUrl(): LiveData<String> = url
    fun postUrl(value: String) {
        url.value = value
        loadPage(url.value!!)
    }

    private val document = MutableLiveData<Document>()

    fun getDocument(): LiveData<Document> = document

    private val contentBody = MutableLiveData<Element>()

    fun getContentBody(): LiveData<Element> = contentBody

    //endregion

    private lateinit var webLoader: ExtendedWebView

    private fun loadPage(url: String) {

        initializeWebLoader()

        webLoader.loadUrl(url)
    }

    private fun initializeWebLoader() {
        if (!::webLoader.isInitialized) {

            webLoader = ExtendedWebView(getApplication())
            webLoader.webViewClient = object : WebViewClient() {

                override fun onPageFinished(view: WebView, url: String) {

                    super.onPageFinished(webLoader, url)

                    parsePublication()
                }
            }
        }
    }

    private fun parsePublication() {

        //document.value = Jsoup.parse(webLoader.getPageSource())

        //contentBody.value = document.value!!.body().getElementById("entrytext")
    }
}