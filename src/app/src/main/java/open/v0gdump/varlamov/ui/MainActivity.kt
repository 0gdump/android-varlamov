package open.v0gdump.varlamov.ui

import android.os.Bundle
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

    fun navigateTo(destination: Int, bundle: Bundle) {
        navController.navigate(destination, bundle)
    }
}
