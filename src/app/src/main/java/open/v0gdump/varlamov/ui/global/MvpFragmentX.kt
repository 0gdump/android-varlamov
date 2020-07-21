package open.v0gdump.varlamov.ui.global

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import moxy.MvpAppCompatFragment
import open.v0gdump.varlamov.App
import open.v0gdump.varlamov.ui.MainActivity

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
        setupLayout()
        return layout
    }

    protected abstract fun setupLayout()

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