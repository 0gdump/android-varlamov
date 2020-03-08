package retulff.open.varlamov.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import retulff.open.varlamov.R
import retulff.open.varlamov.viewmodel.ReaderViewModel

class ReaderActivity : AppCompatActivity() {

    private val model by lazy {
        ViewModelProviders.of(this).get(ReaderViewModel::class.java)
    }

    lateinit var url: String

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reader)

        extractPassedData()
        bindModel()

        model.postUrl(url)
    }

    private fun extractPassedData() {
        intent.extras?.let {
            url = it["URL"] as String
        }
    }

    private fun bindModel() {
        model.getContentBody().observe(this, Observer {
            parsePublication()
        })
    }

    private fun parsePublication() {

        //val contentBody = model.getContentBody().value!!

        //contentBody.children().forEach {
//
         //   when(it.tagName()) {
//
         //   }
        //}
    }

    private fun showPublication() {

    }

    private fun showError() {

    }
}
