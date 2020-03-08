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
import retulff.open.varlamov.varlamov.Blog
import retulff.open.varlamov.varlamov.platform.livejournal.model.Publication
import retulff.open.varlamov.varlamov.platform.livejournal.model.factory.PublicationsResponseFactoryManager

class HomeRecentPublicationViewModel : ViewModel() {

    private val recentPublication = MutableLiveData<Publication?>()

    fun getRecentPublication(): LiveData<Publication?> = recentPublication

    fun loadData() {
        Blog.getPublications(1, DateTime.now().withZone(DateTimeZone.UTC), object : Callback<ResponseBody> {

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                recentPublication.value = null
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {

                    val publications = PublicationsResponseFactoryManager.convert(response.body()!!.string())

                   recentPublication.value = publications[0]

                } else {
                    recentPublication.value = null
                }
            }

        })
    }
}