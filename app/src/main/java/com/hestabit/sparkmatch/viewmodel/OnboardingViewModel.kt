package com.hestabit.sparkmatch.viewmodel

import androidx.lifecycle.ViewModel
import com.hestabit.sparkmatch.R
import com.hestabit.sparkmatch.data.OnboardingData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor() : ViewModel() {

    fun onboardingData() = listOf(
        OnboardingData(
            "Algorithm",
            "Users going through a vetting process to ensure you never match with bots.",
            R.drawable.img_1
        ),

        OnboardingData(
            "Matches",
            "We match you with people that have a large array of similar interests.",
            R.drawable.img_2

        ),
        OnboardingData(
            "Premium",
            "Sign up today and enjoy the first month of premium benefits on us.",
            R.drawable.img_3
        )
    )

}