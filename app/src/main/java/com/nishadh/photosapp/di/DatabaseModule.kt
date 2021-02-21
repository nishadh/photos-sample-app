package com.nishadh.photosapp.di

import android.content.Context
import androidx.room.Room
import com.nishadh.photosapp.data.PhotosRepository
import com.nishadh.photosapp.data.local.PhotoDatabase
import com.nishadh.photosapp.data.remote.PhotosService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDataBase(@ApplicationContext context: Context): PhotoDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            PhotoDatabase::class.java,
            "Photos.db"
        ).build()
    }

    @Singleton
    @Provides
    fun provideIoDispatcher() = Dispatchers.IO

    @Singleton
    @Provides
    fun provideTasksRepository(
        database: PhotoDatabase,
        photosService: PhotosService,
        ioDispatcher: CoroutineDispatcher
    ): PhotosRepository {
        return PhotosRepository(
            database.photoDao(), photosService, ioDispatcher
        )
    }
}