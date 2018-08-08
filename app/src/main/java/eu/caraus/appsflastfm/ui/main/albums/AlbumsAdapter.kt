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
import kotlinx.android.synthetic.main.album_list_item.view.*

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
            holder.albumArtist?.text = it.artist
            holder.albumPlayCount?.text = it.playcount.toString()

            Picasso.with( holder.itemView.context )
                     .load( Uri.parse( it.image?.get(2)?.text))
                     //.error( R.drawable.image_broken)
                     .fit()
                     .centerCrop()
                     .into( holder.albumImage )

            holder.rootView?.setOnClickListener { _->
                    //presenter.showAlbumDetails( it.artist?.name!! , it.name.toString()  )
                }

         }

    }

    class ViewHolder( view: View) : RecyclerView.ViewHolder(view) {
        var rootView       : RelativeLayout? = view.rlAlbum
        var albumImage     : ImageView?  = view.ivAlbumImage
        var albumName      : TextView ?  = view.tvAlbumName
        var albumArtist    : TextView ?  = view.tvAlbumArtist
        var albumPlayCount : TextView ?  = view.tvAlbumPlayCount
    }

}