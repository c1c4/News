package dev.dextra.newsapp.feature.sources

import dev.dextra.newsapp.TestConstants
import dev.dextra.newsapp.api.model.ArticlesResponse
import dev.dextra.newsapp.api.model.Source
import dev.dextra.newsapp.base.BaseTest
import dev.dextra.newsapp.base.NetworkState
import dev.dextra.newsapp.base.TestSuite
import dev.dextra.newsapp.feature.news.NewsViewModel
import dev.dextra.newsapp.utils.JsonUtils
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test
import org.koin.test.get

class NewsViewModelTest : BaseTest() {
    private val emptyResponse = ArticlesResponse(ArrayList(), "ok", 0)
    private val sourceMock = Source("general", "us", "Your trusted source for breaking news, analysis, exclusive interviews, headlines, and videos at ABCNews.com.",
        "abc-news", "en", "ABC News",  "https://abcnews.go.com")

    lateinit var viewModel: NewsViewModel

    @Before
    fun setupTest() {
        viewModel = TestSuite.get()
    }

    @Test
    fun testGetArticles() {
        viewModel.configureSource(sourceMock)
        viewModel.loadNews()

        assert(viewModel.articles.value?.size == 11)
        assertEquals(NetworkState.SUCCESS, viewModel.networkState.value)

        viewModel.onCleared()

        assert(viewModel.getDisposables().isEmpty())
    }

    @Test
    fun testEmptyArticles() {
        TestSuite.mock(TestConstants.newsURL).body(JsonUtils.toJson(emptyResponse)).apply()

        viewModel.loadNews()

        assert(viewModel.articles.value?.size == 0)
        assertEquals(NetworkState.EMPTY, viewModel.networkState.value)
    }

    @Test
    fun testErrorArticles() {
        TestSuite.mock(TestConstants.newsURL).throwConnectionError().apply()

        viewModel.loadNews()

        assert(viewModel.articles.value == null)
        assertEquals(NetworkState.ERROR, viewModel.networkState.value)
    }
}