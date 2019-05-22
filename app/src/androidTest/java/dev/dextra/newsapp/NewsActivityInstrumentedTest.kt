package dev.dextra.newsapp

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.espresso.intent.matcher.IntentMatchers.hasData
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import dev.dextra.newsapp.api.model.ArticlesResponse
import dev.dextra.newsapp.api.model.Source
import dev.dextra.newsapp.base.BaseInstrumentedTest
import dev.dextra.newsapp.feature.news.NEWS_ACTIVITY_SOURCE
import dev.dextra.newsapp.feature.news.NewsActivity
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.not
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class NewsActivityInstrumentedTest : BaseInstrumentedTest() {
    private val emptyResponse = ArticlesResponse(ArrayList(), "ok", 0)
    private val sourceMock = Source("cat", "BR", "Test Brazil Description", "1234", "PT", "Test Brazil", "http://www.google.com.br")

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

    @Test
    fun testEmptyViewDisplay() {
        val matcher = allOf(hasAction(Intent.ACTION_VIEW), hasData("https://abcnews.go.com/US/wireStory/texas-remove-hemp-controlled-substance-list-62093347"))

        val result = Instrumentation.ActivityResult(Activity.RESULT_OK, null)

        intending(matcher).respondWith(result)

        onView(withId(R.id.news_list)).check(matches(isDisplayed())).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                click()
            )
        )
        intended(matcher)
    }

    @After
    fun clearTest() {
        Intents.release()
    }
}