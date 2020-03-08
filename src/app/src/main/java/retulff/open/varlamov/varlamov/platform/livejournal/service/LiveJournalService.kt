package retulff.open.varlamov.varlamov.platform.livejournal.service

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.POST

interface LiveJournalService {

    @POST("interface/xmlrpc")
    fun executeMethod(@Body xmlBody: RequestBody): Call<ResponseBody>

    companion object {

        private const val BASE_URL = "https://www.livejournal.com/"

        fun create(): LiveJournalService {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .build()

            return retrofit.create<LiveJournalService>(LiveJournalService::class.java)
        }
    }
}