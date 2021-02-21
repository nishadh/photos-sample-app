package com.nishadh.photosapp.di

import android.content.Context
import androidx.room.Room
import com.nishadh.photosapp.data.PhotosRepository
import com.nishadh.photosapp.data.local.PhotoDatabase
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
object AppModule {

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
}

/**
 * The binding for TasksRepository is on its own module so that we can replace it easily in tests.
 */
@Module
@InstallIn(SingletonComponent::class)
object PhotosRepositoryModule {

    @Singleton
    @Provides
    fun provideTasksRepository(
        database: PhotoDatabase,
        ioDispatcher: CoroutineDispatcher
    ): PhotosRepository {
        return PhotosRepository(
            database.photoDao(), ioDispatcher
        )
    }
}