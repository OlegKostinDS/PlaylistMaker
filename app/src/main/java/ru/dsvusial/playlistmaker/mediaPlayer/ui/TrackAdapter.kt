package ru.dsvusial.playlistmaker.mediaPlayer.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import ru.dsvusial.playlistmaker.R
import ru.dsvusial.playlistmaker.mediaPlayer.domain.model.TrackData
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

const val SEARCH_KEY = "search_key"


class TrackAdapter(val listener: HistoryListener) :
    RecyclerView.Adapter<TrackAdapter.TrackViewHolder>() {

    val recentTracks: ArrayList<TrackData> = ArrayList<TrackData>()

    class TrackViewHolder(item: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(item.context).inflate(R.layout.track_item, item, false)
    ) {
        private val imageTitle = itemView.findViewById<ImageView>(R.id.rv_image_title)
        private val rvTrackName = itemView.findViewById<TextView>(R.id.rv_track_name)
        private val rvArtistName = itemView.findViewById<TextView>(R.id.rv_artist_name)
        private val rvTrackDuration = itemView.findViewById<TextView>(R.id.rv_track_duration)
        private val rvDetails = itemView.findViewById<ImageButton>(R.id.rv_details_btn)

        fun bind(model: TrackData) {
            Glide.with(itemView.context)
                .load(model.artworkUrl100)
                .placeholder(R.drawable.nodata)
                .transform(RoundedCorners(2))
                .centerCrop()
                .into(imageTitle)
            rvTrackName.text = model.trackName
            rvArtistName.text = model.artistName
            rvTrackDuration.text =
                SimpleDateFormat("mm:ss", Locale.getDefault()).format(model.trackTimeMillis)
                    .toString()

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        TrackViewHolder(parent)

    override fun getItemCount() = recentTracks.size

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {

        holder.bind(recentTracks[position])
        holder.itemView.setOnClickListener {
            listener.onClick(recentTracks[position])
        }

    }

    fun interface HistoryListener {
        fun onClick(trackData: TrackData)
    }


}
