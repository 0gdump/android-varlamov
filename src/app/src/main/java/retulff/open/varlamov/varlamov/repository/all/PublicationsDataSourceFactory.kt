package retulff.open.varlamov.varlamov.repository.all

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import org.joda.time.DateTime
import retulff.open.varlamov.varlamov.platform.livejournal.model.Publication
import java.util.concurrent.Executor

class PublicationsDataSourceFactory(private val retryExecutor: Executor) : DataSource.Factory<DateTime, Publication>() {

    val sourceLiveData = MutableLiveData<ItemKeyedPublicationsDataSource>()

    override fun create(): DataSource<DateTime, Publication> {

        val source =
            ItemKeyedPublicationsDataSource(retryExecutor)
        sourceLiveData.postValue(source)

        return source
    }
}
