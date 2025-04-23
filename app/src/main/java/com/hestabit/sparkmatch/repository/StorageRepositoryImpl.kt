package com.hestabit.sparkmatch.repository

import android.net.Uri
import android.util.Log
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StorageRepositoryImpl @Inject constructor(
    private val storage: FirebaseStorage
) : StorageRepository {
    private val storageRef = storage.reference
    private val _uploadProgress = MutableStateFlow(0)

    override fun getUploadProgress(): Flow<Int> = _uploadProgress.asStateFlow()

    override suspend fun uploadImage(imageUri: Uri, folderPath: String): String? {
        return try {
            val filename = UUID.randomUUID().toString()
            val fileRef = storageRef.child("$folderPath/$filename")

            uploadFileWithProgress(imageUri, fileRef)
        } catch (e: Exception) {
            Log.e(TAG, "Error uploading image: ${e.message}")
            null
        } finally {
            _uploadProgress.value = 0
        }
    }

    // In StorageRepositoryImpl.kt, add more logging:
    override suspend fun uploadMultipleImages(imageUris: List<Uri>, folderPath: String): List<String> {
        Log.d(TAG, "Starting upload of ${imageUris.size} images to $folderPath")
        val uploadedUrls = mutableListOf<String>()

        try {
            for ((index, uri) in imageUris.withIndex()) {
                Log.d(TAG, "Uploading image $index/${imageUris.size}")
                val filename = UUID.randomUUID().toString()
                val fileRef = storageRef.child("$folderPath/$filename")

                val downloadUrl = uploadFileWithProgress(uri, fileRef)
                if (downloadUrl != null) {
                    Log.d(TAG, "Image $index uploaded successfully: $downloadUrl")
                    uploadedUrls.add(downloadUrl)
                } else {
                    Log.e(TAG, "Failed to upload image $index")
                }
            }
            Log.d(TAG, "Completed uploading ${uploadedUrls.size}/${imageUris.size} images")
        } catch (e: Exception) {
            Log.e(TAG, "Error uploading multiple images: ${e.message}", e)
        } finally {
            _uploadProgress.value = 0
        }

        return uploadedUrls
    }

    override suspend fun deleteImage(imageUrl: String): Boolean {
        return try {
            // Extract the storage path from the URL
            // The URL format is typically: https://firebasestorage.googleapis.com/v0/b/[BUCKET]/o/[PATH]?alt=media&token=[TOKEN]
            val encodedPath = imageUrl.substringAfter("/o/").substringBefore("?")
            val decodedPath = java.net.URLDecoder.decode(encodedPath, "UTF-8")

            val imageRef = storage.reference.child(decodedPath)
            imageRef.delete().await()

            Log.d(TAG, "Image deleted successfully: $decodedPath")
            true
        } catch (e: Exception) {
            Log.e(TAG, "Error deleting image: ${e.message}")
            false
        }
    }

    private suspend fun uploadFileWithProgress(uri: Uri, fileRef: StorageReference): String? {
        return try {
            val uploadTask = fileRef.putFile(uri)

            // Monitor the upload progress
            uploadTask.addOnProgressListener { taskSnapshot ->
                val progress = (100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount).toInt()
                _uploadProgress.value = progress
                Log.d(TAG, "Upload progress: $progress%")
            }

            // Await completion of the upload
            uploadTask.await()

            // Get the download URL
            val downloadUrl = fileRef.downloadUrl.await().toString()
            Log.d(TAG, "Upload successful, URL: $downloadUrl")
            downloadUrl
        } catch (e: Exception) {
            Log.e(TAG, "Upload failed: ${e.message}")
            null
        }
    }

    companion object {
        private const val TAG = "StorageRepository"
    }
}