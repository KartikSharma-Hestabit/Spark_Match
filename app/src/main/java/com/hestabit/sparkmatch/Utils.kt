package com.hestabit.sparkmatch

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import coil.ImageLoader
import coil.request.CachePolicy
import com.hestabit.sparkmatch.data.PassionList
import com.hestabit.sparkmatch.router.AuthRoute
import com.hestabit.sparkmatch.router.Routes
import kotlin.collections.toMutableList

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

//    fun convertMapToJsonString(data: Map<String, Object?>): String {
//        val map = data.mapValues {
//            when (val value = it.value) {
//                is List<*> -> value.mapNotNull { it?.toString() } // for passions
//                else -> value?.toString() // convert other values to string
//            }
//        }
////        return Json.encodeToString(map)
//    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getAgeFromBirthday(birthday:String): String{
        return if(!birthday.isNullOrEmpty()) {
            val birthYear = birthday.split("-")[0].toInt()
            val currentYear = java.time.Year.now().value
            (currentYear - birthYear).toString()
        }else{
            ""
        }
    }

    fun stringListToPassions(passionStrings: List<String>): List<AuthRoute.PassionType> {
        return passionStrings.mapNotNull { passionString ->
            try {
                // Use the helper method from our updated enum
                AuthRoute.PassionType.fromId(passionString)
            } catch (e: Exception) {
                Log.e("UserRepository", "Error converting passion string: $passionString", e)
                null
            }
        }
    }

}