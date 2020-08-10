package com.varlamov.android

import androidx.fragment.app.Fragment
import com.varlamov.android.model.platform.livejournal.model.Publication
import com.varlamov.android.ui.main.MainScreen
import ru.terrakok.cicerone.android.support.SupportAppScreen

object Screens {

    object MainFlow : SupportAppScreen() {
        override fun getFragment() = MainScreen()
    }

    data class ByDateScreen(
        val dateTimestamp: Long
    ) : SupportAppScreen() {
        override fun getFragment() =
            com.varlamov.android.ui.date.ByDateScreen.create(dateTimestamp)
    }

    data class ByTagScreen(
        val tag: String
    ) : SupportAppScreen() {
        override fun getFragment() =
            com.varlamov.android.ui.tag.ByTagScreen.create(tag)
    }

    data class ReaderScreen(
        val publication: Publication
    ) : SupportAppScreen() {
        override fun getFragment(): Fragment =
            com.varlamov.android.ui.reader.ReaderScreen.create(publication)
    }
}