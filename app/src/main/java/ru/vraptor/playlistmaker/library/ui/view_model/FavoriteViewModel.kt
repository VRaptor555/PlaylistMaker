package ru.vraptor.playlistmaker.library.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ru.vraptor.playlistmaker.library.ui.models.FavoriteState
import ru.vraptor.playlistmaker.search.domain.db.FavoriteInteractor
import ru.vraptor.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.launch

class FavoriteViewModel(
    private val favoriteInteractor: FavoriteInteractor
): ViewModel() {
    private val stateLiveData = MutableLiveData<FavoriteState>()
    fun observeState(): LiveData<FavoriteState> = stateLiveData

    init {
        showFavorite()
    }

    fun showFavorite() {
        viewModelScope.launch {
            favoriteInteractor
                .getTracks()
                .collect { tracks ->
                    showContent(tracks)
                }
        }
    }

    private fun showContent(tracks: List<Track>) {
        if (tracks.isEmpty()) {
            stateLiveData.postValue(FavoriteState.Empty)
        } else {
            stateLiveData.postValue(FavoriteState.Content(tracks))
        }
    }
}