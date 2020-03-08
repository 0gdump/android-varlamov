package retulff.open.varlamov.ui.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_network_state.view.*
import retulff.open.varlamov.R
import retulff.open.varlamov.varlamov.repository.NetworkState
import retulff.open.varlamov.varlamov.repository.Status.FAILED
import retulff.open.varlamov.varlamov.repository.Status.RUNNING

class NetworkStateItemViewHolder(
    private val view: View,
    private val retryCallback: () -> Unit
) : RecyclerView.ViewHolder(view) {

    init {
        view.item_network_state_retry.setOnClickListener {
            retryCallback()
        }
    }

    companion object {

        fun create(parent: ViewGroup, retryCallback: () -> Unit): NetworkStateItemViewHolder {

            val view = LayoutInflater
                .from(parent.context)
                .inflate(R.layout.item_network_state, parent, false)

            return NetworkStateItemViewHolder(view, retryCallback)
        }

        fun toVisibility(constraint: Boolean): Int {
            return if (constraint) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }
    }

    fun bindTo(networkState: NetworkState?) {

        // Progress bar
        view.item_network_state_progress.visibility = toVisibility(networkState?.status == RUNNING)

        // Retry button
        view.item_network_state_retry.visibility = toVisibility(networkState?.status == FAILED)

        // Error message
        view.item_network_state_error_message.visibility = toVisibility(networkState?.msg != null)
        view.item_network_state_error_message.text = networkState?.msg
    }
}
