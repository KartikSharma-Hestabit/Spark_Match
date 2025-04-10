package com.hestabit.sparkmatch.viewmodel

import androidx.lifecycle.ViewModel
import com.hestabit.sparkmatch.repository.UserRepository
import com.hestabit.sparkmatch.repository.UserRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(val userRepository: UserRepository) : ViewModel() {



}