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
class EliminarPiezasInventarioTest {

    @Rule
    @JvmField
    var mActivityScenarioRule = ActivityScenarioRule(HomeActivity::class.java)

    @Test
    fun eliminarPiezasInventarioTest() {
        val appCompatToggleButton = onView(
            allOf(
                withId(R.id.search_bricks_button), withText("Piezas"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.LinearLayout")),
                        0
                    ),
                    3
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
        searchAutoComplete.perform(replaceText("plate"), closeSoftKeyboard())

        val searchAutoComplete2 = onView(
            allOf(
                withId(com.google.android.material.R.id.search_src_text), withText("plate"),
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
                            0
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        cardView.perform(click())

        val materialButton = onView(
            allOf(
                withId(R.id.brick_details_add), withText("+"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.sv_brick_details),
                        0
                    ),
                    7
                )
            )
        )
        materialButton.perform(scrollTo(), click())

        Thread.sleep(3000)

        val bottomNavigationItemView = onView(
            allOf(
                withId(R.id.InventoryFragment), withContentDescription("Inventario"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.bottom_navigation),
                        0
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        bottomNavigationItemView.perform(click())

        Thread.sleep(3000)

        val materialButton2 = onView(
            allOf(
                withId(R.id.inventory_destroy),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.cv_Item),
                        0
                    ),
                    5
                ),
                isDisplayed()
            )
        )
        materialButton2.perform(click())

        Thread.sleep(3000)

        val recyclerView = onView(
            allOf(
                withId(R.id.rv_inventory_list),
                withParent(withParent(withId(R.id.nav_host_fragment))),
                isDisplayed()
            )
        )
        recyclerView.check(matches(isDisplayed()))
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
