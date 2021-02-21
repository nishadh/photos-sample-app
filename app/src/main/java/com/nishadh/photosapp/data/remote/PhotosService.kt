package com.nishadh.photosapp.data.remote

import retrofit2.Response
import retrofit2.http.GET

interface PhotosService {

    @GET("list")
    suspend fun getPhotos(): Response<List<PhotoDto>>

    companion object {
        const val PHOTOS_API_URL = "https://picsum.photos/v2/"
    }

}