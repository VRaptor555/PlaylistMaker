package com.example.playlistmaker.tracks

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R

class TrackViewHolder private constructor(itemView: View): ViewHolder(itemView) {
    private val trackView: TextView = itemView.findViewById(R.id.textNameTrack)
    private val artistView: TextView = itemView.findViewById(R.id.textArtist)
    private val timeView: TextView = itemView.findViewById(R.id.textTime)
    private val trackImage: ImageView = itemView.findViewById(R.id.imageTrack)

    fun bind(track: Track) {
        trackView.text = track.trackName
        artistView.text = track.artistName
        timeView.text = track.trackTime
        Glide.with(itemView.context)
            .load(track.artworkUrl100)
            .placeholder(R.drawable.ic_launcher_background)
            .transform(RoundedCorners(2))
            .into(trackImage)
    }

    companion object {
        fun createInstance(parent: ViewGroup): TrackViewHolder {
            val items = LayoutInflater.from(parent.context).inflate(R.layout.track_card, parent, false)
            return TrackViewHolder(items)
        }
    }
}