package com.hestabit.sparkmatch.common

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hestabit.sparkmatch.data.PassionList
import com.hestabit.sparkmatch.ui.theme.HotPink
import com.hestabit.sparkmatch.ui.theme.HotPinkDisabled
import com.hestabit.sparkmatch.ui.theme.OffWhite
import com.hestabit.sparkmatch.ui.theme.White
import com.hestabit.sparkmatch.ui.theme.modernist

@Composable
fun  PassionSelectionButton(
    modifier: Modifier = Modifier,
    passionList: PassionList,
    selectionCount: Int,
    isEnabled: Boolean = true,
    isClickEnabled: Boolean = true,
    onClick: (Int) -> Unit = {}
) {

    Box(
        modifier = modifier
            .shadow(
                elevation = if (passionList.isSelected) 8.dp else 0.dp,
                shape = RoundedCornerShape(15.dp),
                spotColor = HotPink // Set the shadow color
            )
            .border(
                width = 1.dp,
                color = if (passionList.isSelected) Color.Transparent else OffWhite,
                shape = RoundedCornerShape(15.dp)
            )
            .clip(RoundedCornerShape(15.dp))
            .background(if (isEnabled && passionList.isSelected) HotPink else if(!isEnabled && passionList.isSelected) HotPinkDisabled else White)
            .clickable {
                if(isClickEnabled) {
                    if (selectionCount <= 4 && !passionList.isSelected) {
                        passionList.isSelected = true
                        onClick(selectionCount + 1)
                    } else if (passionList.isSelected) {
                        passionList.isSelected = false
                        onClick(selectionCount - 1)
                    }
                }
            }
            .padding(horizontal = 12.dp, vertical = 14.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                painter = painterResource(id = passionList.iconRes),
                contentDescription = passionList.name,
                tint = if (passionList.isSelected) White else HotPink,
                modifier = Modifier.size(24.dp)
            )
            Text(
                text = passionList.name,
                fontSize = 14.sp,
                fontFamily = modernist,
                fontWeight = FontWeight.Normal,
                color = if (passionList.isSelected) Color.White else Color.Black
            )
        }
    }
}