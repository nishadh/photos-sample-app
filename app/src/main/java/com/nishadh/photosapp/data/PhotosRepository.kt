package com.nishadh.photosapp.data

import kotlinx.coroutines.withContext
import com.nishadh.photosapp.data.local.Photo
import com.nishadh.photosapp.data.local.PhotosDao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow

class PhotosRepository constructor(private val photosDao: PhotosDao, private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO) {

    val photosFlow: Flow<List<Photo>>
        get() = photosDao.getPhotos()

    suspend fun savePhoto(photo: Photo) = withContext(ioDispatcher) {
        photosDao.insertPhoto(photo)
    }
}