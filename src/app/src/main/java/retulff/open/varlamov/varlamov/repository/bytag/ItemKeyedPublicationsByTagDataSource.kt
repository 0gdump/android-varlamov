package retulff.open.varlamov.varlamov.repository.bytag

import androidx.lifecycle.MutableLiveData
import androidx.paging.ItemKeyedDataSource
import okhttp3.ResponseBody
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import retrofit2.Call
import retrofit2.Response
import retulff.open.varlamov.varlamov.Blog
import retulff.open.varlamov.varlamov.platform.livejournal.model.Publication
import retulff.open.varlamov.varlamov.platform.livejournal.model.PublicationsFactory
import retulff.open.varlamov.varlamov.repository.NetworkState
import java.util.concurrent.Executor

class ItemKeyedPublicationsByTagDataSource(
    private val tag: String,
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

        Blog.getPublicationsByTag(
            tag = tag,
            items = params.requestedLoadSize,
            beforeDate = params.key,
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

                        val publications = PublicationsFactory.convert(response.body()!!.string())

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

    override fun loadInitial(params: LoadInitialParams<DateTime>, callback: LoadInitialCallback<Publication>) {

        networkState.postValue(NetworkState.LOADING)
        initialLoad.postValue(NetworkState.LOADING)

        Blog.getPublicationsByTag(
            tag = tag,
            items = params.requestedLoadSize,
            beforeDate = DateTime.now().withZone(DateTimeZone.UTC),
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

                        val publications = PublicationsFactory.convert(response.body()!!.string())

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

    override fun getKey(item: Publication): DateTime = item.time
}