package com.hestabit.sparkmatch.data

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.hestabit.sparkmatch.repository.AuthRepository
import com.hestabit.sparkmatch.repository.DiscoverRepository
import com.hestabit.sparkmatch.repository.UserRepository
import com.hestabit.sparkmatch.viewmodel.AuthViewModel
import com.hestabit.sparkmatch.viewmodel.DiscoverViewModel
import com.hestabit.sparkmatch.viewmodel.OnboardingViewModel
import com.hestabit.sparkmatch.viewmodel.ProfileDetailsViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ViewModelModule {

//    @Provides
//    @Singleton
//    fun provideAuthVM(authRepository: AuthRepository, firebaseAuth: FirebaseAuth): AuthViewModel {
//        return AuthViewModel(authRepository, firebaseAuth)
//    }
//
//    @Provides
//    @Singleton
//    fun provideDiscoverVM(discoverRepository: DiscoverRepository): DiscoverViewModel {
//        return DiscoverViewModel(discoverRepository)
//    }
//
//    @Provides
//    @Singleton
//    fun provideOnboardingVM(): OnboardingViewModel {
//        return OnboardingViewModel()
//    }
//
//    @RequiresApi(Build.VERSION_CODES.O)
//    @Provides
//    @Singleton
//    fun provideProfileDetailVM(userRepository: UserRepository): ProfileDetailsViewModel {
//        return ProfileDetailsViewModel(userRepository)
//    }

}