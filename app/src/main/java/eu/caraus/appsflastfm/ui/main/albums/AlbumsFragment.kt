package eu.caraus.appsflastfm.ui.main.albums

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import eu.caraus.appsflastfm.R
import eu.caraus.appsflastfm.data.domain.lastFm.albuminfo.Album
import eu.caraus.appsflastfm.data.domain.lastFm.albums.AlbumItem
import eu.caraus.appsflastfm.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_top_albums.*
import javax.inject.Inject

class AlbumsFragment : BaseFragment(), AlbumsContract.View {


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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
            = inflater.inflate(R.layout.fragment_top_albums, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated( view, savedInstanceState)
        rvAlbums.layoutManager = LinearLayoutManager( context )
    }

    override fun showFoundAlbums( albums : List<Album?>) {

        adapter = AlbumsAdapter( albums , presenter )

        rvAlbums.adapter = adapter
        rvAlbums.adapter.notifyDataSetChanged()
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