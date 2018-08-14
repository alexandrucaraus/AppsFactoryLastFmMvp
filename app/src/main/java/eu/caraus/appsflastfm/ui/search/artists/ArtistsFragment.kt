package eu.caraus.appsflastfm.ui.search.artists

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import eu.caraus.appsflastfm.R
import eu.caraus.appsflastfm.data.domain.lastFm.artists.ArtistItem
import eu.caraus.appsflastfm.ui.base.BaseActivity
import eu.caraus.appsflastfm.ui.base.BaseFragment
import eu.caraus.appsflastfm.ui.common.metrics.dpToPx
import eu.caraus.appsflastfm.ui.common.recyclerview.VerticalSpaceItemDecoration
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

        lifecycle.addObserver( presenter )

        setHasOptionsMenu( true )

        arguments?.let {
            if( it.containsKey( SEARCH_TERM )){
                val searchTerm = it.getString(SEARCH_TERM)
                presenter.searchArtist( searchTerm )
                  setTitle( searchTerm)

            } else setTitle()
        } ?: run { setTitle() }

    }

    private fun setTitle( title : String = "" ){
        if( title == "" ){
            ( activity as BaseActivity).apply{
                supportActionBar?.title = resources.getString(R.string.title_search_artist)
            }
        } else {
            ( activity as BaseActivity).apply{
                supportActionBar?.title = resources.getString(R.string.title_search_artist_param,title)
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

    override fun onPrepareOptionsMenu(menu: Menu?) {

        ( activity as BaseActivity).apply {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowHomeEnabled(false)
        }

        super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when( item?.itemId ){
            android.R.id.home -> if( presenter.goBack() ) activity?.finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
         = inflater.inflate(R.layout.fragment_artists, container, false)

    override fun onViewCreated( view: View, savedInstanceState: Bundle?) {
        super.onViewCreated( view, savedInstanceState)

        rvArtists.layoutManager = LinearLayoutManager( context )
        rvArtists.addItemDecoration( VerticalSpaceItemDecoration( resources.dpToPx( R.dimen.item_spacing)))

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