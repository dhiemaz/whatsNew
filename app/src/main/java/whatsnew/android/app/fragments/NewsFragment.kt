package whatsnew.android.app.fragments

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import whatsnew.android.app.R
import whatsnew.android.app.adapter.CycleAdapter


class NewsFragment(urlImage: ArrayList<String>, titles: ArrayList<String>,
                   descriptions: ArrayList<String>, authors: ArrayList<String>, url: ArrayList<String>
) : Fragment(R.layout.fragment_news_layout) {
    private var recyclerView: RecyclerView? = null
    private var urlImage = ArrayList<String>()
    private var titles = ArrayList<String>()
    private var descriptions = ArrayList<String>()
    private var authors = ArrayList<String>()
    private var url = ArrayList<String>()

    init {
        this.urlImage = urlImage
        this.titles = titles
        this.descriptions = descriptions
        this.authors = authors
        this.url = url
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView: RecyclerView = view.findViewById(R.id.recycleView)
        recyclerView.setLayoutManager(LinearLayoutManager(view.context))
        recyclerView.setAdapter(
            CycleAdapter(view.context, urlImage, titles, descriptions, authors, url)
        )
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    } // TODO : ADD NEW WORK
}

