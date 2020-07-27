package open.v0gdump.varlamov.presentation.reader

import android.app.Application
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import moxy.InjectViewState
import open.v0gdump.recontent.ReContent
import open.v0gdump.recontent.ReContentEvents
import open.v0gdump.recontent.model.SectionRule
import open.v0gdump.recontent.model.SpecificNodesHandler
import open.v0gdump.varlamov.model.platform.livejournal.model.Publication
import open.v0gdump.varlamov.model.reader.PublicationElement
import open.v0gdump.varlamov.model.reader.TextPublicationElement
import open.v0gdump.varlamov.presentation.global.Contentator
import open.v0gdump.varlamov.presentation.global.MvpPresenterX

@InjectViewState
class ReaderScreenPresenter(
    private val application: Application
) : MvpPresenterX<ReaderScreenView>() {

    var publication: Publication? = null

    private val contentator = Contentator.Store<PublicationElement>()
    private val eventsHandler = ReContentEvents(
        onError = { throwable ->
            contentator.proceed(Contentator.Action.Error(throwable))
        },
        afterParse = {
            contentator.proceed(Contentator.Action.Data(publicationParts))
        }
    )

    // TODO
    private val recontentRules = listOf(
        SectionRule(
            selector = "div#entrytext.j-e-text",
            childRules = emptyList(),
            specificNodesHandler = SpecificNodesHandler(
                textNodeHandler = { node ->
                    val text = node.text()
                    if (text.isNotBlank()) {
                        publicationParts.add(TextPublicationElement(text))
                    }
                }
            )
        )
    )

    private val publicationParts = mutableListOf<PublicationElement>()

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

    private fun runReContent() {
        check(publication != null) { "No publication in presenter" }
        ReContent(application, eventsHandler).apply {
            sectionsRules = recontentRules
            load(publication!!.url)
        }
    }

    fun refresh() = contentator.proceed(Contentator.Action.Refresh)
    fun load() = contentator.proceed(Contentator.Action.LoadData)
}