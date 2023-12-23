package com.gd05.brickr.ui.home


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.gd05.brickr.R
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.`is`
import org.hamcrest.TypeSafeMatcher
import org.hamcrest.core.IsInstanceOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class ConsultarPiezasSetTest {

    @Rule
    @JvmField
    var mActivityScenarioRule = ActivityScenarioRule(HomeActivity::class.java)

    @Test
    fun consultarPiezasSetTest() {
        // Select the sets search mode
        val appCompatToggleButton = onView(
            allOf(
                withId(R.id.search_sets_button), withText("Sets"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.LinearLayout")),
                        0
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        appCompatToggleButton.perform(click())

        // Click on the search button
        val actionMenuItemView = onView(
            allOf(
                withId(R.id.search_view), withContentDescription("Búsqueda\n"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.toolbar),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        actionMenuItemView.perform(click())

        // Search for a set
        val searchAutoComplete = onView(
            allOf(
                withId(com.google.android.material.R.id.search_src_text),
                childAtPosition(
                    allOf(
                        withId(com.google.android.material.R.id.search_plate),
                        childAtPosition(
                            withId(com.google.android.material.R.id.search_edit_frame),
                            1
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        searchAutoComplete.perform(replaceText("Mickey's Boat"), closeSoftKeyboard())

        val searchAutoComplete2 = onView(
            allOf(
                withId(com.google.android.material.R.id.search_src_text), withText("Mickey's Boat"),
                childAtPosition(
                    allOf(
                        withId(com.google.android.material.R.id.search_plate),
                        childAtPosition(
                            withId(com.google.android.material.R.id.search_edit_frame),
                            1
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        searchAutoComplete2.perform(pressImeActionButton())

        // Wait for the search to complete loading
        Thread.sleep(5000)

        // Click on the first result
        val cardView = onView(
            allOf(
                withId(R.id.cv_Item),
                childAtPosition(
                    allOf(
                        withId(R.id.cl_item),
                        childAtPosition(
                            withId(R.id.rv_search_list),
                            0
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        cardView.perform(click())

        // Wait for the set to complete loading
        Thread.sleep(2000)

        // Look for and click on the parts button
        val button = onView(
            allOf(
                withId(R.id.puzzle_button), withText("Bricks"),
                withParent(withParent(withId(R.id.nav_host_fragment))),
                isDisplayed()
            )
        )
        button.check(matches(isDisplayed()))

        val materialButton = onView(
            allOf(
                withId(R.id.puzzle_button), withText("Bricks"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.nav_host_fragment),
                        0
                    ),
                    5
                ),
                isDisplayed()
            )
        )
        materialButton.perform(click())

        // Check that the parts list is displayed
        val viewGroup = onView(
            allOf(
                withId(R.id.linearLayout),
                withParent(withParent(withId(R.id.nav_host_fragment))),
                isDisplayed()
            )
        )
        viewGroup.check(matches(isDisplayed()))

        // Wait for the parts to complete loading
        Thread.sleep(5000)

        // Check that a specific part is displayed
        val textView = onView(
            allOf(
                withId(R.id.brickset_part_title), withText("Duplo Boat Base"),
                withParent(withParent(withId(R.id.cv_Item))),
                isDisplayed()
            )
        )
        textView.check(matches(withText("Duplo Boat Base")))

        // Check that the part amount is correct
        val textView2 = onView(
            allOf(
                allOf(
                    withId(R.id.brickset_part_amount),
                    withText("0 / 1"),
                    hasSibling(withText("Duplo Boat Base"))
                ),
                withParent(withParent(withId(R.id.cv_Item))),
                isDisplayed()
            )
        )
        textView2.check(matches(withText("0 / 1")))
    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}
