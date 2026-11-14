package com.slavov.bookshelf

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.Matchers.allOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AddBookFlowTest {
    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun addedBookAppearsInTheList() {
        val title = "Aardvark tales ${System.currentTimeMillis()}"

        onView(withId(R.id.addBookButton)).perform(click())
        onView(withId(R.id.titleInput)).perform(replaceText(title))
        onView(withId(R.id.authorInput)).perform(replaceText("Test Author"))
        onView(withId(R.id.totalPagesInput)).perform(replaceText("320"), closeSoftKeyboard())
        onView(withId(R.id.saveButton)).perform(scrollTo(), click())

        onView(withText(title)).check(matches(isDisplayed()))
    }

    @Test
    fun emptyFormShowsValidationError() {
        onView(withId(R.id.addBookButton)).perform(click())
        onView(withId(R.id.saveButton)).perform(scrollTo(), click())

        onView(allOf(withText(R.string.error_required), isDescendantOfA(withId(R.id.titleLayout))))
            .check(matches(isDisplayed()))
    }
}
