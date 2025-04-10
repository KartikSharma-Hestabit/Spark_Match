package com.hestabit.sparkmatch.data

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.hestabit.sparkmatch.repository.UserRepository
import com.hestabit.sparkmatch.repository.UserRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    @Singleton
    fun provideUserRepo(impl: UserRepositoryImpl): UserRepository{
        return impl
    }

}