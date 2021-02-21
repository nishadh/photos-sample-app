package com.nishadh.photosapp.data

import kotlinx.coroutines.withContext
import com.nishadh.photosapp.data.local.Photo
import com.nishadh.photosapp.data.local.PhotosDao
import com.nishadh.photosapp.data.remote.PhotosService
import com.nishadh.photosapp.ui.main.PhotoUio
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow

class PhotosRepository constructor(private val photosDao: PhotosDao, private val photosService: PhotosService, private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO) {

    val photosFlow: Flow<List<Photo>>
        get() = photosDao.getPhotos()

    suspend fun savePhoto(photo: Photo) = withContext(ioDispatcher) {
        photosDao.insertPhoto(photo)
    }

    suspend fun deletePhotoById(photoId: String) = withContext(ioDispatcher) {
        photosDao.deletePhotoById(photoId)
    }

    suspend fun savePhotos(photo1: Photo, photo2: Photo) = withContext(ioDispatcher) {
        photosDao.insertPhotos(photo1, photo2)
    }

    suspend fun addPhoto() = withContext(ioDispatcher) {
        val photoList = photosService.getPhotos().body()
        photoList?.shuffled()
            ?.filter {
                it.author != null && it.download_url != null
            }
            ?.shuffled()
            ?.first()
            ?.let {
                val position = (photosDao.getMaxPosition() ?: 0) + 1
                val photoDto = Photo(it.author!!, it.download_url!!, position)
                savePhoto(photoDto)
            }
    }

}