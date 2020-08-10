package com.varlamov.android.ui

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.MenuItem
import com.varlamov.android.App
import com.varlamov.android.R
import com.varlamov.android.Screens
import moxy.MvpAppCompatActivity
import org.joda.time.DateTime
import org.joda.time.LocalDate
import ru.terrakok.cicerone.android.support.SupportAppNavigator
import java.util.*

class MainActivity : MvpAppCompatActivity() {

    private val navigator = SupportAppNavigator(this, R.id.navigationContainer)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        App.router.navigateTo(Screens.MainFlow)
    }

    override fun onResume() {
        super.onResume()
        App.navigationHolder.setNavigator(navigator)
    }

    override fun onPause() {
        super.onPause()
        App.navigationHolder.removeNavigator()
    }

    override fun onBackPressed() {
        App.router.exit()
    }

    fun onMenuItemSelected(item: MenuItem): Boolean {
        val localDate = LocalDate()

        val datePickerDialog = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { _, y, m, d -> showByDateScreen(y, m + 1, d) },
            localDate.year, localDate.monthOfYear - 1, localDate.dayOfMonth
        )
        datePickerDialog.datePicker.maxDate = Date().time
        datePickerDialog.show()

        return true
    }

    private fun showByDateScreen(year: Int, month: Int, day: Int) {
        val timestamp = DateTime(year, month, day, 0, 0).millis
        App.router.navigateTo(Screens.ByDateScreen(timestamp))
    }

    fun navigateTo(destination: Int, bundle: Bundle) {
        //navController.navigate(destination, bundle)
    }
}
