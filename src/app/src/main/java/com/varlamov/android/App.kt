package com.varlamov.android

import android.app.Application
import android.content.res.Resources
import com.varlamov.android.util.kotlin.InitOnceProperty

class App : Application() {

    companion object {

        var instance: App by InitOnceProperty()
            private set

        var res: Resources by InitOnceProperty()
            private set
    }

    override fun onCreate() {
        super.onCreate()

        instance = this
        res = resources
    }
}