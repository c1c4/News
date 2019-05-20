package dev.dextra.newsapp.feature.news

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dev.dextra.newsapp.R
import dev.dextra.newsapp.api.model.Article
import dev.dextra.newsapp.api.model.Source
import dev.dextra.newsapp.api.repository.NewsRepository
import dev.dextra.newsapp.base.BaseListActivity
import dev.dextra.newsapp.base.repository.EndpointService
import dev.dextra.newsapp.components.LoadPageScrollListener
import dev.dextra.newsapp.feature.news.adapter.ArticleListAdapter
import kotlinx.android.synthetic.main.activity_news.*
import org.koin.android.ext.android.inject


const val NEWS_ACTIVITY_SOURCE = "NEWS_ACTIVITY_SOURCE"

class NewsActivity : BaseListActivity(), ArticleListAdapter.ArticleListAdapterItemListener, LoadPageScrollListener.LoadPageScrollLoadMoreListener {

    override val emptyStateTitle: Int = R.string.empty_state_title_source
    override val emptyStateSubTitle: Int = R.string.empty_state_subtitle_source
    override val errorStateTitle: Int = R.string.error_state_title_source
    override val errorStateSubTitle: Int = R.string.error_state_subtitle_source
    override val mainList: View
        get() = news_list

    private val articlesViewModel: NewsViewModel by inject()

    private var viewAdapter: ArticleListAdapter = ArticleListAdapter(this)
    private var viewManager: RecyclerView.LayoutManager = GridLayoutManager(this, 1)
    private var pageScroll: LoadPageScrollListener = LoadPageScrollListener(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_news)

        (intent?.extras?.getSerializable(NEWS_ACTIVITY_SOURCE) as Source).let { source ->
            title = source.name

            loadNews(source)
            setupList()
        }

        super.onCreate(savedInstanceState)

    }

    override fun setupLandscape() {
        setListColumns(2)
    }

    override fun setupPortrait() {
        setListColumns(1)
    }

    override fun executeRetry() {
        (intent?.extras?.getSerializable(NEWS_ACTIVITY_SOURCE) as Source).let { source ->
            title = source.name

            loadNews(source)
        }
    }

    override fun onLoadMore(currentPage: Int, totalItemCount: Int, recyclerView: RecyclerView) {
        articlesViewModel.loadNews(currentPage)
        println(totalItemCount)
    }

    private fun setupList() {
        news_list.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
            addOnScrollListener(pageScroll)
        }
    }

    private fun loadNews(source: Source) {
        articlesViewModel.articles.observe(this, Observer {
            viewAdapter.apply {
                add(it)
                notifyDataSetChanged()
            }
        })

        articlesViewModel.networkState.observe(this, networkStateObserver)
        articlesViewModel.configureSource(source)
        articlesViewModel.loadNews()
    }

    override fun onClick(article: Article) {
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(article.url)
        startActivity(i)
    }

    private fun setListColumns(columns: Int) {
        val layoutManager = news_list.layoutManager
        if (layoutManager is GridLayoutManager) {
            layoutManager.spanCount = columns
            viewAdapter.notifyDataSetChanged()
        }
    }

}
