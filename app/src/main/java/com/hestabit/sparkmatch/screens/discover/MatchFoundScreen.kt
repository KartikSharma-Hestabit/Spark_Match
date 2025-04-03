package com.hestabit.sparkmatch.screens.discover

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.hestabit.sparkmatch.R
import com.hestabit.sparkmatch.Utils.createImageLoader
import com.hestabit.sparkmatch.common.DefaultButton
import com.hestabit.sparkmatch.router.Routes
import com.hestabit.sparkmatch.ui.theme.HotPink
import com.hestabit.sparkmatch.ui.theme.White
import com.hestabit.sparkmatch.ui.theme.modernist

@Composable
fun MatchFoundScreen(modifier: Modifier = Modifier, onNavigate: (String) -> Unit) {

    val annotatedString = buildAnnotatedString {
        pushStyle(SpanStyle(fontFamily = modernist, fontWeight = FontWeight.W700, fontSize = 34.sp, color = HotPink))
        append("It’s a match, Jake!")
        pop()
        pushStyle(SpanStyle(fontFamily = modernist, fontWeight = FontWeight.W400, fontSize = 14.sp))
        append("\nStart a conversation now with each other")
        pop()
    }

    val context = LocalContext.current
    val imageLoader = createImageLoader(context)

    Column(modifier = modifier,horizontalAlignment = Alignment.CenterHorizontally) {

        Box(modifier = Modifier.weight(2f), contentAlignment = Alignment.Center) {

            Box(modifier = Modifier.rotate(10f).offset(y = (-50).dp, x = 50.dp)) {



                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data("android.resource://${context.packageName}/${R.drawable.img_5}")
                        .crossfade(true)
                        .build(),
                    contentDescription = null,
                    imageLoader = (imageLoader),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .padding(30.dp)
                        .shadow(10.dp, shape = RoundedCornerShape(15.dp))
                        .clip(RoundedCornerShape(15.dp))
                        .size(160.dp, 240.dp)
                )

//                Image(
//                    painter = painterResource(R.drawable.img_5),
//                    contentDescription = "",
//                    modifier = Modifier
//                        .padding(30.dp)
//                        .shadow(10.dp, shape = RoundedCornerShape(15.dp))
//                        .clip(RoundedCornerShape(15.dp))
//                        .size(160.dp, 240.dp)
//
//                )

                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(12.dp)
                        .shadow(10.dp, shape = CircleShape)
                        .clip(CircleShape)
                        .background(White)
                        .size(60.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.profile_like),
                        contentDescription = "",
                        tint = HotPink,
                        modifier = Modifier
                            .size(30.dp)
                            .shadow(
                                elevation = 10.dp,
                                spotColor = HotPink,
                                ambientColor = HotPink,
                                shape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp, bottomEnd = 20.dp)
                            )
                    )
                }
            }

            Box(modifier = Modifier.rotate(-10f).offset(y = 50.dp, x = (-50).dp)) {

                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data("android.resource://${context.packageName}/${R.drawable.img_4}")
                        .crossfade(true)
                        .build(),
                    contentDescription = null,
                    imageLoader = (imageLoader),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .padding(30.dp)
                        .shadow(10.dp, shape = RoundedCornerShape(15.dp))
                        .clip(RoundedCornerShape(15.dp))
                        .size(160.dp, 240.dp)
                )

//                Image(
//                    painter = painterResource(R.drawable.img_4),
//                    contentDescription = "",
//                    modifier = Modifier
//                        .padding(30.dp)
//                        .shadow(10.dp, shape = RoundedCornerShape(15.dp))
//                        .clip(RoundedCornerShape(15.dp))
//                        .size(160.dp, 240.dp)
//
//                )

                Box(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(12.dp)
                        .shadow(10.dp, shape = CircleShape)
                        .clip(CircleShape)
                        .background(White)
                        .size(60.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.profile_like),
                        contentDescription = "",
                        tint = HotPink,
                        modifier = Modifier
                            .size(30.dp)
                            .shadow(
                                elevation = 10.dp,
                                spotColor = HotPink,
                                ambientColor = HotPink,
                                shape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp, bottomEnd = 20.dp)
                            )
                    )
                }
            }

        }

        Column(modifier = Modifier.weight(1f).padding(horizontal = 40.dp).padding(bottom = 50.dp), verticalArrangement = Arrangement.spacedBy(20.dp)) {

            Text(annotatedString, textAlign = TextAlign.Center)

            Spacer(modifier = Modifier.weight(1f))

            DefaultButton(text = "Say Hello") {
                //TODO: Need to call message Screen
            }

            DefaultButton(text = "Keep Swiping", btnColor = HotPink.copy(0.15f), txtColor = HotPink) {
                onNavigate(Routes.POP)
            }

        }

    }

}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun previewMatchScreen() {

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {

        MatchFoundScreen(){}
    }
}