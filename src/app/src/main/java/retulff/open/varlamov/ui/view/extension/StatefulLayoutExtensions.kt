package retulff.open.varlamov.ui.view.extension

import android.view.View
import com.gturedi.views.CustomStateOptions
import com.gturedi.views.StatefulLayout
import retulff.open.varlamov.R

fun StatefulLayout.showBeautifulError(message: String, buttonText: String, clickListener: View.OnClickListener) {
    showCustom(
        CustomStateOptions()
            .message(message)
            .image(R.drawable.ic_error)
            .buttonText(buttonText)
            .buttonClickListener(clickListener)
    )
}
