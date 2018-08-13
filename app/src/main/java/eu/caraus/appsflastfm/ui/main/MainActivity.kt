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

class MainActivity : BaseActivity(), MainContract.View {

    companion object {
        val TAG = MainActivity::class.java.simpleName!!
    }

    @Inject
    lateinit var presenter : MainContract.Presenter

    @Inject
    lateinit var navigator : MainContract.Navigator


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

    override fun onBackPressed() {
        if( presenter.goBack() ) finish()
    }

}
