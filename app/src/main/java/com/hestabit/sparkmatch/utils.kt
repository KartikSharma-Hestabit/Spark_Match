package com.hestabit.sparkmatch

import android.content.Context
import com.hestabit.sparkmatch.ui.Router.Routes
import javax.inject.Inject

object utils {

    fun getString(id : Int, value : Int = -1): String{
        if(value != -1)
            return Routes.getCurrentContext().getString(id, value)
        return Routes.getCurrentContext().getString(id)
    }

}