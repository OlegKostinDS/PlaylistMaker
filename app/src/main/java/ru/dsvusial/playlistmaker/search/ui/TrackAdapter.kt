package ru.dsvusial.playlistmaker.search.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import ru.dsvusial.playlistmaker.R
import ru.dsvusial.playlistmaker.mediaPlayer.domain.model.TrackData
import ru.dsvusial.playlistmaker.utils.DateTimeUtil

const val SEARCH_KEY = "search_key"


class TrackAdapter(
    var onItemClick: ((TrackData) -> Unit)? = null,
    var onItemLongClick: ((TrackData) -> Unit)? = null,
    var imageQuality : String = "100x100bb.jpg"
) :
    RecyclerView.Adapter<TrackAdapter.TrackViewHolder>() {

    val recentTracks: ArrayList<TrackData> = ArrayList<TrackData>()

    class TrackViewHolder(item: ViewGroup, var imageQuality: String) : RecyclerView.ViewHolder(
        LayoutInflater.from(item.context).inflate(R.layout.track_item, item, false)
    ) {
        private val imageTitle = itemView.findViewById<ImageView>(R.id.rv_image_title)
        private val rvTrackName = itemView.findViewById<TextView>(R.id.rv_track_name)
        private val rvArtistName = itemView.findViewById<TextView>(R.id.rv_artist_name)
        private val rvTrackDuration = itemView.findViewById<TextView>(R.id.rv_track_duration)
        fun bind(model: TrackData) {
            val cornerRadius =
                itemView.resources.getDimensionPixelSize(R.dimen.lesser_btn_radius)
            Log.d("TAG", "bind:$imageQuality ")
            model.artworkUrl100 = model.artworkUrl100.replaceAfterLast('/', imageQuality)
            Glide.with(itemView.context)
                .load(model.artworkUrl100)
                .placeholder(R.drawable.nodata)
                .transform(CenterCrop(),RoundedCorners(cornerRadius))
                .into(imageTitle)
            rvTrackName.text = model.trackName
            rvArtistName.text = model.artistName
            rvTrackDuration.text = DateTimeUtil.formatTimeMillisToString(model.trackTimeMillis)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        TrackViewHolder(parent, imageQuality)

    override fun getItemCount() = recentTracks.size

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {

        holder.bind(recentTracks[position])
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(recentTracks[position])
        }
        holder.itemView.setOnLongClickListener {
            onItemLongClick?.invoke(recentTracks[position])
            return@setOnLongClickListener true
        }
    }


}
