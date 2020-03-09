package retulff.open.varlamov.ui.fragment

import okhttp3.ResponseBody
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retulff.open.varlamov.varlamov.Blog
import retulff.open.varlamov.varlamov.platform.livejournal.model.PublicationsFactory

class HomePresenterImpl : HomePresenter() {

    override fun loadLatestPost() {

        val utcNow = DateTime.now().withZone(DateTimeZone.UTC)
        val callback = object : Callback<ResponseBody> {

            override fun onFailure(call: Call<ResponseBody>, e: Throwable) {
                viewState.showErrorWhileLoadingLatestPost()
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (!response.isSuccessful) {
                    viewState.showErrorWhileLoadingLatestPost()
                }

                val responseBody = response.body()!!.string()
                val posts = PublicationsFactory.convert(responseBody)

                if (posts.isEmpty()) {
                    viewState.showErrorWhileLoadingLatestPost()
                }

                viewState.showLatestPost(posts.first())
            }
        }

        Blog.getPublications(1, utcNow, callback)
    }

    override fun loadLatestVideos() {
        // TODO
    }

}