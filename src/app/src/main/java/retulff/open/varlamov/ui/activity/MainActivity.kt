package retulff.open.varlamov.ui.activity

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import kotlinx.android.synthetic.main.activity_main.*
import retulff.open.varlamov.R
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        menuInflater.inflate(R.menu.toolbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        val itemId = item!!.itemId

        return if (itemId == R.id.menu_posts_by_date) {

            startByDateActivity()
            true

        } else super.onOptionsItemSelected(item)
    }

    private fun startByDateActivity() {

        val calendar = Calendar.getInstance()  // Today

        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { _, _year, _month, _day ->

                val intent = Intent(this, ByDateActivity::class.java)

                intent.putExtra("YEAR", _year)
                intent.putExtra("MONTH", _month + 1)
                intent.putExtra("DAY", _day)

                ContextCompat.startActivity(this, intent, null)

            }, year, month, day
        )

        // Нельзя выбирать дату позднее сегодня
        datePickerDialog.datePicker.maxDate = calendar.timeInMillis

        datePickerDialog.show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        enableNavigation()
        setupToolbar()
    }

    private fun enableNavigation() {

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.main_host_fragment) as NavHostFragment?
        NavigationUI.setupWithNavController(main_bottom_navigation, navHostFragment!!.navController)
    }

    private fun setupToolbar() {

        setSupportActionBar(toolbar)
        toolbar_title.text = toolbar.title

        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    fun navigateTo(fragment: Int) {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.main_host_fragment) as NavHostFragment?
        navHostFragment?.navController?.navigate(fragment)
    }

    fun setToolbarTitle(message: Int) {
        toolbar_title.text = getString(message)
    }
}
