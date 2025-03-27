package com.hestabit.sparkmatch.data

import com.hestabit.sparkmatch.R

data class Story(
    val name: String,
    val imageRes: Int,
    val story: Boolean
)

val sampleStories = listOf(
    Story("You", R.drawable.you, true),
    Story("Emma", R.drawable.emma, true),
    Story("Ava", R.drawable.ava, false),
    Story("Sophia", R.drawable.sophia, false),
    Story("Amelia", R.drawable.amelia, true),
    Story("Emma", R.drawable.emma, true),
    Story("Ava", R.drawable.ava, false),
    Story("Sophia", R.drawable.sophia, false),
    Story("Amelia", R.drawable.amelia, true)
)

