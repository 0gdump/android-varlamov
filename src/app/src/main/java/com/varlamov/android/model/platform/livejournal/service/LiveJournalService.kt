package com.varlamov.android.model.platform.livejournal.service

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

// TODO(CODE) Use new json api
interface LiveJournalService {

    @Headers("Content-Type: text/xml")
    @POST("interface/xmlrpc")
    fun executeMethod(@Body xmlBody: String): Call<ResponseBody>

    companion object {

        private const val BASE_URL = "https://www.livejournal.com/"

        fun create(): LiveJournalService {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build()

            return retrofit.create(LiveJournalService::class.java)
        }
    }
}