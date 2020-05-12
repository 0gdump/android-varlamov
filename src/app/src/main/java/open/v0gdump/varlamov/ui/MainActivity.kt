package open.v0gdump.varlamov.ui

import android.os.Bundle
import moxy.MvpAppCompatActivity
import open.v0gdump.varlamov.R

class MainActivity : MvpAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
