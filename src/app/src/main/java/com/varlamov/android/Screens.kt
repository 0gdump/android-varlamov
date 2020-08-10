package com.varlamov.android

import com.varlamov.android.ui.main.MainScreen
import ru.terrakok.cicerone.android.support.SupportAppScreen

object Screens {

    object MainFlow : SupportAppScreen() {
        override fun getFragment() = MainScreen()
    }
}