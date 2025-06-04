package ru.vraptor.playlistmaker.main.ui.utils

import androidx.recyclerview.widget.DiffUtil
import ru.vraptor.playlistmaker.library.domain.model.Playlist

class PlaylistCallback(
    private val oldList: List<Playlist>,
    private val newList: List<Playlist>

) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(
        oldItemPosition: Int,
        newItemPosition: Int
    ): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(
        oldItemPosition: Int,
        newItemPosition: Int
    ): Boolean {
        val (_, name, description, image, _, count) = oldList[oldItemPosition]
        val (_, name1, description1, image1, _, count1) = newList[newItemPosition]
        return name == name1 && description == description1 && image == image1 && count == count1
    }

    override fun getChangePayload(
        oldItemPosition: Int,
        newItemPosition: Int
    ): Any? {
        return super.getChangePayload(oldItemPosition, newItemPosition)
    }
}