package open.v0gdump.varlamov.old.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import open.v0gdump.varlamov.model.platform.livejournal.model.Publication

class ByDateViewModel : ViewModel() {

    private val publicationsByDate = MutableLiveData<List<Publication>>()

    fun getPublicationsByDate(): LiveData<List<Publication>> = publicationsByDate

    fun postPublicationsByDate(items: List<Publication>) {
        publicationsByDate.value = items
    }
}