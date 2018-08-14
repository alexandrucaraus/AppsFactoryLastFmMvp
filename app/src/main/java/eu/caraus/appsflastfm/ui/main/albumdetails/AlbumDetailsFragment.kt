package eu.caraus.appsflastfm.ui.main.albumdetails

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

        const val  ALBUM_ID = "ALBUM_NAME"
        const val  TRANSITION_NAME  = "TRANSITION_NAME"
        const val  TRANSITION_PIC_URL = "TRANSITION_PIC_URL"

        fun newInstance( albumId : String ) : AlbumDetailsFragment {
            val fragment = AlbumDetailsFragment()
            val bundle = Bundle()
            bundle.putString( ALBUM_ID , albumId )
            fragment.arguments = bundle
            return fragment
        }

        fun newInstance( albumId : String,
                         imageUrl : String,
                         transitionName  : String ) : AlbumDetailsFragment {

            val fragment = AlbumDetailsFragment()

            val bundle = Bundle()

            bundle.putString( ALBUM_ID , albumId )
            bundle.putString( TRANSITION_NAME  , transitionName  )
            bundle.putString( TRANSITION_PIC_URL , imageUrl )

            fragment.arguments = bundle

            return fragment
        }

    }

    @Inject
    lateinit var presenter : AlbumDetailsContract.Presenter

    private var adapter : AlbumDetailsAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycle.addObserver( presenter )

        setHasOptionsMenu(true)

        arguments?.let {
            if( it.containsKey( ALBUM_ID )){
                presenter.getAlbumInfo( it.getString( ALBUM_ID ) )
            }
        }

        postponeEnterTransition()

    }

    override fun onResume() {
        presenter.onViewAttached(this)
        super.onResume()
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
            supportActionBar?.setTitle(R.string.title_album_details)
        }

        super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when( item?.itemId ){
            android.R.id.home -> presenter.goBack()
        }
        return true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_album_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvTrackList.layoutManager = LinearLayoutManager(context)
        (rvTrackList.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false

        arguments?.let {
            if( it.containsKey( TRANSITION_NAME )){
                view.ivAlbumImage.transitionName = it.getString( TRANSITION_NAME )
            }
            if( it.containsKey( TRANSITION_PIC_URL )){
                Picasso.with(context)
                        .load( it.getString(TRANSITION_PIC_URL))
                        .fit()
                        .centerCrop()
                        .into( ivAlbumImage, object : Callback {
                            override fun onSuccess() { startPostponedEnterTransition() }
                            override fun onError()   { startPostponedEnterTransition() }
                        })
            } else { startPostponedEnterTransition() }
        } ?: run { startPostponedEnterTransition() }

    }

    override fun updateTrackItem( youTubeVideo: YouTubeVideo) {
        adapter?.updateTrack( youTubeVideo )
    }

    override fun showAlbumInfo( album: Album? ) {

        album?.let {

            Picasso.with(context)
                    .load(Uri.parse( it.image?.get(2)?.text))
                    .fit()
                    .centerCrop()
                    .into( ivAlbumImage )

            tvAlbumName.text  = it.name
            tvArtistName.text = format( R.string.album_by_artist, it.artist )
            it.tracks?.track?.let { trackList ->

                adapter = AlbumDetailsAdapter( trackList, presenter )
                rvTrackList.adapter = adapter
                rvTrackList.adapter.notifyDataSetChanged()
            }

        } ?: run {
            startPostponedEnterTransition()
        }
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