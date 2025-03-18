package com.hestabit.sparkmatch

import android.content.Context
import android.util.Log
import com.hestabit.sparkmatch.Router.Routes
import javax.inject.Inject

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