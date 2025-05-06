package com.hestabit.sparkmatch.utils

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import coil.ImageLoader
import coil.request.CachePolicy
import com.hestabit.sparkmatch.data.PassionList
import com.hestabit.sparkmatch.data.UserProfile
import com.hestabit.sparkmatch.router.AuthRoute
import com.hestabit.sparkmatch.router.Routes
import java.text.SimpleDateFormat
import java.time.Year
import java.util.Calendar
import java.util.Date
import java.util.Locale

object Utils {

    var isNewUser: Boolean = false

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
            .diskCachePolicy(CachePolicy.ENABLED)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .build()
    }

    enum class ButtonClicked {
        LEFT,
        MIDDLE,
        RIGHT
    }

    val hobbyOptions: MutableList<PassionList> = AuthRoute.PassionType.toPassionLists().toMutableList()

    @RequiresApi(Build.VERSION_CODES.O)
    fun getAgeFromBirthday(birthday:String): String{
        return if(birthday.isNotEmpty()) {
            val birthYear = birthday.split("-")[0].toInt()
            val currentYear = Year.now().value
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

    fun convertTimestampSmart(timestamp: Long): String {
        val now = Calendar.getInstance()
        val inputTime = Calendar.getInstance().apply { timeInMillis = timestamp }

        val isSameDay = now.get(Calendar.YEAR) == inputTime.get(Calendar.YEAR) &&
                now.get(Calendar.DAY_OF_YEAR) == inputTime.get(Calendar.DAY_OF_YEAR)

        return if (isSameDay) {
            SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date(timestamp))
        } else {
            SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date(timestamp))
        }
    }

}