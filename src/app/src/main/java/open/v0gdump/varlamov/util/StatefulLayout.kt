package open.v0gdump.varlamov.util

import android.view.View
import com.gturedi.views.CustomStateOptions
import com.gturedi.views.StatefulLayout
import open.v0gdump.varlamov.R

fun StatefulLayout.showBeautifulError(
    message: String,
    buttonText: String,
    clickListener: (() -> Unit)?
) {
    val options = CustomStateOptions()
        .message(message)
        .image(R.drawable.ic_error)
        .buttonText(buttonText)

    if (clickListener != null) {
        options.buttonClickListener(View.OnClickListener {
            clickListener.invoke()
        })
    }

    showCustom(options)
}
