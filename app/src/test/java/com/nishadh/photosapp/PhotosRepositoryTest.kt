package com.nishadh.photosapp

import com.nhaarman.mockitokotlin2.*
import com.nishadh.photosapp.data.PhotosRepository
import com.nishadh.photosapp.data.local.Photo
import com.nishadh.photosapp.data.local.PhotosDao
import com.nishadh.photosapp.data.remote.PhotoDto
import com.nishadh.photosapp.data.remote.PhotosService
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Assert.*
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import retrofit2.Response


class PhotosRepositoryTest {
    val testDispatcher = TestCoroutineDispatcher()
    val photosService = mock<PhotosService>()
    val photosDao = mock<PhotosDao>()
    val repository = PhotosRepository(photosDao, photosService, testDispatcher)

    @Test
    fun `flow emits successfully`() = runBlocking {

        // Mock API Service
        val photos = listOf(
                PhotoDto("id1", "author1", "download_url")
        )
        photosService.stub {
            onBlocking { getPhotos() } doReturn Response.success(photos)
        }
        photosDao.stub {
            onBlocking { getMaxPosition() } doReturn 0
        }
        // Test
        repository.addRemotePhoto().collect { success ->
            assertTrue(success)
            verify(photosDao).insertPhoto(any())
        }
    }

    @Test
    fun `flow emits failure`() = runBlocking {

        // Mock API Service
        val photos = listOf(
                PhotoDto("id1", "author1", "download_url")
        )
        photosService.stub {
            onBlocking { getPhotos() } doReturn Response.success(listOf())
        }
        photosDao.stub {
            onBlocking { getMaxPosition() } doReturn 0
        }
        // Test
        repository.addRemotePhoto().collect { success ->
            assertFalse(success)
        }
    }

    @Test
    fun `swapPhotos swaps position`() = runBlocking {
        val photo1 = Photo("author1", "download_url1", 1)
        val photo2 = Photo("author2", "download_url2", 2)
        photosDao.stub {
            onBlocking { getPhotoById(any()) }.doReturn(photo1).doReturn(photo2)
        }
        val photoArgumentCaptor = argumentCaptor<Photo>()
        repository.swapPhotos(photo1.id, photo2.id)
        verify(photosDao).insertPhotos(photoArgumentCaptor.capture(), photoArgumentCaptor.capture());
        assertEquals(2, photoArgumentCaptor.firstValue.position);
        assertEquals(1, photoArgumentCaptor.secondValue.position);
    }
    
}