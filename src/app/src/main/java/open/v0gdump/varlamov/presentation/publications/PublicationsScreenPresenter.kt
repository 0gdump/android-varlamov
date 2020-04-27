package open.v0gdump.varlamov.presentation.publications

import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import moxy.InjectViewState
import okhttp3.ResponseBody
import open.v0gdump.varlamov.model.Blog
import open.v0gdump.varlamov.model.platform.livejournal.model.Publication
import open.v0gdump.varlamov.model.platform.livejournal.model.PublicationsFactory
import open.v0gdump.varlamov.presentation.global.MvpPresenterX
import open.v0gdump.varlamov.presentation.global.Paginator
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import retrofit2.Call
import retrofit2.Response

@InjectViewState
class PublicationsScreenPresenter : MvpPresenterX<PublicationsScreenView>() {

    private val paginator = Paginator.Store<Publication>()
    private var pageJob: Job? = null

    init {
        paginator.render = { viewState.renderPaginatorState(it) }
        launch {
            paginator.sideEffects.consumeEach { effect ->
                when (effect) {
                    is Paginator.SideEffect.LoadPage -> loadNewPage(effect.currentPage)
                    is Paginator.SideEffect.ErrorEvent -> {
                        viewState.showMessage(effect.error.message.orEmpty())
                    }
                }
            }
        }
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        refresh()
    }

    private fun loadNewPage(page: Int) {
        if (page != 1) {
            paginator.proceed(Paginator.Action.NewPage(page, listOf<Publication>()))
            return
        }

        Blog.getPublications(
            10,
            DateTime.now().withZone(DateTimeZone.UTC),
            object : retrofit2.Callback<ResponseBody> {
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    paginator.proceed(Paginator.Action.PageError(t))
                }

                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (!response.isSuccessful || response.body() == null) {
                        paginator.proceed(Paginator.Action.PageError(Throwable()))
                        return
                    }

                    val responseBody = response.body()!!.string()
                    val publications = PublicationsFactory.convert(responseBody)

                    paginator.proceed(Paginator.Action.NewPage(0, publications))
                }
            }
        )
    }

    fun refresh() = paginator.proceed(Paginator.Action.Refresh)
    fun loadMore() = paginator.proceed(Paginator.Action.LoadMore)

    fun onPublicationClicked(publication: Publication) {}
}