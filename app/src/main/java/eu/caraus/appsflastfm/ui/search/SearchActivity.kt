package eu.caraus.appsflastfm.ui.search


import android.content.Intent
import android.os.Bundle

import android.view.Menu
import android.view.MenuItem
import eu.caraus.appsflastfm.R
import eu.caraus.appsflastfm.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_search.*
import javax.inject.Inject

/**
 *
 *  SearchActivity - Search Artist Activity
 *
 */

class SearchActivity : BaseActivity() {

    @Inject
    lateinit var presenter : SearchContract.Presenter

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when( item?.itemId ){
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_search)
        setSupportActionBar( toolbar )

        handleIntent(intent)

    }

    private fun handleIntent(intent: Intent?){

        val searchedArtist = intent?.getStringExtra("SEARCH_TERM")

        searchedArtist?.let {
            presenter.searchArtist(it)
        }

    }

}
