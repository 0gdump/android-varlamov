package open.v0gdump.varlamov.presentation.date

import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import moxy.InjectViewState
import okhttp3.ResponseBody
import open.v0gdump.varlamov.model.Blog
import open.v0gdump.varlamov.model.platform.livejournal.model.Publication
import open.v0gdump.varlamov.model.platform.livejournal.model.PublicationsFactory
import open.v0gdump.varlamov.presentation.global.MvpPresenterX
import open.v0gdump.varlamov.presentation.global.Paginator
import open.v0gdump.varlamov.util.isSameDay
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import retrofit2.Call
import retrofit2.Response
import java.util.*

@InjectViewState
class ByDateScreenPresenter : MvpPresenterX<ByDateScreenView>() {

    var timestamp: Long = 0L

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

        val day = DateTime(timestamp, DateTimeZone.UTC)

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

    fun onPublicationClicked(publication: Publication) {}
}