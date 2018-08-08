package eu.caraus.appsflastfm.ui.main.albumdetails

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import eu.caraus.appsflastfm.R
import eu.caraus.appsflastfm.data.domain.lastFm.albuminfo.Album
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycle.addObserver(presenter)

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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
            = inflater.inflate(R.layout.fragment_album_details, container, false)

    override fun showAlbumInfo( album: Album? ) {

        album?.let {

            Picasso.with(context)
                    .load(Uri.parse( it.image?.get(1)?.text))
                    .fit()
                    .centerCrop()
                    .into(ivAlbumImage)

            tvAlbumName.text  = it.name
            tvArtistName.text = it.artist

        }

    }

    override fun showLoading() {

    }

    override fun hideLoading() {

    }

    override fun showError(error: Throwable) {

    }

}