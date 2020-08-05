package open.v0gdump.varlamov.ui

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.navigation.Navigation
import moxy.MvpAppCompatActivity
import open.v0gdump.varlamov.R


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
        when (item.itemId) {
            R.id.menu_publications_by_date -> {
                Toast.makeText(this, "\uD83D\uDE48 Unimplemented", Toast.LENGTH_LONG).show()
            }
        }

        return true
    }

    fun navigateTo(destination: Int, bundle: Bundle) {
        navController.navigate(destination, bundle)
    }
}
