package com.hestabit.sparkmatch.repository

import android.net.Uri
import kotlinx.coroutines.flow.Flow

interface StorageRepository {

    suspend fun uploadImage(imageUri: Uri, folderPath: String): String?

    suspend fun uploadMultipleImages(imageUris: List<Uri>, folderPath: String): List<String>

    suspend fun deleteImage(imageUrl: String): Boolean

    fun getUploadProgress(): Flow<Int>

}