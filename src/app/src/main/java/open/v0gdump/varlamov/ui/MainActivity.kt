package open.v0gdump.varlamov.ui

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.MenuItem
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import moxy.MvpAppCompatActivity
import open.v0gdump.varlamov.R
import org.joda.time.DateTime
import org.joda.time.LocalDate


class MainActivity : MvpAppCompatActivity() {

    private val navController by lazy {
        Navigation.findNavController(
            findViewById(R.id.navigation_container)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onMenuItemSelected(item: MenuItem): Boolean {
        val localDate = LocalDate()
        
        DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { _, y, m, d -> showByDateScreen(y, m, d) },
            localDate.year, localDate.monthOfYear, localDate.dayOfMonth
        ).show()

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
