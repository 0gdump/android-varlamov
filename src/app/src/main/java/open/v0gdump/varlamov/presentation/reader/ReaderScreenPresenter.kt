package open.v0gdump.varlamov.presentation.reader

import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import moxy.InjectViewState
import open.v0gdump.recontent.ReContent
import open.v0gdump.recontent.ReContentEvents
import open.v0gdump.recontent.model.NodeRule
import open.v0gdump.recontent.model.SectionRule
import open.v0gdump.recontent.model.SpecificNodesHandler
import open.v0gdump.varlamov.App
import open.v0gdump.varlamov.BuildConfig
import open.v0gdump.varlamov.model.platform.livejournal.model.Publication
import open.v0gdump.varlamov.model.reader.*
import open.v0gdump.varlamov.model.reader.quote.QuoteElement
import open.v0gdump.varlamov.presentation.global.Contentator
import open.v0gdump.varlamov.presentation.global.MvpPresenterX
import org.jsoup.nodes.Element
import org.jsoup.nodes.TextNode

@InjectViewState
class ReaderScreenPresenter : MvpPresenterX<ReaderScreenView>() {

    var publication: Publication? = null

    //region ReContent rules

    private val recontentQuoteRule = NodeRule(
        selector = "blockquote",
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

                recontentQuoteRule
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
        check(publication != null) { "No publication in presenter" }

        paragraphBuffer = ""
        publicationParts.clear()

        ReContent(App.instance, eventsHandler).apply {
            sectionsRules = recontentRules
            load(publication!!.url)
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
            quoteParts.add(QuoteElement(quoteParagraphBuffer))
            quoteParagraphBuffer = ""
        }
    }

    private fun addQuote(element: Element, tag: String?) {
        if (quoteParagraphBuffer.isNotEmpty()) addQuotePart()
        if (quoteParts.isEmpty()) return

        publicationParts.add(QuotePublicationElement(quoteParts))
    }

    //endregion
}