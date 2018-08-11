package eu.caraus.appsflastfm.ui.search.albumdetails

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import eu.caraus.appsflastfm.R
import eu.caraus.appsflastfm.data.domain.lastFm.albuminfo.TrackItem
import kotlinx.android.synthetic.main.album_details_track_item.view.*


class AlbumDetailsAdapter(var tracks : List<TrackItem?>,
                          val presenter : AlbumDetailsContract.Presenter )
                                : RecyclerView.Adapter<AlbumDetailsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            ViewHolder( LayoutInflater.from( parent.context)
                    .inflate( R.layout.album_details_track_item, parent, false))

    override fun getItemCount() = tracks.size

    override fun onBindViewHolder( holder: ViewHolder, position: Int) {

        tracks[ position ]?.let {

            holder.trackName?.text = it.name
            holder.trackDuration?.text = it.duration

//                       holder.trackPlay?.setOnClickListener { _->
                //                    presenter.playTrack( it )
//                }

        }

    }

    class ViewHolder( view: View) : RecyclerView.ViewHolder(view) {
        var trackName      : TextView?   = view.tvTrackName
        var trackDuration  : TextView?   = view.tvTrackDuration
        var trackPlay      : ImageView?  = view.ivTrackPlay
    }

}