package ru.dsvusial.playlistmaker.music_library.ui

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import ru.dsvusial.playlistmaker.R
import ru.dsvusial.playlistmaker.addPlaylist.domain.model.PlaylistData
import ru.dsvusial.playlistmaker.utils.NUMBER_FOR_FIRST_ITERATION
import ru.dsvusial.playlistmaker.utils.NUMBER_FOR_SECOND_ITERATION

class PlaylistAdapter(val listener: PlaylistListener) :
    RecyclerView.Adapter<PlaylistAdapter.PlaylistViewHolder>() {


    val recentPlaylists: ArrayList<PlaylistData> = ArrayList<PlaylistData>()

    class PlaylistViewHolder(item: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(item.context).inflate(R.layout.playlist_item, item, false)
    ) {
        private val imageTitle = itemView.findViewById<ImageView>(R.id.playlist_image)
        private val plName = itemView.findViewById<TextView>(R.id.playlist_name_layout)
        private val plAmount = itemView.findViewById<TextView>(R.id.tracks_amount)

        fun bind(model: PlaylistData) {
            val play = Uri.parse(model.playlistUri)
            val cornerRadius =
                itemView.resources.getDimensionPixelSize(R.dimen.lesser_btn_radius)
            Glide.with(itemView.context)
                .load(play)
                .placeholder(R.drawable.nodata)
                .centerCrop()
                .transform(RoundedCorners(cornerRadius))
                .into(imageTitle)
            plName.text = model.playlistName
            plAmount.text = conventAmountToString(model.playlistAmount)

        }

        private fun conventAmountToString(amount: Int): String {
            var temp = amount % NUMBER_FOR_FIRST_ITERATION
            if (temp in 5..20)
                return "$amount треков"
            temp %= NUMBER_FOR_SECOND_ITERATION
            return when (temp) {
                1 -> "$amount трек"
                in (2..4) -> "$amount трека"
                else -> "$amount треков"
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        PlaylistViewHolder(parent)

    override fun getItemCount() = recentPlaylists.size

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {

        holder.bind(recentPlaylists[position])
        holder.itemView.setOnClickListener {
            listener.onClick(recentPlaylists[position])
        }

    }

    fun interface PlaylistListener {
        fun onClick(playlistData: PlaylistData)
    }


}
