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
import open.v0gdump.varlamov.model.reader.ImagePublicationElement
import open.v0gdump.varlamov.model.reader.PublicationElement
import open.v0gdump.varlamov.model.reader.TextPublicationElement
import open.v0gdump.varlamov.model.reader.UnimplementedPublicationElement
import open.v0gdump.varlamov.presentation.global.Contentator
import open.v0gdump.varlamov.presentation.global.MvpPresenterX
import org.jsoup.nodes.Element
import org.jsoup.nodes.TextNode

@InjectViewState
class ReaderScreenPresenter(
    private val application: Application
) : MvpPresenterX<ReaderScreenView>() {

    var publication: Publication? = null

    //region ReContent

    private val recontentRules = listOf(
        SectionRule(
            selector = "div#entrytext.j-e-text",
            childRules = listOf(
                NodeRule(selector = "i", callback = ::appendToTextBuffer),
                NodeRule(selector = "a", callback = ::appendToTextBuffer),
                NodeRule(selector = "br", callback = ::addTextPart),
                NodeRule(selector = ".j-imagewrapper", callback = ::addFeedbackImagePart),
                NodeRule(selector = "img", callback = ::addImagePart)
            ),
            specificNodesHandler = SpecificNodesHandler(
                textNodeHandler = ::appendToTextBuffer,
                unmatchedElementHandler = ::addUnimplementedPart
            )
        )
    )

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

        ReContent(application, eventsHandler).apply {
            sectionsRules = recontentRules
            load(publication!!.url)
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

    private fun addFeedbackImagePart(element: Element, tag: String?) {
        val imgs = element.getElementsByTag("img")
        if (imgs.size == 0) {
            return
        }

        // FIXME(CODE) Create feedback (!!!) image part
        addImagePart(imgs.first(), tag)
    }

    private fun addImagePart(element: Element, tag: String?) {
        publicationParts.add(ImagePublicationElement(element.attr("src")))
    }
}