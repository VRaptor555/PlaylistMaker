package ru.vraptor.playlistmaker.library.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import ru.vraptor.playlistmaker.R
import ru.vraptor.playlistmaker.search.domain.model.Track
import ru.vraptor.playlistmaker.utils.pxToDp
import ru.vraptor.playlistmaker.utils.timeMillisToMin

class FavoriteViewHolder private constructor(itemView: View) : ViewHolder(itemView) {
    private val trackView: TextView = itemView.findViewById(R.id.text_name_track)
    private val artistView: TextView = itemView.findViewById(R.id.text_artist)
    private val timeView: TextView = itemView.findViewById(R.id.text_time)
    private val trackImage: ImageView = itemView.findViewById(R.id.image_track)

    constructor(
        parent: ViewGroup
    ) : this(
        LayoutInflater.from(parent.context).inflate(R.layout.track_card, parent, false)
    )

    @SuppressLint("SimpleDateFormat")
    fun bind(track: Track) {
        trackView.text = track.trackName
        artistView.text = track.artistName
        timeView.text = timeMillisToMin(track.trackTimeMillis)
        Glide.with(itemView.context)
            .load(track.artworkUrl100)
            .placeholder(R.drawable.placeholder)
            .transform(RoundedCorners(pxToDp(2f, itemView.context)))
            .into(trackImage)
    }
}