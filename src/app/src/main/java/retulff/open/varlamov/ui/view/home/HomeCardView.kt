package retulff.open.varlamov.ui.view.home

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.widget.LinearLayout
import androidx.fragment.app.FragmentActivity
import kotlinx.android.synthetic.main.view_home_card.view.*
import retulff.open.varlamov.App
import retulff.open.varlamov.R
import retulff.open.varlamov.ui.view.extension.showBeautifulError

abstract class HomeCardView(
    context: Context,
    attrs: AttributeSet
) : LinearLayout(context, attrs) {

    private lateinit var layout: View

    protected fun getActivity(): FragmentActivity? {
        var context = context

        while (context is ContextWrapper) {
            if (context is Activity) {
                return context as FragmentActivity
            }
            context = context.baseContext
        }

        return null
    }

    protected fun setupCard(title: String, r: Int): View {

        layout = LayoutInflater
            .from(context)
            .inflate(R.layout.view_home_card, this, true)

        layout.title.text = title

        return LayoutInflater
            .from(context)
            .inflate(r, layout.container, true)
    }

    protected fun setMoreClickListener(listener: OnClickListener) {
        layout.more.setOnClickListener(listener)
    }

    abstract fun loadData(forcibly: Boolean = false)

    protected fun showLoading() {
        layout.stateful_container.showLoading(App.res.getString(R.string.state_loading))
    }

    protected fun showError(message: String, retryCallback: () -> Unit) {
        layout.stateful_container.showBeautifulError(
            message,
            App.res.getString(R.string.retry),
            OnClickListener { retryCallback.invoke() }
        )
    }

    protected fun showContent() {
        layout.stateful_container.showContent()
    }
}