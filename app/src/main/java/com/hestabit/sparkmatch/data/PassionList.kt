package com.hestabit.sparkmatch.data

import com.hestabit.sparkmatch.R

data class Hobby(val name: String, val iconRes: Int)

val options = listOf(
    Hobby("Photography", R.drawable.photography),
    Hobby("Shopping", R.drawable.weixin_market),
    Hobby("Karaoke", R.drawable.voice),
    Hobby("Yoga", R.drawable.viencharts),
    Hobby("Cooking", R.drawable.noodles),
    Hobby("Tennis", R.drawable.tennis),
    Hobby("Run", R.drawable.sport),
    Hobby("Swimming", R.drawable.ripple),
    Hobby("Art", R.drawable.platte),
    Hobby("Traveling", R.drawable.outdoor),
    Hobby("Extreme", R.drawable.parachute),
    Hobby("Music", R.drawable.music),
    Hobby("Drink", R.drawable.goblet_full),
    Hobby("Video games", R.drawable.game_handle)
)
