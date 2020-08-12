package com.varlamov.android.presentation.reader

import com.varlamov.android.App
import com.varlamov.android.BuildConfig
import com.varlamov.android.model.platform.livejournal.model.Publication
import com.varlamov.android.model.reader.*
import com.varlamov.android.presentation.global.Contentator
import com.varlamov.android.presentation.global.MvpPresenterX
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import moxy.InjectViewState
import open.v0gdump.recontent.ReContent
import open.v0gdump.recontent.ReContentEvents
import open.v0gdump.recontent.model.NodeRule
import open.v0gdump.recontent.model.SectionRule
import open.v0gdump.recontent.model.SpecificNodesHandler
import org.jsoup.nodes.Element
import org.jsoup.nodes.TextNode


@InjectViewState
class ReaderScreenPresenter(
    val publication: Publication
) : MvpPresenterX<ReaderScreenView>() {

    //region ReContent rules

    private val recontentQuoteRule = NodeRule(
        selector = "blockquote:not([class])",
        matchCallback = ::prepareQuoteBuffers,
        treeParsedCallback = ::addQuote,
        sectionRule = SectionRule(
            childRules = listOf(
                NodeRule(selector = "i", matchCallback = ::appendToQuoteBuffer),
                NodeRule(selector = "a", matchCallback = ::appendToQuoteBuffer),
                NodeRule(selector = "b", matchCallback = ::appendToQuoteBuffer),
                NodeRule(selector = "u", matchCallback = ::appendToQuoteBuffer),
                NodeRule(selector = "br", matchCallback = ::finishQuotePart)
            ),
            specificNodesHandler = SpecificNodesHandler(textNodeHandler = ::appendToQuoteBuffer)
        )
    )

    private val recontentRules = listOf(
        SectionRule(
            selector = "div#entrytext.j-e-text",
            childRules = listOf(
                NodeRule(selector = ".j-imagewrapper", matchCallback = ::addFeedbackImagePart),
                NodeRule(selector = "img", matchCallback = ::addImagePart),

                NodeRule(selector = "i", matchCallback = ::appendToTextBuffer),
                NodeRule(selector = "a", matchCallback = ::appendToTextBuffer),
                NodeRule(selector = "b", matchCallback = ::appendToTextBuffer),
                NodeRule(selector = "u", matchCallback = ::appendToTextBuffer),
                NodeRule(selector = "br", matchCallback = ::addTextPart),

                NodeRule(selector = "div.twitter-tweet", matchCallback = ::appendTwitter),
                NodeRule(selector = "iframe.instagram-media", matchCallback = ::appendInstagram),

                recontentQuoteRule,

                // Ignore broken blockquotes
                NodeRule(selector = "blockquote", matchCallback = { _, _ -> })
            ),
            specificNodesHandler = SpecificNodesHandler(
                textNodeHandler = ::appendToTextBuffer,
                unmatchedElementHandler = ::addUnimplementedPart
            )
        )
    )

    //endregion

    //region ReContent variables

    private var quoteParagraphBuffer = ""
    private val quoteParts = mutableListOf<QuoteElement>()

    private var paragraphBuffer = ""
    private val publicationParts = mutableListOf<PublicationElement>()

    //endregion

    //region Content state machine

    private val contentator = Contentator.Store<PublicationElement>()
    private val eventsHandler = ReContentEvents(
        onError = { throwable ->
            contentator.proceed(Contentator.Action.Error(throwable))
        },
        afterParse = {
            contentator.proceed(Contentator.Action.Data(publicationParts))
        }
    )

    //endregion

    init {
        contentator.render = { viewState.renderState(it) }
        launch { sideEffectsConsumer() }
    }

    private suspend fun sideEffectsConsumer() {
        contentator.sideEffects.consumeEach { effect ->
            when (effect) {
                is Contentator.SideEffect.LoadData -> {
                    runReContent()
                }
                is Contentator.SideEffect.ErrorEvent -> {
                    viewState.showMessage(effect.error.message.orEmpty())
                }
            }
        }
    }

    fun onActivityCreated() = load()

    fun refresh() = contentator.proceed(Contentator.Action.Refresh)
    fun load() = contentator.proceed(Contentator.Action.LoadData)

    private fun runReContent() {
        paragraphBuffer = ""
        publicationParts.clear()

        ReContent(App.instance, eventsHandler).apply {
            sectionsRules = recontentRules
            load(publication.url)
        }
    }

    //region Main content

    private fun addFeedbackImagePart(element: Element, tag: String?) {
        val imgs = element.getElementsByTag("img")

        // FIXME(CODE) Create feedback (!!!) image part
        if (imgs.size != 0) {
            addImagePart(imgs.first(), tag)
        }
    }

    private fun addImagePart(element: Element, tag: String?) {
        val src = element.attr("src")
        if (src.isNotEmpty()) {
            publicationParts.add(ImagePublicationElement(src))
        }
    }

    private fun appendToTextBuffer(element: Element, tag: String?) {
        if (element.html().isNotBlank()) {
            paragraphBuffer += element.outerHtml()
        }
    }

    private fun appendToTextBuffer(node: TextNode) {
        paragraphBuffer += node.text()
    }

    private fun addTextPart(element: Element, tag: String?) {
        if (paragraphBuffer.isNotBlank()) {
            publicationParts.add(TextPublicationElement(paragraphBuffer))
            paragraphBuffer = ""
        }
    }

    private fun addUnimplementedPart(element: Element) {
        if (BuildConfig.DEBUG) {
            publicationParts.add(UnimplementedPublicationElement(element.cssSelector()))
        }
    }

    //endregion

    //region Social's

    // TODO(CODE) Show iframe instead of url link
    // https://stackoverflow.com/questions/27836043/get-tweet-url-having-only-tweet-id/27843083
    private fun appendTwitter(element: Element, tag: String?) {
        val tweetId = element.select("iframe").attr("data-tweet-id")
        val tweetUrl = "https://twitter.com/crunch/status/$tweetId"

        if (tweetUrl.isNotEmpty()) {
            publicationParts += TwitterPublicationElement(tweetUrl)
        }
    }

    // TODO(CODE) Show iframe instead of url link
    private fun appendInstagram(element: Element, tag: String?) {
        val embedPostUrl = element.attr("src")
        val regex = Regex("(https:\\/\\/www\\.instagram\\.com\\/p\\/.*)(\\/embed)")
        val urlMatch = regex.find(embedPostUrl) ?: return

        if (urlMatch.groups.size != 3) return

        publicationParts += InstagramPublicationElement(urlMatch.groupValues[1])
    }

    //endregion

    //region Quote content

    private fun prepareQuoteBuffers(element: Element, tag: String?) {
        quoteParagraphBuffer = ""
        quoteParts.clear()
    }

    private fun appendToQuoteBuffer(element: Element, tag: String?) {
        if (element.html().isNotBlank()) {
            quoteParagraphBuffer += element.outerHtml()
        }
    }

    private fun appendToQuoteBuffer(node: TextNode) {
        quoteParagraphBuffer += node.text()
    }

    private fun finishQuotePart(element: Element, tag: String?) = addQuotePart()

    private fun addQuotePart() {
        if (quoteParagraphBuffer.isNotEmpty()) {
            quoteParts.add(
                QuoteElement(
                    quoteParagraphBuffer
                )
            )
            quoteParagraphBuffer = ""
        }
    }

    private fun addQuote(element: Element, tag: String?) {
        if (quoteParagraphBuffer.isNotEmpty()) addQuotePart()
        if (quoteParts.isEmpty()) return

        // .toList() копирует список. Без .toList() класс сохранит ссылку на quoteParts
        publicationParts.add(QuotePublicationElement(quoteParts.toList()))
    }

    //endregion
}