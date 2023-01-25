package ru.dsvusial.playlistmaker

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class TrackAdapter(private val tracks: ArrayList<Track>) :
    RecyclerView.Adapter<TrackAdapter.TrackViewHolder>() {

    class TrackViewHolder(item: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(item.context).inflate(R.layout.track_item, item, false)
    ) {
        private val imageTitle = itemView.findViewById<ImageView>(R.id.rv_image_title)
        private val rvTrackName = itemView.findViewById<TextView>(R.id.rv_track_name)
        private val rvArtistName = itemView.findViewById<TextView>(R.id.rv_artist_name)
        private val rvTrackDuration = itemView.findViewById<TextView>(R.id.rv_track_duration)

        fun bind(model: Track) {
            Glide.with(itemView.context)
                .load(model.artworkUrl100)
                .placeholder(R.drawable.nodata)
                .transform(RoundedCorners(2))
                .centerCrop()
                .into(imageTitle)
            rvTrackName.text = model.trackName
            rvArtistName.text = model.artistName
            rvTrackDuration.text = model.trackTime
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        TrackViewHolder(parent)

    override fun getItemCount() = tracks.size

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) =
        holder.bind(tracks[position])
}