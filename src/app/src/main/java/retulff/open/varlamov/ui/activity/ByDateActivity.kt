package retulff.open.varlamov.ui.activity

import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_of_day.*
import okhttp3.ResponseBody
import org.joda.time.DateTime
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retulff.open.varlamov.App
import retulff.open.varlamov.R
import retulff.open.varlamov.ui.adapter.PublicationsSimpleAdapter
import retulff.open.varlamov.ui.view.extension.showBeautifulError
import retulff.open.varlamov.util.TimeUtils
import retulff.open.varlamov.varlamov.Blog
import retulff.open.varlamov.varlamov.platform.livejournal.model.PublicationsFactory
import retulff.open.varlamov.viewmodel.ByDateViewModel

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
            if (TimeUtils.isCurrentYear(date)) {
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
        Blog.getPublicationsOfDay(200, date, this)
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
            getString(R.string.retry),
            View.OnClickListener {
                loadPublications()
            }
        )
    }

    private fun showEmpty() {
        stateful_container.showEmpty(R.string.no_content)
    }
}