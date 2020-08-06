package com.varlamov.android.util.android

import com.gturedi.views.CustomStateOptions
import com.gturedi.views.StatefulLayout
import com.varlamov.android.R

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
        options.buttonClickListener({
            clickListener.invoke()
        })
    }

    showCustom(options)
}
