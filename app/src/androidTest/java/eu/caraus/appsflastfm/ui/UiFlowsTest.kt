package eu.caraus.appsflastfm.ui

import android.support.test.espresso.Espresso
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.UiController
import android.support.test.espresso.ViewAction
import android.support.test.espresso.action.ViewActions.*
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.contrib.RecyclerViewActions
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.filters.LargeTest
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import eu.caraus.appsflastfm.R
import eu.caraus.appsflastfm.ui.idlingResources.IdlingResourceUtils.Companion.idlingPoliciesSetup
import eu.caraus.appsflastfm.ui.idlingResources.IdlingResourceUtils.Companion.whenReady
import eu.caraus.appsflastfm.ui.idlingResources.search.*
import eu.caraus.appsflastfm.ui.main.MainActivity
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.containsString
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
@LargeTest
class UiFlowsTest {

    companion object {

        const val ARTIST = "Metallica"
        const val ALBUM  = "Metallica"

        const val TIMEOUT_S = 30L

    }

    @get:Rule
    var mainActivityRule = ActivityTestRule(MainActivity::class.java)

    @Before
    fun before(){
       idlingPoliciesSetup( TIMEOUT_S )
    }

    @After
    fun after(){

    }

    @Test
    fun searchBasicTest() {

        onView( withId( R.id.search )).perform( click())
        onView( withId( R.id.search_src_text)).perform( typeText ( ARTIST ), pressImeActionButton())

        whenReady (SearchArtistListIdlingResource()) {
            onView( withId( R.id.rvArtists)).check( matches( isDisplayed()))
            onView( withId( R.id.rvArtists)).perform( RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))
        }

        whenReady( SearchAlbumsListIdlingResource()) {
            onView( withId( R.id.rvAlbums)).check( matches( isDisplayed()))
            onView( withId( R.id.rvAlbums)).perform( RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))
        }

        whenReady( SearchAlbumsDetailsIdlingResource()) {
            onView( allOf( withId( R.id.tvArtistName) , isDescendantOfA( withId(R.id.nsvAlbumDetails)))).check {
                view,_-> assertThat( (view as TextView).text.toString(), containsString(ARTIST))
            }
            onView( allOf( withId( R.id.tvAlbumName) , isDescendantOfA( withId(R.id.nsvAlbumDetails)))).check {
                view,_-> assertThat( (view as TextView).text.toString(), containsString(ALBUM))
            }
        }

    }

    @Test
    fun searchAndSaveTest() {

        onView( withId( R.id.search )).perform( click())
        onView( withId( R.id.search_src_text)).perform( typeText ( ARTIST ), pressImeActionButton())

        whenReady ( SearchArtistListIdlingResource() ) {
            onView( withId( R.id.rvArtists)).check( matches( isDisplayed()))
            onView( withId( R.id.rvArtists)).perform( RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))
        }

        whenReady( SearchAlbumsListIdlingResource() ) {
            onView( withId( R.id.rvAlbums)).check( matches( isDisplayed()))
            onView( withId( R.id.rvAlbums)).perform(
                    RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, object : ViewAction {
                        override fun getDescription(): String { return ""}
                        override fun getConstraints(): Matcher<View> { return object: Matcher<View>{
                            override fun describeTo(description: Description?) {}
                            override fun describeMismatch(item: Any?, mismatchDescription: Description?) {}
                            override fun _dont_implement_Matcher___instead_extend_BaseMatcher_() {}
                            override fun matches(item: Any?): Boolean { return false }
                        }}
                        override fun perform(uiController: UiController?, view: View?) {
                            view?.findViewById<ImageButton>(R.id.btSaveArtists)?.performClick()
                        }
                    }))
        }

        Espresso.pressBack()

        whenReady ( SearchArtistListIdlingResource() ) {
            onView( withId( R.id.rvArtists)).check( matches( isDisplayed()))
        }

        Espresso.pressBack()


        whenReady( AlbumsListIdlingResource()){
            onView( withId( R.id.rvAlbums)).check( matches( isDisplayed()))
        }

    }


    @Test
    fun mainOpenAlbumDetails() {

    }

    private fun childAtPosition(
            parentMatcher: Matcher<View>, position: Int): Matcher<View> {

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

    private fun childWithId( parentMatcher: Matcher<View>, position: Int): Matcher<View> {

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

