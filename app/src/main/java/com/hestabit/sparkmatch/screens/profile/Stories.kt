package com.hestabit.sparkmatch.screens.profile

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.hestabit.sparkmatch.common.HorizontalPagerLoopingIndicatorSample
import com.hestabit.sparkmatch.common.HorizontalPagerWithGateTransition

@Composable
fun Stories(navController: NavController){

//    HorizontalPagerWithGateTransition()

//    HorizontalPagerLoopingIndicatorSample()

    Gallery(navController)

}