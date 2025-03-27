package com.hestabit.sparkmatch.data

import com.hestabit.sparkmatch.R

data class CardData(
    val id: Int,
    val name: String,
    val age: Int,
    val profession: String,
    val imageRes: Int,
    val distance: Int
)

val cardDataList = listOf(
    CardData(1, "Leilani", 19, "", R.drawable.leilani, 12),
    CardData(2, "Annabelle", 20, "", R.drawable.img_4, 8),
    CardData(3, "Reagan", 24, "", R.drawable.reagan, 8),
    CardData(4, "Hadley", 25, "", R.drawable.hadley, 8),
    CardData(5, "Chloe", 20, "", R.drawable.chloe, 8),
    CardData(6, "Kyle", 20, "", R.drawable.kyle, 8),
    CardData(1, "Leilani", 19, "", R.drawable.leilani, 12),
    CardData(2, "Annabelle", 20, "", R.drawable.img_4, 8),
    CardData(3, "Reagan", 24, "", R.drawable.reagan, 8),
    CardData(4, "Hadley", 25, "", R.drawable.hadley, 8),
    CardData(5, "Chloe", 20, "", R.drawable.chloe, 8),
    CardData(6, "Kyle", 20, "", R.drawable.kyle, 8)
)