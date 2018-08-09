package eu.caraus.appsflastfm.ui.main.albums

import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.squareup.picasso.Picasso
import eu.caraus.appsflastfm.R
import eu.caraus.appsflastfm.data.domain.lastFm.albuminfo.Album
import kotlinx.android.synthetic.main.album_list_item_save.view.*

class AlbumsAdapter(var albums    : List<Album?>,
                    val presenter : AlbumsContract.Presenter )
                                        : RecyclerView.Adapter<AlbumsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            ViewHolder( LayoutInflater.from( parent.context)
                    .inflate( R.layout.album_list_item, parent, false))

    override fun getItemCount() = albums.size

    override fun onBindViewHolder( holder: ViewHolder, position: Int) {

        albums[ position ]?.let {

            holder.albumName?.text = it.name
            holder.albumArtist?.text = format( holder, R.string.album_by_artist, it.artist)
            holder.albumPlayCount?.text = format( holder, R.string.album_play_count, it.playcount )

            Picasso.with( holder.itemView.context )
                     .load( Uri.parse( it.image?.get(2)?.text))
                     .error( R.mipmap.ic_last )
                     .fit()
                     .centerCrop()
                     .into( holder.albumImage )

            holder.rootView?.setOnClickListener { _->
                    presenter.showAlbumDetails( it.mbid  )
                }

         }

    }

    private fun format( holder : ViewHolder?, resId : Int , text : String?) : String?
            = holder?.itemView?.resources?.getString( resId, text)


    class ViewHolder( view: View) : RecyclerView.ViewHolder(view) {
        var rootView       : RelativeLayout? = view.rlAlbum
        var albumImage     : ImageView?  = view.ivAlbumImage
        var albumName      : TextView ?  = view.tvAlbumName
        var albumArtist    : TextView ?  = view.tvAlbumArtist
        var albumPlayCount : TextView ?  = view.tvAlbumPlayCount
    }

}