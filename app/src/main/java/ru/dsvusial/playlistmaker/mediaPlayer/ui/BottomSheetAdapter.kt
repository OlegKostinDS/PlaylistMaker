package ru.dsvusial.playlistmaker.mediaPlayer.ui

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import ru.dsvusial.playlistmaker.R
import ru.dsvusial.playlistmaker.addPlaylist.domain.model.PlaylistData
import ru.dsvusial.playlistmaker.utils.ConvertUtil

class BottomSheetAdapter(val listener: PlaylistListener) :
    RecyclerView.Adapter<BottomSheetAdapter.BottomSheetViewHolder>() {


    val recentPlaylists: ArrayList<PlaylistData> = ArrayList<PlaylistData>()

    class BottomSheetViewHolder(item: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(item.context).inflate(R.layout.bottom_sheet_item, item, false)
    ) {
        private val imageTitle = itemView.findViewById<ImageView>(R.id.bottom_image)
        private val plName = itemView.findViewById<TextView>(R.id.bottom_name)
        private val plAmount = itemView.findViewById<TextView>(R.id.bottom_amount)

        fun bind(model: PlaylistData) {

            val play = Uri.parse(model.playlistUri)
            val cornerRadius =
                itemView.resources.getDimensionPixelSize(R.dimen.lesser_btn_radius)
            Glide.with(itemView.context)
                .load(play)
                .placeholder(R.drawable.nodata)
                .transform(CenterCrop(),RoundedCorners(cornerRadius))
                .into(imageTitle)
            plName.text = model.playlistName
            plAmount.text = ConvertUtil.conventAmountToTrackString(model.playlistAmount)

        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        BottomSheetViewHolder(parent)

    override fun getItemCount() = recentPlaylists.size

    override fun onBindViewHolder(holder: BottomSheetViewHolder, position: Int) {

        holder.bind(recentPlaylists[position])
        holder.itemView.setOnClickListener {
            listener.onClick(recentPlaylists[position])
        }

    }

    fun interface PlaylistListener {
        fun onClick(playlistData: PlaylistData)
    }


}
