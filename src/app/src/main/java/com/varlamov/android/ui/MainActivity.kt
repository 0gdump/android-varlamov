package com.varlamov.android.ui

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.MenuItem
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import com.varlamov.android.R
import moxy.MvpAppCompatActivity
import org.joda.time.DateTime
import org.joda.time.LocalDate
import java.util.*


class MainActivity : MvpAppCompatActivity() {

    private val navController by lazy {
        Navigation.findNavController(
            findViewById(R.id.navigationContainer)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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

        navigateTo(
            R.id.by_date_screen,
            bundleOf("dateTimestamp" to timestamp)
        )
    }

    fun navigateTo(destination: Int, bundle: Bundle) {
        navController.navigate(destination, bundle)
    }
}
