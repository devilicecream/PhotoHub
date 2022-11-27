package com.walterda.photohub.features.gallery.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.walterda.photohub.core.utils.Constants
import com.walterda.photohub.core.utils.IConnectivity
import com.walterda.photohub.features.gallery.data.repositories.IAlbumRepository
import com.walterda.photohub.features.gallery.domain.models.AlbumListItem
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class AlbumsViewModel @Inject constructor(
    private val albumRepository: IAlbumRepository,
    private val connectivity: IConnectivity
) : ViewModel() {

    companion object {
        private const val TAG = "AlbumViewModel"
    }

    private val _pagedAlbumList : MutableLiveData<PagingData<AlbumListItem>> = MutableLiveData()
    val pagedAlbumList : LiveData<PagingData<AlbumListItem>> get() = _pagedAlbumList

    private val _errorMessage: MutableLiveData<String> = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    private val _loading: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading


    fun getAlbums() {
        if (connectivity.hasInternetConnection()) {
            _loading.value = true
            viewModelScope.launch {
                albumRepository.getAlbums(1).catch {
                    Log.e(TAG, "getAlbums: Exception")
                    _loading.value = false
                    _errorMessage.value = Constants.ERROR_MESSAGE
                }.cachedIn(viewModelScope).collectLatest {
                    _loading.value = false
                    _pagedAlbumList.value = it
                }
            }
        } else {
            _errorMessage.value = Constants.NO_NETWORK_CONNECTION
        }
    }

    fun showLoader(show:Boolean) {
        _loading.value = show
    }

}