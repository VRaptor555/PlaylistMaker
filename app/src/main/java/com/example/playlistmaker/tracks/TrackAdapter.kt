package com.example.playlistmaker.tracks

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class TrackAdapter(val clickListener: TrackClickListener) : RecyclerView.Adapter<TrackViewHolder>() {
    var tracks = ArrayList<Track>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        return TrackViewHolder(parent)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks[position])
        holder.itemView.setOnClickListener { clickListener.onTrackClick(tracks[position]) }
    }

    override fun getItemCount(): Int {
        return tracks.size
    }

    fun interface TrackClickListener {
        fun onTrackClick(track: Track)
    }
}