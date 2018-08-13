package eu.caraus.appsflastfm.ui.search.artists

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
import eu.caraus.appsflastfm.data.domain.lastFm.artists.ArtistItem
import eu.caraus.appsflastfm.ui.main.albumdetails.AlbumDetailsAdapter
import kotlinx.android.synthetic.main.artist_list_item.view.*

class ArtistsAdapter( var artists   : List<ArtistItem?>,
                      val presenter : ArtistsContract.Presenter )
                                        : RecyclerView.Adapter<ArtistsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            ViewHolder( LayoutInflater.from( parent.context)
                    .inflate( R.layout.artist_list_item, parent, false))

    override fun getItemCount() = artists.size

    override fun onBindViewHolder( holder: ViewHolder, position: Int) {

         artists[ position ]?.let {

            holder.artistName?.text = it.name
            holder.artistInfo?.text = format( holder, R.string.artist_listeners, it.listeners)

            Picasso.with( holder.itemView.context )
                     .load( Uri.parse( it.image?.get(2)?.text))
                     .error( R.mipmap.ic_last)
                     .fit()
                     .centerCrop()
                     .into( holder.artistImage )

            holder.rootView?.setOnClickListener { view ->
                    presenter.showTopAlbums( it.name.toString() )
                }

         }

    }

    private fun format(holder : ViewHolder?, resId : Int, text : String?) : String?
            = holder?.itemView?.resources?.getString( resId, text)

    class ViewHolder( view: View) : RecyclerView.ViewHolder(view) {
        var rootView    : RelativeLayout? = view.rlArtist
        var artistImage : ImageView?  = view.ivArtistImage
        var artistName  : TextView ?  = view.tvArtistName
        var artistInfo  : TextView ?  = view.tvArtistInfo
    }

}