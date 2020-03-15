package open.v0gdump.varlamov.varlamov.repository

import open.v0gdump.varlamov.varlamov.platform.livejournal.model.Publication

interface PublicationsRepository {

    fun posts(items: Int): Listing<Publication>

    fun postsByTag(tag: String, items: Int): Listing<Publication>
}