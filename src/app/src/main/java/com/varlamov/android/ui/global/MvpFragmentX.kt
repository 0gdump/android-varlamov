package com.varlamov.android.ui.global

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import com.varlamov.android.App
import com.varlamov.android.ui.MainActivity
import moxy.MvpAppCompatFragment

abstract class MvpFragmentX(
    private val layoutRes: Int
) : MvpAppCompatFragment() {

    protected lateinit var layout: View

    protected val activity: MainActivity
        get() = super.getActivity() as MainActivity

    protected val app: App
        get() = activity.application as App

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        layout = inflater.inflate(layoutRes, container, false)
        setupLayoutOnCreate()
        return layout
    }

    protected open fun setupLayoutOnCreate() {}

    protected fun unimplemented() {
        Toast.makeText(context, "\uD83D\uDE48 Unimplemented", Toast.LENGTH_LONG).show()
    }

    protected fun finish() {
        activity.supportFragmentManager.popBackStack(
            null,
            FragmentManager.POP_BACK_STACK_INCLUSIVE
        )
    }
}