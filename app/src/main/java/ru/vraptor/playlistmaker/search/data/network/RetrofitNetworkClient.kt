package ru.vraptor.playlistmaker.search.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import ru.vraptor.playlistmaker.search.data.Response
import ru.vraptor.playlistmaker.search.data.TracksSearchRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RetrofitNetworkClient(
    private val trackService: ITunesApiService,
    private val context: Context
): NetworkClient {

    override suspend fun doRequest(dto: Any): Response {
        if (!isConnected()) {
            return Response().apply { resultCode = -1 }
        }
        return withContext(Dispatchers.IO) {
            try {
                when(dto) {
                    is TracksSearchRequest -> {
                        Log.d("PLAYLIST_TEST", "Dtotext: ${dto.text}")
                        val response = trackService.searchTracks(dto.text)
                        Log.d("PLAYLIST_TEST", "Response")
                        response.apply { resultCode = 200 }
                    }
                    else -> Response().apply { resultCode = 400 }
                }
            } catch (e: Throwable) {
                Log.e("PLAYLIST_TEST", "Error: ${e.stackTraceToString()}")
                Response().apply { resultCode = 500 }
            }
        }
    }

    private fun isConnected(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
            }
        }
        return false
    }
}