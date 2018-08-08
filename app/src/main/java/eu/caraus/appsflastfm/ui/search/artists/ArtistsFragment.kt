package eu.caraus.appsflastfm.ui.search.artists

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import eu.caraus.appsflastfm.R
import eu.caraus.appsflastfm.data.domain.lastFm.artists.ArtistItem
import eu.caraus.appsflastfm.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_artists.*
import javax.inject.Inject

class ArtistsFragment : BaseFragment(), ArtistsContract.View {

    companion object {

        val TAG = ArtistsFragment::class.java.simpleName!!
        const val SEARCH_TERM = "SEARCH_TERM"
        fun newInstance( searchedArtists : String ) : ArtistsFragment {
            val fragment = ArtistsFragment()
            val bundle = Bundle()
            bundle.putString( SEARCH_TERM, searchedArtists )
            fragment.arguments = bundle
            return fragment
        }
    }

    @Inject
    lateinit var presenter : ArtistsContract.Presenter

    private var adapter : ArtistsAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycle.addObserver(presenter)

        arguments?.let {
            if( it.containsKey( SEARCH_TERM )){
                presenter.searchArtist( it.getString(SEARCH_TERM) )
            }
        }

    }

    override fun onResume() {
        super.onResume()
        presenter.onViewAttached(this)
    }

    override fun onPause() {
        presenter.onViewDetached(true)
        super.onPause()
    }

    override fun onDestroy() {
        lifecycle.removeObserver(presenter)
        super.onDestroy()
    }

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
         = inflater.inflate(R.layout.fragment_artists, container, false)

    override fun onViewCreated( view: View, savedInstanceState: Bundle?) {
        super.onViewCreated( view, savedInstanceState)

        rvArtists.layoutManager = LinearLayoutManager( context )

    }

    override fun showFoundArtists( artists: List<ArtistItem?> ) {

        adapter = ArtistsAdapter( artists , presenter )

        rvArtists.adapter = adapter
        rvArtists.adapter.notifyDataSetChanged()

    }

    override fun showFoundNothing() {

    }

    override fun showLoading() {

    }

    override fun hideLoading() {

    }

    override fun showError(error: Throwable) {

    }

}