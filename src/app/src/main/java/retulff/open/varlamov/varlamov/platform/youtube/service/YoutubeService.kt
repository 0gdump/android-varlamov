package retulff.open.varlamov.varlamov.platform.youtube.service

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import retulff.open.varlamov.varlamov.platform.youtube.model.VideosRequestResponse

interface YoutubeService {

    @GET("search?part=snippet,id&order=date")
    fun getLatestVideo(
        @Query("key") authKey: String,
        @Query("channelId") channelId: String,
        @Query("maxResults") maxResults: Int
    ): Call<VideosRequestResponse>

    companion object {

        private const val BASE_URL = "https://www.googleapis.com/youtube/v3/"

        fun create(): YoutubeService {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .build()

            return retrofit.create<YoutubeService>(YoutubeService::class.java)
        }
    }
}