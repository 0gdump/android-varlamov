package open.v0gdump.varlamov.presentation.reader

import android.app.Application
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import moxy.InjectViewState
import open.v0gdump.recontent.ReContent
import open.v0gdump.recontent.ReContentEvents
import open.v0gdump.recontent.model.NodeRule
import open.v0gdump.recontent.model.SectionRule
import open.v0gdump.recontent.model.SpecificNodesHandler
import open.v0gdump.varlamov.presentation.global.Contentator
import open.v0gdump.varlamov.presentation.global.MvpPresenterX
import org.jsoup.nodes.Element
import org.jsoup.nodes.TextNode

@InjectViewState
class ReaderScreenPresenter(
    private val application: Application
) : MvpPresenterX<ReaderScreenView>() {

    private val contentator = Contentator.Store<PublicationElement>()

    var url: String? = null

    init {
        contentator.render = { viewState.renderState(it) }
        launch { sideEffectsConsumer() }
    }

    private suspend fun sideEffectsConsumer() {
        contentator.sideEffects.consumeEach { effect ->
            when (effect) {
                is Contentator.SideEffect.LoadData -> {
                    loadData()
                }
                is Contentator.SideEffect.ErrorEvent -> {
                    viewState.showMessage(effect.error.message.orEmpty())
                }
            }
        }
    }

    private fun loadData() {

        if (url == null) return

        val eventsHandler = ReContentEvents(
            //onError = {
            // TODO(CODE) Show error
            //},
            afterParse = {
                // TODO(CODE) Show content
            }
        )

        ReContent(application, eventsHandler).apply {
            sectionsRules = createRules()
            load(url!!)
        }
    }

    // TODO(CODE) Check rules, create new!
    private fun createRules(): List<SectionRule> = listOf(
        SectionRule(
            selector = "div#entrytext.j-e-text",
            childRules = listOf(
                NodeRule(
                    selector = "span.j-imagewrapper",
                    callback = { e, t -> buildImage(e, t) }
                )
            ),
            specificNodesHandler = SpecificNodesHandler(
                textNodeHandler = { node -> buildText(node) }
            )
        )
    )

    //region ReContent Callbacks

    // TODO(CODE) Append to intermediate list

    private fun buildImage(element: Element, tag: String?) {
        val source = element.select("img").attr("src")

        //data.add(ItemImage(source = source))
    }

    private fun buildText(node: TextNode) {
        //data.add(ItemText(content = node.text()))
    }

    //endregion

    fun refresh() = contentator.proceed(Contentator.Action.Refresh)
}