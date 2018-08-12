package eu.caraus.appsflastfm.ui.main.albumdetails

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import eu.caraus.appsflastfm.R
import eu.caraus.appsflastfm.data.domain.extensions.lastFm.TrackState
import eu.caraus.appsflastfm.data.domain.lastFm.albuminfo.TrackItem
import eu.caraus.appsflastfm.services.youtube.model.youtube.YouTubeVideo
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

            when( it.trackState ){

                TrackState.PLAYING -> {
                    holder.trackPlay?.setImageResource(R.drawable.pause)
                    holder.trackDuration?.text = formatTrackLength( it.trackElapsed.toString())
                }

                TrackState.STOPPED -> {
                    holder.trackPlay?.setImageResource(R.drawable.play)
                    holder.trackDuration?.text = formatTrackLength( it.duration!!)
                }

            }

            holder.trackPlay?.setOnClickListener { _->
                    when( it.trackState ){
                        TrackState.PLAYING -> presenter.triggerStopTrack( it.apply { trackState = TrackState.STOPPED } )
                        TrackState.STOPPED -> presenter.triggerPlayTrack( it.apply { trackState = TrackState.PLAYING } )
                    }
                }

        }

    }

    fun updateTrack( youTubeVideo: YouTubeVideo ){
        tracks.forEachIndexed { index , track ->
            track?.let {
                if( it.id == youTubeVideo.trackId ){
                    it.trackState   = youTubeVideo.trackState
                    it.trackElapsed = youTubeVideo.trackElapsed
                    notifyItemChanged( index )
                }
            }
        }
    }

    private fun formatTrackLength( length : String ) : String {

        val duration = length.toIntOrNull()

        duration?.let {

            val minutes = it / 60
            val seconds = it % 60

            return String.format("%02d:%02d",minutes,seconds)

        } ?: run {
            return length
        }

    }

    class ViewHolder( view: View) : RecyclerView.ViewHolder(view) {
        var trackName      : TextView?   = view.tvTrackName
        var trackDuration  : TextView?   = view.tvTrackDuration
        var trackPlay      : ImageView?  = view.ivTrackPlay
    }

}