package com.nishadh.photosapp.data

import kotlinx.coroutines.withContext
import com.nishadh.photosapp.data.local.Photo
import com.nishadh.photosapp.data.local.PhotosDao
import com.nishadh.photosapp.ui.main.PhotoUio
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow

class PhotosRepository constructor(private val photosDao: PhotosDao, private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO) {

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
//        photosDao.insertPhoto(photo)
        val photos = arrayOf(
            PhotoUio(
                "1",
                "Matthew Wiebe",
                "https://picsum.photos/id/1025/4951/3301",
                1
            ),
            PhotoUio(
                "2",
                "Мартин Тасев",
                "https://picsum.photos/id/1024/1920/1280",
                2
            ),
            PhotoUio(
                "3",
                "William Hook",
                "https://picsum.photos/id/1023/3955/2094",
                3
            ),
            PhotoUio(
                "4",
                "Vashishtha Jogi",
                "https://picsum.photos/id/1022/6000/3376",
                4
            ),
            PhotoUio(
                "5",
                "Frances Gunn",
                "https://picsum.photos/id/1021/2048/1206",
                5
            ),
            PhotoUio(
                "6",
                "Adam Willoughby-Knox",
                "https://picsum.photos/id/1020/4288/2848",
                6
            ),
            PhotoUio(
                "7",
                "Ben Moore",
                "https://picsum.photos/id/102/4320/3240",
                7

            )
        )

        val photo = photos.toList().shuffled().first()
        val position = (photosDao.getMaxPosition() ?: 0) + 1
        val photoDto = Photo(photo.author, photo.imageUrl, position)
        savePhoto(photoDto)
    }

}