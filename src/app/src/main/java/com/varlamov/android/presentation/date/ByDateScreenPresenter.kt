package com.varlamov.android.presentation.date

import com.varlamov.android.App
import com.varlamov.android.Screens
import com.varlamov.android.model.Blog
import com.varlamov.android.model.platform.livejournal.model.Publication
import com.varlamov.android.model.platform.livejournal.model.PublicationsFactory
import com.varlamov.android.presentation.global.MvpPresenterX
import com.varlamov.android.presentation.global.Paginator
import com.varlamov.android.util.isSameDay
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import moxy.InjectViewState
import okhttp3.ResponseBody
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import retrofit2.Call
import retrofit2.Response
import java.util.*

@InjectViewState
class ByDateScreenPresenter(dateTimestamp: Long) : MvpPresenterX<ByDateScreenView>() {

    var day = DateTime(dateTimestamp, DateTimeZone.UTC)

    private val paginator = Paginator.Store<Publication>()

    init {
        paginator.render = { viewState.renderPaginatorState(it) }
        launch {
            paginator.sideEffects.consumeEach { effect ->
                when (effect) {
                    is Paginator.SideEffect.LoadPage -> {
                        loadData()
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

    private fun loadData() {
        val callback = object : retrofit2.Callback<ResponseBody> {

            var totalResponses = 0
            var isError = false
            val totalPublications = mutableListOf<Publication>()

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                paginator.proceed(Paginator.Action.PageError(t))
                isError = true
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {

                if (!response.isSuccessful || response.body() == null) {
                    paginator.proceed(Paginator.Action.PageError(Throwable()))
                    isError = true
                    return
                } else if (isError) {
                    return
                }

                totalResponses++

                val responseBody = response.body()!!.string()
                val publications = PublicationsFactory.convert(responseBody)

                totalPublications += publications

                if (totalResponses == 3) {
                    val filteredPublications =
                        totalPublications.filter {
                            val pubTime =
                                DateTime(it.time, DateTimeZone.forTimeZone(TimeZone.getDefault()))
                            val datTime =
                                DateTime(day, DateTimeZone.forTimeZone(TimeZone.getDefault()))
                            pubTime.isSameDay(datTime)
                        }

                    paginator.proceed(Paginator.Action.NewPage(1, filteredPublications))
                }
            }
        }

        Blog.getPublicationsOfDay(500, day.minusDays(1), callback)
        Blog.getPublicationsOfDay(500, day, callback)
        Blog.getPublicationsOfDay(500, day.plusDays(1), callback)
    }

    fun refresh() {
        paginator.proceed(Paginator.Action.Refresh)
    }

    fun onPublicationClicked(publication: Publication) {
        App.router.navigateTo(Screens.ReaderScreen(publication))
    }
}