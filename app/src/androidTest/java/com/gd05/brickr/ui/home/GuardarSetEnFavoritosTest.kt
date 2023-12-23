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
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class GuardarSetEnFavoritosTest {

    @Rule
    @JvmField
    var mActivityScenarioRule = ActivityScenarioRule(HomeActivity::class.java)

    @Test
    fun guardarSetEnFavoritosTest() {
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

        val actionMenuItemView = onView(
            allOf(
                withId(R.id.search_view),
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
        searchAutoComplete.perform(replaceText("Batman "), closeSoftKeyboard())

        val searchAutoComplete2 = onView(
            allOf(
                withId(com.google.android.material.R.id.search_src_text), withText("Batman "),
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

        Thread.sleep(3000)

        val cardView = onView(
            allOf(
                withId(R.id.cv_Item),
                childAtPosition(
                    allOf(
                        withId(R.id.cl_item),
                        childAtPosition(
                            withId(R.id.rv_search_list),
                            1
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        cardView.perform(click())
        Thread.sleep(3000)

        val appCompatToggleButton2 = onView(
            allOf(
                withId(R.id.toggle_favorite),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.nav_host_fragment),
                        0
                    ),
                    4
                ),
                isDisplayed()
            )
        )
        appCompatToggleButton2.perform(click())
        Thread.sleep(3000)

        val bottomNavigationItemView = onView(
            allOf(
                withId(R.id.FavoriteFragment), withContentDescription("Favoritos"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.bottom_navigation),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        bottomNavigationItemView.perform(click())

        Thread.sleep(5000)

        val textView = onView(
            allOf(
                withId(R.id.item_name), withText("Batman Adventure"),
                withParent(withParent(withId(R.id.cv_Item))),
                isDisplayed()
            )
        )
        textView.check(matches(withText("Batman Adventure")))

        Thread.sleep(3000)

        val cardView2 = onView(
            allOf(
                withId(R.id.cv_Item),
                childAtPosition(
                    allOf(
                        withId(R.id.cl_item),
                        childAtPosition(
                            withId(R.id.sets_list),
                            0
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        cardView2.perform(click())

        Thread.sleep(3000)

        val toggleButton = onView(
            allOf(
                withId(R.id.toggle_favorite),
                withParent(withParent(withId(R.id.nav_host_fragment))),
                isDisplayed()
            )
        )
        toggleButton.check(matches(isChecked()))
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
