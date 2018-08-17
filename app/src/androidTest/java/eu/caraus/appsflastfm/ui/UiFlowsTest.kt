package eu.caraus.appsflastfm.ui

import android.support.test.espresso.Espresso
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.*
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.contrib.RecyclerViewActions
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.filters.LargeTest
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.support.v7.widget.RecyclerView
import android.widget.TextView
import eu.caraus.appsflastfm.R
import eu.caraus.appsflastfm.testUtils.views.RecyclerViewUtils.withRecyclerView
import eu.caraus.appsflastfm.ui.idlingResources.IdlingResourceUtils.Companion.idlingPoliciesSetup
import eu.caraus.appsflastfm.ui.idlingResources.IdlingResourceUtils.Companion.whenReady
import eu.caraus.appsflastfm.ui.idlingResources.search.*
import eu.caraus.appsflastfm.ui.main.MainActivity
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.containsString

import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.NoMatchingViewException

import eu.caraus.appsflastfm.testUtils.views.RecyclerViewUtils.recyclerViewItemsCount


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
            onView( withRecyclerView(R.id.rvAlbums).atPositionOnView(0, R.id.btSaveArtists)).perform(click())
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
    fun searchSaveAndThenDelete() {

        cleanAllSavedAlbums()

        onView( withId( R.id.search )).perform( click())
        onView( withId( R.id.search_src_text)).perform( typeText ( ARTIST ), pressImeActionButton())

        whenReady ( SearchArtistListIdlingResource() ) {
            onView( withId( R.id.rvArtists)).check( matches( isDisplayed()))
            onView( withId( R.id.rvArtists)).perform( RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))
        }

        whenReady( SearchAlbumsListIdlingResource() ) {
            onView( withId( R.id.rvAlbums)).check( matches( isDisplayed()))
            onView( withRecyclerView(R.id.rvAlbums).atPositionOnView(0, R.id.btSaveArtists)).perform(click())
        }

        Espresso.pressBack()

        whenReady ( SearchArtistListIdlingResource() ) {
            onView( withId( R.id.rvArtists)).check( matches( isDisplayed()))
        }

        Espresso.pressBack()

        whenReady( AlbumsListIdlingResource()){
            onView( withId( R.id.rvAlbums)).check( matches( isDisplayed()))
            onView( withRecyclerView(R.id.rvAlbums).atPositionOnView(0 , R.id.btDeleteAlbum)).perform(click())
        }

    }


    @Test
    fun deleteAllSearchSaveAndThenCheckDetails() {

        cleanAllSavedAlbums()

        searchAndSaveTest()

        whenReady( AlbumsListIdlingResource()){
            onView( withId( R.id.rvAlbums)).check( matches( isDisplayed()))
            onView( withId( R.id.rvAlbums)).perform( RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))
        }

        whenReady( AlbumsDetailsIdlingResource()) {
            onView( allOf( withId( R.id.tvArtistName) , isDescendantOfA( withId(R.id.nsvAlbumDetails)))).check {
                view,_-> assertThat( (view as TextView).text.toString(), containsString(ARTIST))
            }
            onView( allOf( withId( R.id.tvAlbumName) , isDescendantOfA( withId(R.id.nsvAlbumDetails)))).check {
                view,_-> assertThat( (view as TextView).text.toString(), containsString(ALBUM))
            }
        }


    }

    private fun cleanAllSavedAlbums() {
        var items = 0
        try {
            items = recyclerViewItemsCount(R.id.rvAlbums)
        } catch ( e : NoMatchingViewException ){

        }
        // clean all albums
        if( items > 0 ) {
            whenReady(AlbumsListIdlingResource()) {
                onView(withId(R.id.rvAlbums)).check(matches(isDisplayed()))
            }
            for (i in 0..(items - 1)) {
                onView(withRecyclerView(R.id.rvAlbums).atPositionOnView(0, R.id.btDeleteAlbum)).perform(click())
                Thread.sleep(1000)
            }
        }
    }


}

