package eu.caraus.appsflastfm.ui.search.albumdetails

import android.net.Uri
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import com.squareup.picasso.Picasso
import eu.caraus.appsflastfm.R
import eu.caraus.appsflastfm.data.domain.lastFm.albuminfo.Album
import eu.caraus.appsflastfm.ui.base.BaseActivity
import eu.caraus.appsflastfm.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_album_details.*
import javax.inject.Inject

class AlbumDetailsFragment : BaseFragment(), AlbumDetailsContract.View {

    companion object {

        val TAG = AlbumDetailsFragment::class.java.simpleName!!

        private const val ARTIST_NAME = "ARTIST_NAME"
        private const val  ALBUM_NAME = "ALBUM_NAME"

        fun newInstance( artistName : String, albumName : String ) : AlbumDetailsFragment {
            val fragment = AlbumDetailsFragment()
            val bundle = Bundle()
            bundle.putString( ARTIST_NAME, artistName)
            bundle.putString( ALBUM_NAME , albumName )
            fragment.arguments = bundle
            return fragment
        }
    }

    @Inject
    lateinit var presenter : AlbumDetailsContract.Presenter

    private var adapter : AlbumDetailsAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycle.addObserver(presenter)

        setHasOptionsMenu(true)

        arguments?.let {
            if( it.containsKey( ALBUM_NAME ) && it.containsKey(ARTIST_NAME)){
                presenter.getAlbumInfo( it.getString(ALBUM_NAME) ,
                                        it.getString(ARTIST_NAME))
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
            = inflater.inflate(R.layout.fragment_album_details, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvTrackList.layoutManager = LinearLayoutManager(context)
    }


    override fun showAlbumInfo( album: Album? ) {
        album?.let {

            Picasso.with(context)
                    .load(Uri.parse( it.image?.get(2)?.text))
                    .error( R.mipmap.ic_last)
                    .fit()
                    .centerInside()
                    .into(ivAlbumImage)

            tvAlbumName.text  = it.name
            tvArtistName.text = format( R.string.album_by_artist, it.artist )
            it.tracks?.track?.let { trackList ->

                adapter = AlbumDetailsAdapter( trackList, presenter )
                rvTrackList.adapter = adapter
                rvTrackList.adapter.notifyDataSetChanged()
            }

        }
    }

    override fun showLoading() {

    }

    override fun hideLoading() {

    }

    override fun showError(error: Throwable) {

    }

    private fun format(resId : Int, text : String?) : String?
            = context?.resources?.getString( resId, text)

}