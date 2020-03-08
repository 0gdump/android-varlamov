package retulff.open.varlamov.viewmodel.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retulff.open.varlamov.varlamov.YoutubeChannel
import retulff.open.varlamov.varlamov.platform.youtube.model.Video
import retulff.open.varlamov.varlamov.platform.youtube.model.VideosRequestResponse

class HomeCurrentVideosViewModel : ViewModel() {

    private val currentVideos = MutableLiveData<List<Video>?>()

    fun getCurrentVideos(): LiveData<List<Video>?> = currentVideos

    fun loadData() {
        YoutubeChannel.getLatestVideos(5, object : Callback<VideosRequestResponse> {

            override fun onFailure(call: Call<VideosRequestResponse>, t: Throwable) {
                currentVideos.value = null
            }

            override fun onResponse(call: Call<VideosRequestResponse>, response: Response<VideosRequestResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        currentVideos.value = it.items
                    }
                } else {
                    currentVideos.value = null
                }
            }
        })
    }
}