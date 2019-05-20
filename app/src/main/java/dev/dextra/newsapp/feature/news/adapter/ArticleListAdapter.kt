package dev.dextra.newsapp.feature.news.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.dextra.newsapp.R
import dev.dextra.newsapp.api.model.Article
import dev.dextra.newsapp.feature.news.NewsActivity
import kotlinx.android.synthetic.main.item_article.view.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class ArticleListAdapter(val listener: ArticleListAdapterItemListener) :
    RecyclerView.Adapter<ArticleListAdapter.ArticlesListAdapterViewHolder>() {

    private val dateFormat = SimpleDateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.SHORT)
    private val parseFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())

    private val dataset: ArrayList<Article> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticlesListAdapterViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_article, parent, false)
        return ArticlesListAdapterViewHolder(view)
    }

    override fun getItemCount(): Int = dataset.size

    override fun onBindViewHolder(holder: ArticlesListAdapterViewHolder, position: Int) {
        val article = dataset[position]

        holder.view.setOnClickListener { listener.onClick(article) }

        holder.view.article_name.text = article.title
        holder.view.article_description.text = article.description
        holder.view.article_author.text = article.author
        holder.view.article_date.text = dateFormat.format(parseFormat.parse(article.publishedAt))
    }

    fun add(articles: List<Article>) {
        dataset.addAll(articles)
    }

    fun clear() {
        dataset.clear()
    }

    class ArticlesListAdapterViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    interface ArticleListAdapterItemListener {

        fun onClick(article: Article)

    }
}