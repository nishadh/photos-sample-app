package com.nishadh.photosapp.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.nishadh.photosapp.data.PhotosRepository
import com.nishadh.photosapp.data.local.Photo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: PhotosRepository): ViewModel() {
    var photos = repository.photosFlow.map {
        it?.map { photo ->
            PhotoUio(
                photo.id,
                photo.author,
                photo.imageUrl
            )
        }
    }.asLiveData(Dispatchers.Default + viewModelScope.coroutineContext)
    var selectedPhoto = MutableLiveData<PhotoUio>()

    fun addNewPhoto(photo: PhotoUio) {
        viewModelScope.launch {
            val newPostion = photos.value?.let { it.size } ?: 0
            val photoDto = Photo(photo.author, photo.imageUrl, newPostion)
            repository.savePhoto(photoDto)
        }
    }

}