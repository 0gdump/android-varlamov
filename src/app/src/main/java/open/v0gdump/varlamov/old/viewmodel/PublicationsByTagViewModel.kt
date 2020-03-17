package open.v0gdump.varlamov.old.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations.map
import androidx.lifecycle.Transformations.switchMap
import androidx.lifecycle.ViewModel
import open.v0gdump.varlamov.model.repository.PublicationsRepository


class PublicationsByTagViewModel(private val repository: PublicationsRepository) : ViewModel() {

    private val currentTag = MutableLiveData<String>()
    private val repoResult = map(currentTag) {
        repository.postsByTag(it, 10)
    }

    val posts = switchMap(repoResult) { it.pagedList }
    val networkState = switchMap(repoResult) { it.networkState }
    val refreshState = switchMap(repoResult) { it.refreshState }

    fun showByTag(newTag: String): Boolean {

        if (currentTag.value == newTag) return false

        currentTag.postValue(newTag)
        return true
    }

    fun getCurrentTag(): String? = currentTag.value

    fun refresh() {
        repoResult.value?.refresh?.invoke()
    }

    fun retry() {
        val listing = repoResult.value
        listing?.retry?.invoke()
    }
}
