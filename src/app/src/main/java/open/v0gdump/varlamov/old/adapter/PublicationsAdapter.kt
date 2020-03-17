package open.v0gdump.varlamov.old.adapter

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.RecyclerView
import open.v0gdump.varlamov.R
import open.v0gdump.varlamov.old.viewholder.NetworkStateItemViewHolder
import open.v0gdump.varlamov.old.viewholder.PublicationViewHolder
import open.v0gdump.varlamov.model.platform.livejournal.model.Publication
import open.v0gdump.varlamov.model.repository.NetworkState
import open.v0gdump.varlamov.model.util.PUBLICATIONS_COMPARATOR

class PublicationsAdapter(
    private val retryCallback: () -> Unit,
    private val primaryTag: String?
) : PagedListAdapter<Publication, RecyclerView.ViewHolder>(PUBLICATIONS_COMPARATOR) {

    private var networkState: NetworkState? = null

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {

            // Контент
            R.layout.item_text_and_image_post ->
                (holder as PublicationViewHolder).bindTo(getItem(position)!!)

            // Индикатор загрузки
            R.layout.item_network_state ->
                (holder as NetworkStateItemViewHolder).bindTo(networkState)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {

            // Контент
            R.layout.item_text_and_image_post ->
                PublicationViewHolder.create(parent, primaryTag)

            // Индикатор загрузки
            R.layout.item_network_state ->
                NetworkStateItemViewHolder.create(parent, retryCallback)

            else ->
                throw IllegalArgumentException("Unknown view type $viewType")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            R.layout.item_network_state
        } else {
            R.layout.item_text_and_image_post
        }
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasExtraRow()) 1 else 0
    }

    private fun hasExtraRow() = networkState != null && networkState != NetworkState.LOADED

    fun setNetworkState(newNetworkState: NetworkState?) {

        val previousState = this.networkState
        val hadExtraRow = hasExtraRow()

        this.networkState = newNetworkState
        val hasExtraRow = hasExtraRow()

        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) {
                notifyItemRemoved(super.getItemCount())
            } else {
                notifyItemInserted(super.getItemCount())
            }
        } else if (hasExtraRow && previousState != newNetworkState) {
            notifyItemChanged(itemCount - 1)
        }
    }
}
