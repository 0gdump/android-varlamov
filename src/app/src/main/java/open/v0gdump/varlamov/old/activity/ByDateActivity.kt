package open.v0gdump.varlamov.old.activity

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_of_day.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import open.v0gdump.varlamov.App
import open.v0gdump.varlamov.R
import open.v0gdump.varlamov.old.adapter.PublicationsSimpleAdapter
import open.v0gdump.varlamov.extension.showBeautifulError
import open.v0gdump.varlamov.extension.isCurrentYear
import open.v0gdump.varlamov.model.Blog
import open.v0gdump.varlamov.model.platform.livejournal.model.PublicationsFactory
import open.v0gdump.varlamov.old.viewmodel.ByDateViewModel
import org.joda.time.DateTime
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ByDateActivity : AppCompatActivity(), Callback<ResponseBody> {

    private val model by lazy {
        ViewModelProviders.of(this).get(ByDateViewModel::class.java)
    }

    private lateinit var date: DateTime

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_of_day)

        extractPassedData()

        setupToolbar()
        setupRecycler()

        observeModel()

        loadPublications()
    }

    private fun extractPassedData() {
        intent.extras?.let {

            val year = it["YEAR"] as Int
            val month = it["MONTH"] as Int
            val day = it["DAY"] as Int

            date = DateTime(year, month, day, 0, 0, 0)
        }
    }

    private fun setupToolbar() {

        setSupportActionBar(toolbar)

        val title =
            if (date.isCurrentYear()) {
                date.toString("d MMMM")
            } else {
                date.toString("d MMMM yyyy")
            }

        val subtitle = App.res.getString(R.string.title_posts)

        toolbar_title.text = title
        toolbar_subtitle.text = subtitle

        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        toolbar.setNavigationOnClickListener { finish() }
    }

    private fun setupRecycler() {

        recycler.layoutManager = LinearLayoutManager(this)

        swipe_to_refresh.setOnRefreshListener {
            Handler().run {

                swipe_to_refresh.isRefreshing = false
                loadPublications()
            }
        }
    }

    private fun observeModel() {
        model.getPublicationsByDate().observe(this, Observer {

            val adapter = PublicationsSimpleAdapter(it)
            recycler.adapter = adapter

            stateful_container.showContent()
        })
    }

    private fun loadPublications() {

        recycler.adapter = null

        stateful_container.showLoading(getString(R.string.state_loading))

        // TODO: Заменить загрузку в конкретный день на загрузку в промежуток 3 дней
        GlobalScope.launch {
            Blog.getPublicationsOfDay(200, date, this@ByDateActivity)
        }
    }

    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
        showError()
    }

    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
        if (response.isSuccessful) {

            //val rawResponse = Jsoup.parse(response.body()?.string(), "", Parser.xmlParser())
            val publications = PublicationsFactory.convert(response.body()!!.string())

            if (publications.isNotEmpty()) {
                model.postPublicationsByDate(publications)
            } else {
                showEmpty()
            }
        } else {

            showError()
        }
    }

    private fun showError() {
        stateful_container.showBeautifulError(
            getString(R.string.state_error),
            getString(R.string.retry)
        ) { loadPublications() }
    }

    private fun showEmpty() {
        stateful_container.showEmpty(R.string.no_content)
    }
}