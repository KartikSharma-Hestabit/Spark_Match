package com.hestabit.sparkmatch

import android.content.Context
import android.util.Log
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
            .diskCachePolicy(CachePolicy.DISABLED)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .build()
    }

    enum class ButtonClicked {
        LEFT,
        MIDDLE,
        RIGHT
    }

    val hobbyOptions: MutableList<PassionList> = AuthRoute.PassionType.toPassionLists().toMutableList()

}