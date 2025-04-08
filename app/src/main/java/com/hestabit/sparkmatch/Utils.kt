package com.hestabit.sparkmatch

import android.content.Context
import android.util.Log
import androidx.compose.runtime.remember
import coil.ImageLoader
import coil.request.CachePolicy
import com.hestabit.sparkmatch.data.PassionList
import com.hestabit.sparkmatch.router.AuthRoute
import com.hestabit.sparkmatch.router.Routes

object Utils {

    fun getString(id: Int, value: Int = -1): String {
        if (value != -1)
            return Routes.getCurrentContext().getString(id, value)
        return Routes.getCurrentContext().getString(id)
    }

    fun printDebug(msg: String) {
        Log.d("DEBUG", "printDebug: $msg")
    }

    fun createImageLoader(context: Context): ImageLoader {
        return ImageLoader.Builder(context)
            .diskCachePolicy(CachePolicy.DISABLED) // Disable disk cache for resources
            .memoryCachePolicy(CachePolicy.ENABLED) // Enable memory cache
            .build()
    }

    enum class buttonClicked {
        LEFT,
        MIDDLE,
        RIGHT
    }

    val hobbyOptions =
        mutableListOf(
            PassionList("Photography", R.drawable.photography),
            PassionList("Shopping", R.drawable.weixin_market),
            PassionList("Karaoke", R.drawable.voice),
            PassionList("Yoga", R.drawable.viencharts),
            PassionList("Cooking", R.drawable.noodles),
            PassionList("Tennis", R.drawable.tennis),
            PassionList("Running", R.drawable.sport),
            PassionList("Swimming", R.drawable.ripple),
            PassionList("Art", R.drawable.platte),
            PassionList("Traveling", R.drawable.outdoor),
            PassionList("Extreme", R.drawable.parachute),
            PassionList("Music", R.drawable.music),
            PassionList("Drink", R.drawable.goblet_full),
            PassionList("Video games", R.drawable.game_handle)
        )

}