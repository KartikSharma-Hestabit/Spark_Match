package com.hestabit.sparkmatch.screens.discover

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.EaseInOutQuad
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hestabit.sparkmatch.R
import com.hestabit.sparkmatch.Utils.printDebug
import com.hestabit.sparkmatch.ui.theme.HotPink
import com.hestabit.sparkmatch.ui.theme.modernist

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun CardStack(
    cards: List<CardData>,
    onRemoveCard: (CardData) -> Unit,
    modifier: Modifier = Modifier,
    onNavigate: (String, CardData) -> Unit,
    onReload: () -> Unit
) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {

        val noResultText = buildAnnotatedString {
            pushStyle(
                SpanStyle(
                    fontFamily = modernist,
                    fontWeight = FontWeight.W700,
                    fontSize = 20.sp
                )
            )
            append("Oops!")
            append("\n\n")
            pop()
            pushStyle(
                SpanStyle(
                    fontWeight = FontWeight.W500,
                    fontFamily = modernist,
                    fontSize = 15.sp
                )
            )
            append("We can no longer find people near you,\nplease try again later")
        }


        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            Text(noResultText, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())

            TextButton(
                onClick = onReload,
                colors = ButtonDefaults.textButtonColors(contentColor = HotPink),
                modifier = Modifier.padding(top = 30.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Icon(
                        painter = painterResource(R.drawable.back_reload),
                        contentDescription = "reload",
                        modifier = Modifier.size(30.dp)
                    )

                    Text("Try Again", fontFamily = modernist, fontSize = 19.sp, fontWeight = FontWeight.W500)

                }

            }

        }
        for (i in cards.indices.reversed()) {
            val isTopCard = i == 0
            val isVisible = i <= 2  // Show only top 3 cards

            val animatedOffset by animateDpAsState(
                targetValue = if (isVisible) -(i * 65).dp else 0.dp,
                label = "",
                animationSpec = tween(200, easing = EaseInOutQuad)
            )
            val animatedScale by animateFloatAsState(
                targetValue = if (isTopCard) 1f else ((10 - i) / 10f),
                label = "",
                animationSpec = tween(200, easing = EaseInOutQuad)
            )

            AnimatedVisibility(
                visible = isVisible,
                enter = fadeIn() + slideInVertically { it / 10 }, // Smooth entry from slightly above
            ) {
                DraggableCard(
                    cardData = cards[i],
                    onSwiped = { direction ->
                        onRemoveCard(cards[i])  // Remove the swiped card
                        printDebug("Card swiped ${direction.name}")
                    },
                    modifier = Modifier
                        .padding(top = 40.dp, start = 40.dp, end = 40.dp)
                        .offset(y = animatedOffset)
                        .scale(animatedScale)
                        .graphicsLayer {
                            scaleX = animatedScale
                            scaleY = animatedScale
                        },
                    imageAlpha = if (isVisible && !isTopCard) ((10 - i) - 4) / 10f else 1f,
                    isTopCard = isTopCard,
                    onNavigate = onNavigate
                )
            }
        }
    }
}