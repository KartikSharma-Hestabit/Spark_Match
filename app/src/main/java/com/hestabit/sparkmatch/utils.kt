package com.hestabit.sparkmatch

import com.hestabit.sparkmatch.router.Routes

object utils {

    fun getString(id : Int, value : Int = -1): String{
        if(value != -1)
            return Routes.getCurrentContext().getString(id, value)
        return Routes.getCurrentContext().getString(id)
    }

}