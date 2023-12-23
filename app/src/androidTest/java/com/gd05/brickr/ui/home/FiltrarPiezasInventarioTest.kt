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
class FiltrarPiezasInventarioTest {

    @Rule
    @JvmField
    var mActivityScenarioRule = ActivityScenarioRule(HomeActivity::class.java)

    @Test
    fun filtrarPiezasInventarioTest() {
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

        val chip = onView(
            allOf(
                withId(R.id.chip1), withText("Baseplates"),
                childAtPosition(
                    allOf(
                        withId(R.id.chipGroup),
                        childAtPosition(
                            withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            2
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        chip.perform(click())
        Thread.sleep(2000)
        val chip2 = onView(
            allOf(
                withId(R.id.chip3), withText("Plates special"),
                childAtPosition(
                    allOf(
                        withId(R.id.chipGroup),
                        childAtPosition(
                            withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            2
                        )
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        chip2.perform(click())
        Thread.sleep(2000)
        val textView = onView(
            allOf(
                withId(R.id.inventory_title), withText("Plate Special 1 x 1 Rounded with Handle"),
                withParent(withParent(withId(R.id.cv_Item))),
                isDisplayed()
            )
        )
        textView.check(matches(withText("Plate Special 1 x 1 Rounded with Handle")))
        Thread.sleep(2000)

        val radioButton = onView(
            allOf(
                withId(R.id.chip3), withText("Plates special"),
                withParent(
                    allOf(
                        withId(R.id.chipGroup),
                        withParent(IsInstanceOf.instanceOf(android.view.ViewGroup::class.java))
                    )
                ),
                isDisplayed()
            )
        )
        radioButton.check(matches(isDisplayed()))
        Thread.sleep(2000)
        val chip3 = onView(
            allOf(
                withId(R.id.chip4), withText("Panels"),
                childAtPosition(
                    allOf(
                        withId(R.id.chipGroup),
                        childAtPosition(
                            withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            2
                        )
                    ),
                    3
                ),
                isDisplayed()
            )
        )
        chip3.perform(click())
        Thread.sleep(2000)
        val radioButton2 = onView(
            allOf(
                withId(R.id.chip4), withText("Panels"),
                withParent(
                    allOf(
                        withId(R.id.chipGroup),
                        withParent(IsInstanceOf.instanceOf(android.view.ViewGroup::class.java))
                    )
                ),
                isDisplayed()
            )
        )
        radioButton2.check(matches(isDisplayed()))

        val textView2 = onView(
            allOf(
                withId(R.id.inventory_title), withText("Plate Special 1 x 1 Rounded with Handle"),
                withParent(withParent(withId(R.id.cv_Item))),
                isDisplayed()
            )
        )
        textView.check(doesNotExist())

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
