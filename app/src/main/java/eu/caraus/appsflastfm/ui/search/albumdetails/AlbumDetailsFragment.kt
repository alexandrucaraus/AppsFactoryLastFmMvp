package eu.caraus.appsflastfm.ui.search.albumdetails

import android.net.Uri
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SimpleItemAnimator
import android.view.*
import com.squareup.picasso.Callback

import com.squareup.picasso.Picasso

import eu.caraus.appsflastfm.R
import eu.caraus.appsflastfm.data.domain.lastFm.albuminfo.Album
import eu.caraus.appsflastfm.services.youtube.model.youtube.YouTubeVideo
import eu.caraus.appsflastfm.ui.base.BaseActivity
import eu.caraus.appsflastfm.ui.base.BaseFragment

import kotlinx.android.synthetic.main.fragment_album_details.*
import kotlinx.android.synthetic.main.fragment_album_details.view.*

import javax.inject.Inject

class AlbumDetailsFragment : BaseFragment(), AlbumDetailsContract.View {

    companion object {

        val TAG = AlbumDetailsFragment::class.java.simpleName!!

        const val ARTIST_NAME        = "ARTIST_NAME"
        const val ALBUM_NAME         = "ALBUM_NAME"
        const val TRANSITION_NAME    = "TRANSITION_NAME"
        const val TRANSITION_PIC_URL = "TRANSITION_PIC_URL"

        fun newInstance( artistName : String, albumName : String ) : AlbumDetailsFragment {
            val fragment = AlbumDetailsFragment()
            val bundle = Bundle()
            bundle.putString( ARTIST_NAME, artistName)
            bundle.putString( ALBUM_NAME , albumName )
            fragment.arguments = bundle
            return fragment
        }

        fun newInstance( artistName       : String,
                         albumName        : String ,
                         transitionPicUrl : String,
                         transitionName   : String) : AlbumDetailsFragment {
            val fragment = AlbumDetailsFragment()
            val bundle = Bundle()
            bundle.putString( ARTIST_NAME       , artistName)
            bundle.putString( ALBUM_NAME        , albumName )
            bundle.putString( TRANSITION_PIC_URL, transitionPicUrl)
            bundle.putString( TRANSITION_NAME   , transitionName)
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
                presenter.getAlbumInfo(  it.getString(ARTIST_NAME), it.getString(ALBUM_NAME))
                setTitle( it.getString(ALBUM_NAME), it.getString(ARTIST_NAME) )
            }
        }

        postponeEnterTransition()

    }

    private fun setTitle( album : String = "" , artist : String = ""){
        ( activity as BaseActivity).apply{
            supportActionBar?.title = resources.getString(R.string.title_for_album_param, album, artist)
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
        (rvTrackList.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false

        arguments?.let {

            if( it.containsKey( TRANSITION_NAME )){
                view.ivAlbumImage.transitionName = it.getString( TRANSITION_NAME )
            }

            if( it.containsKey( TRANSITION_PIC_URL) ){
                Picasso.with(context)
                        .load( it.getString( TRANSITION_PIC_URL))
                        .fit()
                        .centerCrop()
                        .into( ivAlbumImage, object : Callback {
                            override fun onSuccess() { startPostponedEnterTransition() }
                            override fun onError()   { startPostponedEnterTransition() }
                        })

            } else { startPostponedEnterTransition() }

        } ?: run { startPostponedEnterTransition() }

    }

    override fun showAlbumInfo( album: Album? ) {

        album?.let {
            tvAlbumName.text  = it.name
            tvArtistName.text = format( R.string.album_by_artist, it.artist )
            it.tracks?.track?.let { trackList ->
                adapter = AlbumDetailsAdapter( trackList, presenter )
                rvTrackList.adapter = adapter
                rvTrackList.adapter.notifyDataSetChanged()
            }
        }
    }

    override fun updateTrackItem( youTubeVideo: YouTubeVideo) {
        adapter?.updateTrack(youTubeVideo)
    }

    override fun showLoading() {

    }

    override fun hideLoading() {

    }

    override fun showError(error: Throwable) {
        snack( error.localizedMessage )
    }

    private fun format(resId : Int, text : String?) : String?
            = context?.resources?.getString( resId, text)

    private fun snack( message : String ){
        clRoot?.let {
            Snackbar.make( it, message, Snackbar.LENGTH_LONG).show()
        }
    }

}