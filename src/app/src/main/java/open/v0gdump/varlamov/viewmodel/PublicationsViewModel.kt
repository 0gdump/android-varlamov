package open.v0gdump.varlamov.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import open.v0gdump.varlamov.varlamov.platform.livejournal.model.Publication
import open.v0gdump.varlamov.varlamov.repository.Listing
import open.v0gdump.varlamov.varlamov.repository.PublicationsRepository


class PublicationsViewModel(private val repository: PublicationsRepository) : ViewModel() {

    private val repoResult = MutableLiveData<Listing<Publication>>()

    val posts = Transformations.switchMap(repoResult) { it.pagedList }
    val networkState = Transformations.switchMap(repoResult) { it.networkState }
    val refreshState = Transformations.switchMap(repoResult) { it.refreshState }

    fun init() {
        repoResult.value = repository.posts(10)
    }

    fun refresh() {
        repoResult.value?.refresh?.invoke()
    }

    fun retry() {
        val listing = repoResult.value
        listing?.retry?.invoke()
    }
}
