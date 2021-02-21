package com.nishadh.photosapp.ui.main

import androidx.lifecycle.*
import com.nishadh.photosapp.data.PhotosRepository
import com.nishadh.photosapp.data.local.Photo
import com.nishadh.photosapp.util.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: PhotosRepository): ViewModel() {

    var selectedPhoto = MutableLiveData<PhotoUio>()

    var photos = repository.photosFlow.map {
        it?.map { photo ->
            PhotoUio(
                photo.id,
                photo.author,
                photo.imageUrl,
                photo.position
            )
        }
    }.asLiveData(Dispatchers.Default + viewModelScope.coroutineContext)

    private val _apiResult = MutableLiveData<Event<Boolean>>()
    val apiResult :LiveData<Event<Boolean>>
        get() = _apiResult

    private var _loading = MutableLiveData<Boolean>(false)
    val loading :LiveData<Boolean>
        get() = _loading

    fun deletePhoto(position: Int) {
        viewModelScope.launch {
            photos.value?.get(position)?.let {
                repository.deletePhotoById(it.id)
            }
        }
    }

    fun swapPhotoPosition(firstPosition: Int, secondPosition: Int) {
        viewModelScope.launch {
            photos.value?.get(firstPosition)?.let { firstPhoto ->
                photos.value?.get(secondPosition)?.let { secondPhoto ->
                    repository.swapPhotos(firstPhoto.id, secondPhoto.id)
                }
            }
        }
    }

    fun addPhoto() {
        if (_loading.value == true) {
            return
        }
        viewModelScope.launch {
            _loading.value = true
            repository.addRemotePhoto().collect { success ->
                _loading.value = false
                _apiResult.value = Event(success)
            }
        }
    }
}