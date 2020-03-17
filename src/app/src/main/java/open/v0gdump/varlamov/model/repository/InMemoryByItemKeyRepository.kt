package open.v0gdump.varlamov.model.repository

import androidx.annotation.MainThread
import androidx.lifecycle.Transformations
import androidx.paging.Config
import androidx.paging.toLiveData
import open.v0gdump.varlamov.model.platform.livejournal.model.Publication
import open.v0gdump.varlamov.model.repository.all.PublicationsDataSourceFactory
import open.v0gdump.varlamov.model.repository.bytag.PublicationsByTagDataSourceFactory
import java.util.concurrent.Executor

class InMemoryByItemRepository(private val networkExecutor: Executor) : PublicationsRepository {

    @MainThread
    override fun posts(items: Int): Listing<Publication> {

        val sourceFactory =
            PublicationsDataSourceFactory(networkExecutor)

        val livePagedList = sourceFactory.toLiveData(
            config = Config(
                pageSize = items,
                enablePlaceholders = false,
                initialLoadSizeHint = items * 2
            ),
            fetchExecutor = networkExecutor
        )

        val refreshState = Transformations.switchMap(sourceFactory.sourceLiveData) {
            it.initialLoad
        }

        return Listing(
            pagedList = livePagedList,
            networkState = Transformations.switchMap(sourceFactory.sourceLiveData) {
                it.networkState
            },
            retry = {
                sourceFactory.sourceLiveData.value?.retryAllFailed()
            },
            refresh = {
                sourceFactory.sourceLiveData.value?.invalidate()
            },
            refreshState = refreshState
        )
    }

    @MainThread
    override fun postsByTag(tag: String, items: Int): Listing<Publication> {

        val sourceFactory =
            PublicationsByTagDataSourceFactory(tag, networkExecutor)

        val livePagedList = sourceFactory.toLiveData(
            config = Config(
                pageSize = items,
                enablePlaceholders = false,
                initialLoadSizeHint = items * 2
            ),
            fetchExecutor = networkExecutor
        )

        val refreshState = Transformations.switchMap(sourceFactory.sourceLiveData) {
            it.initialLoad
        }

        return Listing(
            pagedList = livePagedList,
            networkState = Transformations.switchMap(sourceFactory.sourceLiveData) {
                it.networkState
            },
            retry = {
                sourceFactory.sourceLiveData.value?.retryAllFailed()
            },
            refresh = {
                sourceFactory.sourceLiveData.value?.invalidate()
            },
            refreshState = refreshState
        )
    }
}

