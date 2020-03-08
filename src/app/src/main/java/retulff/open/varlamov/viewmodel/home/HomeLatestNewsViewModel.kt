package retulff.open.varlamov.viewmodel.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import okhttp3.ResponseBody
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retulff.open.varlamov.App
import retulff.open.varlamov.R
import retulff.open.varlamov.varlamov.Blog
import retulff.open.varlamov.varlamov.platform.livejournal.model.Publication
import retulff.open.varlamov.varlamov.platform.livejournal.model.PublicationsFactory

class HomeLatestNewsViewModel : ViewModel() {

    private val latestNews = MutableLiveData<List<Publication>?>()

    fun getLatestNews(): LiveData<List<Publication>?> = latestNews

    fun loadData() {
        Blog.getPublicationsByTag(
            App.res.getString(R.string.news_tag),
            5,
            DateTime.now().withZone(DateTimeZone.UTC),
            object : Callback<ResponseBody> {

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    latestNews.value = null
                }

                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {

                        //val rawResponse = Jsoup.parse(response.body()?.string(), "", Parser.xmlParser())
                        val publications = PublicationsFactory.convert(response.body()!!.string())

                        latestNews.value = publications

                    } else {
                        latestNews.value = null
                    }
                }

            })
    }
}