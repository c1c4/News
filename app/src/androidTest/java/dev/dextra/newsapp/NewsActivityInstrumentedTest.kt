package dev.dextra.newsapp

import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import dev.dextra.newsapp.api.model.ArticlesResponse
import dev.dextra.newsapp.api.model.Source
import dev.dextra.newsapp.base.BaseInstrumentedTest
import dev.dextra.newsapp.feature.news.NEWS_ACTIVITY_SOURCE
import dev.dextra.newsapp.feature.news.NewsActivity
import org.hamcrest.CoreMatchers.not
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.InstrumentationRegistry.getTargetContext
import androidx.test.platform.app.InstrumentationRegistry
import dev.dextra.newsapp.api.model.Article


@RunWith(AndroidJUnit4::class)
class NewsActivityInstrumentedTest : BaseInstrumentedTest() {
    private val sourceMock = Source("general", "us", "Your trusted source for breaking news, analysis, exclusive interviews, headlines, and videos at ABCNews.com.",
        "abc-news", "en", "ABC News",  "https://abcnews.go.com")

    @get:Rule
    val activityRule = ActivityTestRule(NewsActivity::class.java, false, false)

    @Before
    fun setupTest() {
        //we need to launch the activity here so the MockedEndpointService is set
        val targetContext = InstrumentationRegistry.getInstrumentation().targetContext
        val intent = Intent(targetContext, NewsActivity::class.java)
        intent.putExtra(NEWS_ACTIVITY_SOURCE, sourceMock)
        activityRule.launchActivity(intent)
        Intents.init()
    }

    @Test
    fun testViewDisplay() {
        onView(withId(R.id.news_list)).check(matches(isDisplayed()))
        onView(withId(R.id.error_state)).check(matches(not(isDisplayed())))
        onView(withId(R.id.empty_state)).check(matches(not(isDisplayed())))
    }

    @After
    fun clearTest() {
        Intents.release()
    }
}