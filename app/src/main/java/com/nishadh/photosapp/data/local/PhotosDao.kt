package com.nishadh.photosapp.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for the tasks table.
 */
@Dao
interface PhotosDao {

    /** Select all tasks from the tasks table.
     *
     * @return all tasks.
     */
    @Query("SELECT * FROM Photos ORDER BY position ASC")
    fun getPhotos(): Flow<List<Photo>>

    /**
     * Select a task by id.
     *
     * @param taskId the task id.
     * @return the task with taskId.
     */
    @Query("SELECT * FROM Photos WHERE entryid = :photoId")
    suspend fun getPhotoById(photoId: String): Photo?

    /**
     * Insert a task in the database. If the task already exists, replace it.
     *
     * @param task the task to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPhoto(photo: Photo)

    /**
     * Update a task.
     *
     * @param task task to be updated
     * @return the number of tasks updated. This should always be 1.
     */
    @Update
    suspend fun updateTask(photo: Photo): Int

    /**
     * Delete a task by id.
     *
     * @return the number of tasks deleted. This should always be 1.
     */
    @Query("DELETE FROM Photos WHERE entryid = :photoId")
    suspend fun deletePhotoById(photoId: String): Int

}
