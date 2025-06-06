package ru.vraptor.playlistmaker.settings.ui.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import ru.vraptor.playlistmaker.settings.domain.SettingsInteractor
import ru.vraptor.playlistmaker.settings.domain.model.AppSettings
import ru.vraptor.playlistmaker.settings.ui.models.SettingsState
import ru.vraptor.playlistmaker.sharing.domain.SharingInteractor
import ru.vraptor.playlistmaker.sharing.domain.model.EmailData

class SettingsViewModel(
    application: Application,
    private val settingsInteractor: SettingsInteractor,
    private val sharedInteractor: SharingInteractor
): AndroidViewModel(application) {

    private val settingsLiveData = MutableLiveData<SettingsState>()
    private val mediatorSettingsStateLiveData = MediatorLiveData<SettingsState>().also { liveData ->
        liveData.addSource(settingsLiveData) { settingState ->
            liveData.value = when(settingState) {
                is SettingsState.GetSettings -> SettingsState.GetSettings(settingState.settings)
                is SettingsState.SetSettings -> SettingsState.SetSettings(settingState.settings)
                is SettingsState.EmailIntent -> SettingsState.EmailIntent(settingState.email)
                is SettingsState.ShareIntent -> SettingsState.ShareIntent(settingState.share)
                is SettingsState.UrlIntent -> SettingsState.UrlIntent(settingState.url)
            }
        }
    }

    fun observeState(): LiveData<SettingsState> = mediatorSettingsStateLiveData

    fun getSettings() {
        renderState(SettingsState.GetSettings(settingsInteractor.getSettings()))
    }

    fun setSettings(settings: AppSettings) {
        settingsInteractor.setSettings(settings)
        renderState(SettingsState.SetSettings(settingsInteractor.getSettings()))
    }

    fun sendEmail(mail: EmailData) {
        renderState(SettingsState.EmailIntent(sharedInteractor.sendEmail(mail)))
    }

    fun sendShare(message: String, messageSubj: String) {
        renderState(SettingsState.ShareIntent(sharedInteractor.share(message, messageSubj)))
    }

    fun sendUrl(url: String) {
        renderState(SettingsState.UrlIntent(sharedInteractor.sendUrl(url)))
    }


    private fun renderState(state: SettingsState) {
        settingsLiveData.postValue(state)
    }
}