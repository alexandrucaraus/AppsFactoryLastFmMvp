package eu.caraus.appsflastfm.ui.main

import android.os.Bundle
import android.support.v7.widget.SearchView
import android.view.Menu
import eu.caraus.appsflastfm.R
import eu.caraus.appsflastfm.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*

import javax.inject.Inject

/**
 *  Application's MainActivity,
 *
 */

class MainActivity : BaseActivity(), MainContract.View, SearchView.OnQueryTextListener {

    companion object {
        val TAG = MainActivity::class.java.simpleName!!
    }

    @Inject
    lateinit var presenter : MainContract.Presenter

    @Inject
    lateinit var navigator : MainContract.Navigator

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.main_menu, menu)

        (menu?.findItem(R.id.search)?.actionView as SearchView).apply {
            setOnQueryTextListener(this@MainActivity)
        }

        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycle.addObserver( presenter )

        setContentView( R.layout.activity_main )

        setSupportActionBar( toolbar )

    }

    override fun onStart() {
        super.onStart()
        presenter.onViewAttached(this)
    }

    override fun onStop() {
        presenter.onViewDetached(false)
        super.onStop()
    }

    override fun onDestroy() {
        lifecycle.removeObserver( presenter )
        super.onDestroy()
    }

    override fun onQueryTextSubmit( searchTerm : String? ): Boolean {

        navigator.showSearchResultScreen( searchTerm!! )

        return true
    }

    override fun onQueryTextChange( newText: String? ): Boolean {

        //navigator.showSearchResultScreen( newText!! )

        return true
    }

    override fun onBackPressed() {
        if( presenter.goBack() ) finish()
    }

}
