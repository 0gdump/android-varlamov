package retulff.open.varlamov.varlamov.repository

import retulff.open.varlamov.varlamov.platform.livejournal.model.Publication

interface PublicationsRepository {

    fun posts(items: Int): Listing<Publication>

    fun postsByTag(tag: String, items: Int): Listing<Publication>
}