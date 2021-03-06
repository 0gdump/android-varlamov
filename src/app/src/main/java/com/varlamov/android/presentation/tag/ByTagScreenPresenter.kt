package com.varlamov.android.presentation.tag

import android.util.Log
import com.varlamov.android.App
import com.varlamov.android.Screens
import com.varlamov.android.model.Blog
import com.varlamov.android.model.platform.livejournal.model.Publication
import com.varlamov.android.model.platform.livejournal.model.PublicationsFactory
import com.varlamov.android.presentation.global.MvpPresenterX
import com.varlamov.android.presentation.global.Paginator
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import moxy.InjectViewState
import okhttp3.ResponseBody
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import retrofit2.Call
import retrofit2.Response

@InjectViewState
class ByTagScreenPresenter(
    private val publicationsTag: String
) : MvpPresenterX<ByTagScreenView>() {

    private val paginator = Paginator.Store<Publication>()

    init {
        paginator.render = {
            viewState.renderPaginatorState(it)
        }
        launch {
            paginator.sideEffects.consumeEach { effect ->
                when (effect) {
                    is Paginator.SideEffect.LoadPage -> {
                        loadNewPage(effect.currentPage, effect.currentPageLastItem as Publication?)
                    }
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

    private fun loadNewPage(page: Int, lastItem: Publication?) {

        Log.d("varlamov", "BOOOL")

        val beforeDate =
            lastItem?.time?.withZone(DateTimeZone.UTC) ?: DateTime.now().withZone(DateTimeZone.UTC)

        Blog.getPublicationsByTag(
            publicationsTag,
            20,
            beforeDate,
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

                    paginator.proceed(Paginator.Action.NewPage(page + 1, publications))
                }
            }
        )
    }

    fun refresh() = paginator.proceed(Paginator.Action.Refresh)
    fun loadMore() = paginator.proceed(Paginator.Action.LoadMore)

    fun onPublicationClicked(publication: Publication) {
        App.router.navigateTo(Screens.ReaderScreen(publication))
    }
}