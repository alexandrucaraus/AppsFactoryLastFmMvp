package eu.caraus.appsflastfm.ui.main.albums

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.view.*
import eu.caraus.appsflastfm.R
import eu.caraus.appsflastfm.data.domain.lastFm.albuminfo.Album
import eu.caraus.appsflastfm.ui.base.BaseActivity
import eu.caraus.appsflastfm.ui.base.BaseFragment
import eu.caraus.appsflastfm.ui.base.util.metrics.dpToPx
import eu.caraus.appsflastfm.ui.base.util.recyclerview.VerticalSpaceItemDecoration
import kotlinx.android.synthetic.main.fragment_albums.*
import kotlinx.android.synthetic.main.fragment_albums.view.*
import javax.inject.Inject

class AlbumsFragment : BaseFragment(), SearchView.OnQueryTextListener, AlbumsContract.View {

    companion object {
        val TAG = AlbumsFragment::class.java.simpleName
        fun newInstance() : AlbumsFragment {
            val fragment = AlbumsFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }

    @Inject
    lateinit var presenter : AlbumsContract.Presenter

    private var adapter : AlbumsAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycle.addObserver( presenter )

        setHasOptionsMenu(true)

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
        lifecycle.removeObserver( presenter )
        super.onDestroy()
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {

        inflater?.inflate( R.menu.main_menu, menu )

        (menu?.findItem(R.id.search)?.actionView as SearchView).apply {
            setOnQueryTextListener(this@AlbumsFragment)
            maxWidth = Int.MAX_VALUE

        }

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onPrepareOptionsMenu(menu: Menu?) {

        ( activity as BaseActivity).apply {
            supportActionBar?.setDisplayHomeAsUpEnabled(false)
            supportActionBar?.setDisplayShowHomeEnabled(true)
            supportActionBar?.setIcon( R.mipmap.ic_last)
            supportActionBar?.setTitle(R.string.title_saved_albums)
        }

        (menu?.findItem(R.id.search)?.actionView as SearchView).apply {
            isIconified = true
        }

        super.onPrepareOptionsMenu(menu)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
            = inflater.inflate(R.layout.fragment_albums, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated( view, savedInstanceState)
        rvAlbums.layoutManager = LinearLayoutManager( context )
        rvAlbums.addItemDecoration( VerticalSpaceItemDecoration( resources.dpToPx( R.dimen.item_spacing)))
        adapter = AlbumsAdapter( mutableListOf() , presenter )
        adapter?.registerAdapterDataObserver( object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                super.onChanged()
                checkEmptyAdapter()
            }
        })
        rvAlbums.adapter = adapter
        rvAlbums.adapter.notifyDataSetChanged()

        waitRecycleViewToRender( view )
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

    override fun updateAlbums( albums : List<Album?> ) {

        adapter?.updateAlbums(albums)

        if( checkEmptyAdapter() ) return

    }

    private fun checkEmptyAdapter() : Boolean {
        return adapter?.let {
            if( it.itemCount == 0 ) {
                llEmptyListPlaceholder.visibility = View.VISIBLE;false
            }
            else {
                llEmptyListPlaceholder.visibility = View.GONE ;true
            }
        } ?: run {
            llEmptyListPlaceholder.visibility = View.VISIBLE;false
        }
    }

    override fun showFoundNothing() {
        checkEmptyAdapter()
    }

    override fun showLoading() {

    }

    override fun hideLoading() {

    }

    override fun deleteSuccess() {
        snack(resources.getString(R.string.delete_success))
    }

    override fun deleteFailed() {
        snack(resources.getString(R.string.delete_failed))
    }

    override fun showError( error: Throwable ) {
        snack( error.localizedMessage )
    }

    private fun snack( message : String ){
        clRoot?.let {
            Snackbar.make( it, message, Snackbar.LENGTH_LONG).show()
        }
    }

    override fun onQueryTextSubmit( query: String?): Boolean {

        query?.let {
            presenter.showSearchResultScreen( it )
        }

        activity?.invalidateOptionsMenu()

        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {

        return true
    }

}