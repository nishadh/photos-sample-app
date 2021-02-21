package com.nishadh.photosapp.data

import com.nishadh.photosapp.data.local.Photo
import com.nishadh.photosapp.data.local.PhotosDao
import com.nishadh.photosapp.data.remote.PhotoDto
import com.nishadh.photosapp.data.remote.PhotosService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext

class PhotosRepository constructor(private val photosDao: PhotosDao, private val photosService: PhotosService, private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO) {

    val photosFlow: Flow<List<Photo>>
        get() = photosDao.getPhotos()

    suspend fun savePhoto(photo: Photo) = withContext(ioDispatcher) {
        photosDao.insertPhoto(photo)
    }

    suspend fun deletePhotoById(photoId: String) = withContext(ioDispatcher) {
        photosDao.deletePhotoById(photoId)
    }

    suspend fun swapPhotos(firstPhotoId: String, secondPhotoId: String) = withContext(ioDispatcher) {
        var firstPhoto = photosDao.getPhotoById(firstPhotoId)
        var secondPhoto = photosDao.getPhotoById(secondPhotoId)
        if (firstPhoto != null && secondPhoto != null) {
            val firstPhotoPosition = firstPhoto.position
            firstPhoto.position = secondPhoto.position
            secondPhoto.position = firstPhotoPosition
            photosDao.insertPhotos(firstPhoto, secondPhoto)
        }
    }

    fun addRemotePhoto(): Flow<Boolean> {
        return flow {
            transform(photosService.getPhotos().body()!!).let {
                savePhoto(it)
                emit(true)
            }
        }.catch { throwable ->
            emit(false)
        }.flowOn(ioDispatcher)
    }

    private suspend fun transform(photos: List<PhotoDto>): Photo {
        return random(photos)
                ?.let {
                    val position = (photosDao.getMaxPosition() ?: 0) + 1
                    Photo(it.author!!, it.download_url!!, position)
                }
    }

    private fun random(photos: List<PhotoDto>): PhotoDto {
        return photos.filter { it.author != null && it.download_url != null }
                ?.shuffled()
                ?.first()
    }


}
