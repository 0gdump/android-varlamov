package com.varlamov.android

import android.app.Application
import android.content.res.Resources
import com.varlamov.android.util.kotlin.initOnce
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.Router

class App : Application() {

    companion object {

        var instance: App by initOnce()
            private set

        var res: Resources by initOnce()
            private set

        private var cicerone: Cicerone<Router> by initOnce()

        val navigationHolder
            get() = cicerone.navigatorHolder

        val router
            get() = cicerone.router
    }

    override fun onCreate() {
        super.onCreate()

        instance = this
        res = resources
        cicerone = Cicerone.create()
    }
}