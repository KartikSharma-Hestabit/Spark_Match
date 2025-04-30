package com.hestabit.sparkmatch.repository

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
    fun provideUserRepo(impl: UserRepositoryImpl): UserRepository {
        return impl
    }

    @Provides
    @Singleton
    fun provideDiscoverRepo(impl: DiscoverRepositoryImpl): DiscoverRepository {
        return impl
    }

    @Provides
    @Singleton
    fun provideAuthRepo(impl: AuthRepositoryImpl): AuthRepository {
        return impl
    }

    @Provides
    @Singleton
    fun provideStorageRepository(impl: StorageRepositoryImpl): StorageRepository {
        return impl
    }

    @Provides
    @Singleton
    fun provideChatRepository(impl: ChatRepositoryImpl): ChatRepository {
        return impl
    }

}