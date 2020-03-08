package retulff.open.varlamov.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat.startActivity

fun openUrlInBrowser(ctx: Context, url: String) {

    val i = Intent(
        Intent.ACTION_VIEW,
        Uri.parse(url)
    )

    startActivity(ctx, i, null)
}