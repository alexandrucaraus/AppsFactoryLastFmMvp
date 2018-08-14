package eu.caraus.appsflastfm.ui.search.albums

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import eu.caraus.appsflastfm.R
import eu.caraus.appsflastfm.data.domain.lastFm.albums.AlbumItem
import eu.caraus.appsflastfm.ui.base.BaseActivity
import eu.caraus.appsflastfm.ui.base.BaseFragment
import eu.caraus.appsflastfm.ui.common.metrics.dpToPx
import eu.caraus.appsflastfm.ui.common.recyclerview.VerticalSpaceItemDecoration
import kotlinx.android.synthetic.main.fragment_albums.*
import kotlinx.android.synthetic.main.fragment_albums.view.*
import javax.inject.Inject

class AlbumsFragment : BaseFragment(), AlbumsContract.View {

    companion object {
        val TAG = AlbumsFragment::class.java.simpleName!!
        private const val SEARCH_TERM = "SEARCH_TERM"
        fun newInstance( artistId : String ) : AlbumsFragment {
            val fragment = AlbumsFragment()
            val bundle = Bundle()
            bundle.putString( SEARCH_TERM, artistId )
            fragment.arguments = bundle
            return fragment
        }
    }

    @Inject
    lateinit var presenter : AlbumsContract.Presenter

    private var adapter : AlbumsAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycle.addObserver(presenter)

        setHasOptionsMenu(true)

        ( activity as BaseActivity).apply{
            supportActionBar?.title = resources.getString(R.string.title_search_artist)
        }

        arguments?.let {
            if( it.containsKey( AlbumsFragment.SEARCH_TERM )){
                presenter.getAlbums( it.getString( AlbumsFragment.SEARCH_TERM ))
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
            android.R.id.home -> if( presenter.goBack() ) (activity as BaseActivity).finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
            = inflater.inflate(R.layout.fragment_albums, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated( view, savedInstanceState)

        rvAlbums.layoutManager = LinearLayoutManager( context )
        rvAlbums.addItemDecoration( VerticalSpaceItemDecoration( resources.dpToPx( R.dimen.item_spacing)))

        waitRecycleViewToRender(view)

    }

    private fun waitRecycleViewToRender( view: View ){
        postponeEnterTransition()
        view.rvAlbums?.viewTreeObserver?.addOnPreDrawListener( object : ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                startPostponedEnterTransition()
                view.rvAlbums.viewTreeObserver.removeOnPreDrawListener( this )
                return true
            }
        })
    }

    override fun showFoundAlbums( albums : List<AlbumItem?>) {

        llEmptyListPlaceholder.visibility = View.GONE

        adapter = AlbumsAdapter( albums , presenter )

        rvAlbums.adapter = adapter
        rvAlbums.adapter.notifyDataSetChanged()

    }

    override fun showFoundNothing() {
        llEmptyListPlaceholder.visibility = View.VISIBLE
    }

    override fun showLoading() {
        progress.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        progress.visibility = View.GONE
    }

    override fun showError(error: Throwable) {
        snack( error.localizedMessage )
    }

    private fun snack( message : String ){
        clRoot?.let {
            Snackbar.make( it, message, Snackbar.LENGTH_LONG).show()
        }
    }

}