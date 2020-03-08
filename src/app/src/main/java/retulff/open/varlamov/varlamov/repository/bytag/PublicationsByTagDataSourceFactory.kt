package retulff.open.varlamov.varlamov.repository.bytag

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import org.joda.time.DateTime
import retulff.open.varlamov.varlamov.platform.livejournal.model.Publication
import java.util.concurrent.Executor

class PublicationsByTagDataSourceFactory(
    private val tag: String,
    private val retryExecutor: Executor
) : DataSource.Factory<DateTime, Publication>() {

    val sourceLiveData = MutableLiveData<ItemKeyedPublicationsByTagDataSource>()

    override fun create(): DataSource<DateTime, Publication> {

        val source = ItemKeyedPublicationsByTagDataSource(tag, retryExecutor)

        sourceLiveData.postValue(source)

        return source
    }
}
