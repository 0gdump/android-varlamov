package open.v0gdump.varlamov.model.repository.all

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import open.v0gdump.varlamov.model.platform.livejournal.model.Publication
import org.joda.time.DateTime
import java.util.concurrent.Executor

class PublicationsDataSourceFactory(private val retryExecutor: Executor) :
    DataSource.Factory<DateTime, Publication>() {

    val sourceLiveData = MutableLiveData<ItemKeyedPublicationsDataSource>()

    override fun create(): DataSource<DateTime, Publication> {

        val source =
            ItemKeyedPublicationsDataSource(retryExecutor)
        sourceLiveData.postValue(source)

        return source
    }
}
