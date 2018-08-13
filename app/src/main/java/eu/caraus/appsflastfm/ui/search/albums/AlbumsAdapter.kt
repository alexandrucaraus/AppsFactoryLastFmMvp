package eu.caraus.appsflastfm.ui.search.albums

import android.net.Uri
import android.support.v4.view.ViewCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.squareup.picasso.Picasso
import eu.caraus.appsflastfm.R
import eu.caraus.appsflastfm.data.domain.lastFm.albums.AlbumItem
import kotlinx.android.synthetic.main.album_list_item_save.view.*

class AlbumsAdapter(var albums    : List<AlbumItem?>,
                    val presenter : AlbumsContract.Presenter )
                                        : RecyclerView.Adapter<AlbumsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            ViewHolder( LayoutInflater.from( parent.context)
                    .inflate( R.layout.album_list_item_save, parent, false))

    override fun getItemCount() = albums.size

    override fun onBindViewHolder( holder: ViewHolder, position: Int) {

        albums[ position ]?.let {

            holder.albumName?.text = it.name
            holder.albumArtist?.text = format( holder, R.string.album_by_artist, it.artist?.name)
            holder.albumPlayCount?.text = format( holder, R.string.album_play_count, it.playcount.toString() )

            Picasso.with( holder.itemView.context )
                     .load( Uri.parse( it.image?.get(2)?.text))
                     .error( R.mipmap.ic_last)
                     .fit()
                     .centerCrop()
                     .into( holder.albumImage )

            ViewCompat.setTransitionName( holder.albumImage, "transition_$position" )

            holder.rootView?.setOnClickListener { _->
                    presenter.showAlbumDetails( it.artist?.name!! , it.name.toString()  )
                }

         holder.albumSave?.setOnClickListener { _->
                    presenter.saveAlbumDetails( it.artist?.name!! , it.name.toString())
                }

         }

    }

    private fun format(holder : ViewHolder?, resId : Int, text : String?) : String?
            = holder?.itemView?.resources?.getString( resId, text)

    class ViewHolder( view: View) : RecyclerView.ViewHolder(view) {
        var rootView       : RelativeLayout? = view.rlAlbum
        var albumImage     : ImageView?  = view.ivAlbumImage
        var albumName      : TextView ?  = view.tvAlbumName
        var albumArtist    : TextView ?  = view.tvAlbumArtist
        var albumPlayCount : TextView ?  = view.tvAlbumPlayCount
        var albumSave      : Button?     = view.btSaveArtists
    }

}