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
                photo.imageUrl,
                photo.position
            )
        }
    }.asLiveData(Dispatchers.Default + viewModelScope.coroutineContext)
    var selectedPhoto = MutableLiveData<PhotoUio>()

    fun deletePhoto(position: Int) {
        viewModelScope.launch {
            photos.value?.get(position)?.let {
                repository.deletePhotoById(it.id)
            }
        }
    }

    fun swapPhotoPosition(firstPosition: Int, secondPosition: Int) {
        viewModelScope.launch {
            val firstPhoto = photos.value?.get(firstPosition)
            val secondPhoto = photos.value?.get(secondPosition)
            if (firstPhoto != null && secondPhoto != null ){
                val photo1Dto = Photo( firstPhoto.author, firstPhoto.imageUrl, secondPhoto.position, firstPhoto.id)
                val photo2Dto = Photo( secondPhoto.author, secondPhoto.imageUrl, firstPhoto.position, secondPhoto.id)
                repository.savePhotos(photo1Dto, photo2Dto)
            }
        }
    }

    fun addPhoto() {
        viewModelScope.launch {
            repository.addPhoto()
        }
    }

}