package open.v0gdump.varlamov.varlamov.repository.all

import androidx.lifecycle.MutableLiveData
import androidx.paging.ItemKeyedDataSource
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import open.v0gdump.varlamov.varlamov.Blog
import open.v0gdump.varlamov.varlamov.platform.livejournal.model.Publication
import open.v0gdump.varlamov.varlamov.platform.livejournal.model.PublicationsFactory
import open.v0gdump.varlamov.varlamov.repository.NetworkState
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import retrofit2.Call
import retrofit2.Response
import java.util.concurrent.Executor

class ItemKeyedPublicationsDataSource(
    private val retryExecutor: Executor
) : ItemKeyedDataSource<DateTime, Publication>() {

    private var retry: (() -> Any)? = null

    val networkState = MutableLiveData<NetworkState>()
    val initialLoad = MutableLiveData<NetworkState>()

    fun retryAllFailed() {

        val prevRetry = retry

        retry = null

        prevRetry?.let {
            retryExecutor.execute {
                it.invoke()
            }
        }
    }

    override fun loadBefore(params: LoadParams<DateTime>, callback: LoadCallback<Publication>) {

        // Ignored, since we only ever append to our initial load
    }

    override fun loadAfter(params: LoadParams<DateTime>, callback: LoadCallback<Publication>) {

        networkState.postValue(NetworkState.LOADING)

        GlobalScope.launch {
            Blog.getPublications(
                count = params.requestedLoadSize,
                before = params.key,
                callback = object : retrofit2.Callback<ResponseBody> {

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {

                        retry = {
                            loadAfter(params, callback)
                        }
                        networkState.postValue(
                            NetworkState.error(
                                t.message ?: "Unknown error"
                            )
                        )
                    }

                    override fun onResponse(
                        call: Call<ResponseBody>,
                        response: Response<ResponseBody>
                    ) {

                        if (response.isSuccessful) {

                            val publications =
                                PublicationsFactory.convert(response.body()!!.string())

                            retry = null

                            callback.onResult(publications)
                            networkState.postValue(NetworkState.LOADED)

                        } else {

                            retry = {
                                loadAfter(params, callback)
                            }

                            networkState.postValue(
                                NetworkState.error("Error code: ${response.code()}")
                            )
                        }
                    }
                })
        }
    }

    override fun loadInitial(
        params: LoadInitialParams<DateTime>,
        callback: LoadInitialCallback<Publication>
    ) {

        networkState.postValue(NetworkState.LOADING)
        initialLoad.postValue(NetworkState.LOADING)

        GlobalScope.launch {
            Blog.getPublications(
                count = params.requestedLoadSize,
                before = DateTime.now().withZone(DateTimeZone.UTC),
                callback = object : retrofit2.Callback<ResponseBody> {

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {

                        retry = {
                            loadInitial(params, callback)
                        }

                        val error = NetworkState.error(
                            t.message ?: "unknown error"
                        )

                        networkState.postValue(error)
                        initialLoad.postValue(error)
                    }

                    override fun onResponse(
                        call: Call<ResponseBody>,
                        response: Response<ResponseBody>
                    ) {

                        if (response.isSuccessful) {

                            //val rawResponse = Jsoup.parse(response.body()?.string(), "", Parser.xmlParser())
                            val publications =
                                PublicationsFactory.convert(response.body()!!.string())

                            retry = null

                            networkState.postValue(NetworkState.LOADED)
                            initialLoad.postValue(NetworkState.LOADED)

                            callback.onResult(publications)

                        } else {

                            retry = {
                                loadInitial(params, callback)
                            }

                            val error =
                                NetworkState.error("Error code: ${response.code()}")

                            networkState.postValue(error)
                            initialLoad.postValue(error)
                        }
                    }
                })
        }
    }

    override fun getKey(item: Publication): DateTime = item.time
}