package eu.caraus.appsflastfm.ui.main.albumdetails

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SeekBar
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

            it.duration?.toInt()?.let { max ->
                holder.trackSeekTimeline?.max = max
                holder.trackSeekTimeline?.progress = it.trackElapsed
            }

            when( it.trackState ){

                TrackState.PLAYING -> {
                    holder.trackMediaButton?.setImageResource(R.drawable.pause)
                    holder.trackDuration?.text = formatTrackLength( it.trackElapsed.toString())
                    holder.trackSeekTimeline?.visibility = View.VISIBLE
                }

                TrackState.STOPPED -> {
                    holder.trackMediaButton?.setImageResource(R.drawable.play)
                    holder.trackDuration?.text = formatTrackLength( it.duration!!)
                    holder.trackSeekTimeline?.visibility = View.GONE
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

    inner class ViewHolder( view: View ) : RecyclerView.ViewHolder(view) {

        var trackName      : TextView?   = view.tvTrackName
        var trackDuration  : TextView?   = view.tvTrackDuration
        var trackMediaButton      : ImageView?  = view.ivTrackPlay
        var trackSeekTimeline  : SeekBar?    = view.sbTrackTimeline

        init {

            trackMediaButton?.setOnClickListener {
                val track = tracks[ adapterPosition ]
                when( track?.trackState ){
                    TrackState.PLAYING -> presenter.triggerStopTrack( track.apply { trackState = TrackState.STOPPED } )
                    TrackState.STOPPED -> {
                        presenter.triggerPlayTrack( track.apply { trackState = TrackState.PLAYING } )
                        (it as ImageView).setImageResource(R.drawable.pause)
                    }
                }
            }

            trackSeekTimeline?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {}
                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    seekBar?.let {
                        presenter.triggerSeekTo( it.progress )
                    }
                }
            })

        }

    }

}