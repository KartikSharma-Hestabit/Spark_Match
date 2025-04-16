package com.hestabit.sparkmatch.screens.discover

import androidx.compose.animation.core.EaseInOutQuad
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.unit.DpSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.rememberRangeSliderState
import androidx.compose.material3.rememberSliderState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hestabit.sparkmatch.utils.Utils.ButtonClicked
import com.hestabit.sparkmatch.common.DefaultButton
import com.hestabit.sparkmatch.ui.theme.Black
import com.hestabit.sparkmatch.ui.theme.HotPink
import com.hestabit.sparkmatch.ui.theme.OffWhite
import com.hestabit.sparkmatch.ui.theme.White
import com.hestabit.sparkmatch.ui.theme.modernist
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterScreen(onDismiss: () -> Unit) {

    var clickedButton by remember { mutableStateOf(ButtonClicked.LEFT) }

    val leftButtonWeight by animateFloatAsState(
        targetValue = if (clickedButton == ButtonClicked.LEFT) 0.001f else if (clickedButton == ButtonClicked.MIDDLE) 1 / 3f else 2 / 3f,
        animationSpec = tween(durationMillis = 200, easing = EaseInOutQuad)
    )
    val rightButtonWeight by animateFloatAsState(
        targetValue = if (clickedButton == ButtonClicked.LEFT) 2 / 3f else if (clickedButton == ButtonClicked.MIDDLE) 1 / 3f else 0.001f,
        animationSpec = tween(durationMillis = 200, easing = EaseInOutQuad)
    )

    val coroutineScope = rememberCoroutineScope()

    val customTextSelectionColors = TextSelectionColors(
        handleColor = HotPink,  // Drop indicator (caret handle) color
        backgroundColor = HotPink.copy(alpha = 0.4f) // Selection highlight color
    )

    var value by remember { mutableFloatStateOf(0.4f) }

    var startRangeValue by remember { mutableFloatStateOf(0.1f) }
    var endRangeValue by remember { mutableFloatStateOf(0.5f) }


    val sliderState = rememberSliderState(value, valueRange = 0.0f..0.8f)
    val rangeSliderState = rememberRangeSliderState(
        activeRangeStart = startRangeValue,
        activeRangeEnd = endRangeValue,
        valueRange = 0.1f..0.5f
    )

    LaunchedEffect(sliderState.value) {
        // Optional: react to value change
        value = sliderState.value
    }

    LaunchedEffect(rangeSliderState.activeRangeStart, rangeSliderState.activeRangeEnd) {
        // Optional: react to value change
        startRangeValue = rangeSliderState.activeRangeStart
        endRangeValue = rangeSliderState.activeRangeEnd
    }

    Column(
        modifier = Modifier
            .padding(horizontal = 40.dp)
            .padding(top = 25.dp),
        verticalArrangement = Arrangement.spacedBy(30.dp)
    ) {

        Box(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                "Filters",
                fontSize = 24.sp,
                fontFamily = modernist,
                fontWeight = FontWeight.W700,
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.TopCenter)
            )

            Text(
                "Clear",
                fontSize = 16.sp,
                fontFamily = modernist,
                fontWeight = FontWeight.W700,
                textAlign = TextAlign.End,
                color = HotPink,
                modifier = Modifier
                    .clip(RoundedCornerShape(15.dp))
                    .align(Alignment.TopEnd)
                    .clickable() {
                        onDismiss()
                    }
                    .padding(vertical = 5.dp, horizontal = 10.dp),
            )
        }

        Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
            Text(
                "Interested in",
                fontFamily = modernist,
                fontWeight = FontWeight.W700,
                fontSize = 16.sp,
                modifier = Modifier
            )

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(15.dp))
                    .border(
                        width = 1.dp,
                        shape = RoundedCornerShape(15.dp),
                        color = Color.LightGray
                    )
            ) {

                Row(modifier = Modifier.height(64.dp)) {

                    Box(modifier = Modifier.weight(leftButtonWeight))

                    Box(
                        modifier = Modifier
                            .weight(1 / 3f)
                            .fillMaxHeight()
                            .background(HotPink)
                    )

                    Box(modifier = Modifier.weight(rightButtonWeight))

                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        "Women",
                        modifier = Modifier
                            .clip(
                                RoundedCornerShape(
                                    topStart = 15.dp,
                                    bottomStart = 15.dp
                                )
                            )
                            .clickable() {
                                coroutineScope.launch {
                                    clickedButton = ButtonClicked.LEFT
                                }
                            }
                            .weight(1f)
                            .padding(20.dp),
                        textAlign = TextAlign.Center,
                        fontFamily = modernist,
                        fontWeight = FontWeight.W700,
                        fontSize = 14.sp,
                        color = if (clickedButton == ButtonClicked.LEFT) White else Black
                    )

                    VerticalDivider(modifier = Modifier.height(22.dp))

                    Text(
                        "Men",
                        modifier = Modifier
                            .clickable() {
                                coroutineScope.launch {
                                    clickedButton = ButtonClicked.MIDDLE
                                }
                            }
                            .weight(1f)
                            .padding(20.dp),
                        textAlign = TextAlign.Center,
                        fontFamily = modernist,
                        fontWeight = FontWeight.W700,
                        fontSize = 14.sp,
                        color = if (clickedButton == ButtonClicked.MIDDLE) White else Black
                    )

                    VerticalDivider(modifier = Modifier.height(22.dp))

                    Text(
                        "Both",
                        modifier = Modifier
                            .clip(RoundedCornerShape(topEnd = 15.dp, bottomEnd = 15.dp))
                            .clickable() {
                                coroutineScope.launch {
                                    clickedButton = ButtonClicked.RIGHT
                                }
                            }
                            .weight(1f)
                            .padding(20.dp),
                        textAlign = TextAlign.Center,
                        fontFamily = modernist,
                        fontWeight = FontWeight.W700,
                        fontSize = 14.sp,
                        color = if (clickedButton == ButtonClicked.RIGHT) White else Black
                    )
                }

            }
        }

        CompositionLocalProvider(LocalTextSelectionColors provides customTextSelectionColors) {
            OutlinedTextField(
                value = "Noida, In",
                textStyle = TextStyle(
                    fontFamily = modernist,
                    fontWeight = FontWeight.W400,
                    fontSize = 14.sp
                ),
                onValueChange = {},
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color.LightGray,
                    focusedBorderColor = HotPink.copy(0.5f),
                    focusedLabelColor = HotPink.copy(0.75f),
                    cursorColor = HotPink
                ),
                shape = RoundedCornerShape(15.dp),
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Location", fontFamily = modernist) }
            )
        }

        Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Distance", fontSize = 16.sp, fontWeight = FontWeight.W700)

                Text(
                    "${(value * 100).times(10).roundToInt() / 10.0}km",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.W400
                )
            }

            Slider(
                state = sliderState,
                modifier = Modifier.fillMaxWidth(),
                track = {
                    SliderDefaults.Track(
                        sliderState,
                        thumbTrackGapSize = 0.dp,
                        drawStopIndicator = {},
                        colors = SliderDefaults.colors(
                            activeTrackColor = HotPink,
                            inactiveTrackColor = OffWhite,
                        ),
                        modifier = Modifier
                            .height(6.dp)
                            .fillMaxWidth()
                    )
                },
                thumb = {
                    SliderDefaults.Thumb(
                        interactionSource = remember { MutableInteractionSource() },
                        thumbSize = DpSize(
                            width = 30.dp,
                            30.dp
                        ),
                        colors = SliderDefaults.colors(thumbColor = HotPink),
                        modifier = Modifier
                            .border(4.dp, color = White, shape = CircleShape)
                            .shadow(
                                2.dp,
                                shape = CircleShape,
                                spotColor = HotPink,
                                ambientColor = HotPink
                            )
                    )
                }
            )

        }

        Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Age", fontSize = 16.sp, fontWeight = FontWeight.W700)

                Text(
                    "${(startRangeValue * 100).toInt()} - ${(endRangeValue * 100).toInt()}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.W400
                )
            }

            RangeSlider(
                state = rangeSliderState,
                track = {
                    SliderDefaults.Track(
                        rangeSliderState,
                        thumbTrackGapSize = 0.dp,
                        drawStopIndicator = { },
                        colors = SliderDefaults.colors(
                            activeTrackColor = HotPink,
                            inactiveTrackColor = OffWhite,
                        ),
                        modifier = Modifier
                            .height(6.dp)
                            .fillMaxWidth()
                    )
                },
                startThumb = {
                    SliderDefaults.Thumb(
                        interactionSource = remember { MutableInteractionSource() },
                        thumbSize = DpSize(
                            width = 30.dp,
                            30.dp
                        ),
                        colors = SliderDefaults.colors(thumbColor = HotPink),
                        modifier = Modifier
                            .border(4.dp, color = White, shape = CircleShape)
                            .shadow(
                                2.dp,
                                shape = CircleShape,
                                spotColor = HotPink,
                                ambientColor = HotPink
                            )
                    )
                },
                endThumb = {
                    SliderDefaults.Thumb(
                        interactionSource = remember { MutableInteractionSource() },
                        thumbSize = DpSize(
                            width = 30.dp,
                            30.dp
                        ),
                        colors = SliderDefaults.colors(thumbColor = HotPink),
                        modifier = Modifier
                            .border(4.dp, color = White, shape = CircleShape)
                            .shadow(
                                2.dp,
                                shape = CircleShape,
                                spotColor = HotPink,
                                ambientColor = HotPink
                            )
                    )
                }
            )

            DefaultButton(text = "Continue"){
                onDismiss()
            }

        }


    }

}