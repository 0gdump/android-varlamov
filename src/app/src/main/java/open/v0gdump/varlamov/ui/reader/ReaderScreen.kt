package open.v0gdump.varlamov.ui.reader

import android.os.Bundle
import kotlinx.android.synthetic.main.fragment_reader.*
import open.v0gdump.varlamov.R
import open.v0gdump.varlamov.model.platform.livejournal.model.Publication
import open.v0gdump.varlamov.ui.global.MvpFragmentX

class ReaderScreen : MvpFragmentX(R.layout.fragment_reader) {

    override fun setupLayout() {}

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val publication = arguments?.get("publication") as Publication

        text_hello.text = ("Hello, ${publication.title}")
    }
}