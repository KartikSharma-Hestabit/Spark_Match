package com.hestabit.sparkmatch

import android.util.Log
import com.hestabit.sparkmatch.routing.Routes

object utils {

    fun getString(id : Int, value : Int = -1): String{
        if(value != -1)
            return Routes.getCurrentContext().getString(id, value)
        return Routes.getCurrentContext().getString(id)
    }

    fun printDebug(msg: String){
        Log.d("DEBUG", "printDebug: $msg")
    }

}