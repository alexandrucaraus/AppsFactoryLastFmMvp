package eu.caraus.appsflastfm.testUtils.views

import android.support.test.espresso.contrib.RecyclerViewActions.scrollToPosition
import android.support.v7.widget.RecyclerView
import android.support.test.espresso.UiController
import android.support.test.espresso.matcher.ViewMatchers
import android.support.test.espresso.ViewAction
import android.support.test.espresso.util.HumanReadables
import android.support.test.espresso.PerformException
import android.support.annotation.IdRes
import android.support.test.espresso.Espresso
import android.support.test.espresso.assertion.ViewAssertions
import android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.view.View
import org.hamcrest.*
import org.hamcrest.Matchers.allOf


object RecyclerViewUtils {


    fun <VH : RecyclerView.ViewHolder> actionOnItemViewAtPosition( position:Int, @IdRes viewId:Int, viewAction:ViewAction):ViewAction {
        return ActionOnItemViewAtPositionViewAction<VH>(position, viewId, viewAction)
    }

    fun recyclerViewItemsCount(@IdRes RecyclerViewId: Int): Int {

        var COUNT = 0
        val matcher = object : TypeSafeMatcher<View>() {

            override fun matchesSafely(item: View): Boolean {
                COUNT = (item as RecyclerView).adapter.itemCount
                return true
            }

            override fun describeTo(description: Description) {

            }
        }
        Espresso.onView(CoreMatchers.allOf(ViewMatchers.withId(RecyclerViewId), isDisplayed())).check(ViewAssertions.matches(matcher))
        val result = COUNT
        COUNT = 0
        return result
    }


    class ActionOnItemViewAtPositionViewAction<VH : RecyclerView.ViewHolder>

    ( private val position:Int,
      @param:IdRes private val viewId:Int,
      private val viewAction:ViewAction )

        : ViewAction {

        override fun getConstraints(): Matcher<View> {
            return allOf( isAssignableFrom(RecyclerView::class.java), isDisplayed() )
        }

        override fun getDescription():String {
            return ("actionOnItemAtPosition performing ViewAction: " + this.viewAction.description + " on item at position: " + this.position)
        }

        override fun perform(uiController:UiController, view:View) {

            val recyclerView = view as RecyclerView

            ScrollToPositionViewAction(this.position).perform( uiController, view)

            uiController.loopMainThreadUntilIdle()

            val targetView = recyclerView.getChildAt(this.position).findViewById<View>(this.viewId)

            if (targetView == null)
            {
                throw PerformException.Builder().withActionDescription(this.toString())
                        .withViewDescription(

                                HumanReadables.describe(view))
                        .withCause(IllegalStateException(
                                "No view with id "
                                        + this.viewId
                                        + " found at position: "
                                        + this.position))
                        .build()
            }
            else
            {
                this.viewAction.perform(uiController, targetView)
            }
        }
    }

    class ScrollToPositionViewAction (

            private val position : Int )

        : ViewAction {

        override fun getConstraints():Matcher<View> {
            return allOf( isAssignableFrom(RecyclerView::class.java), isDisplayed() )
        }

        override fun getDescription():String {
            return "scroll RecyclerView to position: " + this.position
        }

        override fun perform(uiController:UiController, view:View) {
            val recyclerView = view as RecyclerView
            recyclerView.scrollToPosition(this.position)
        }
    }


    fun withRecyclerView( recyclerViewId : Int) : RecyclerViewMatcher {

        return RecyclerViewMatcher(recyclerViewId)
    }

}