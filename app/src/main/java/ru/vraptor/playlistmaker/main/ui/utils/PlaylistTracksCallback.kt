package ru.vraptor.playlistmaker.main.ui.utils

import androidx.recyclerview.widget.DiffUtil
import ru.vraptor.playlistmaker.search.domain.model.Track

class PlaylistTracksCallback(
    private val oldList: List<Track>,
    private val newList: List<Track>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(
        oldItemPosition: Int,
        newItemPosition: Int
    ): Boolean {
        return oldList[oldItemPosition].trackId == newList[newItemPosition].trackId
    }

    override fun areContentsTheSame(
        oldItemPosition: Int,
        newItemPosition: Int
    ): Boolean {
        val (_, trackName, artistName) = oldList[oldItemPosition]
        val (_, trackName1, artistName1) = newList[newItemPosition]
        return trackName == trackName1 && artistName == artistName1
    }

    override fun getChangePayload(
        oldItemPosition: Int,
        newItemPosition: Int
    ): Any? {
        return super.getChangePayload(oldItemPosition, newItemPosition)
    }
}