package com.hestabit.sparkmatch.screens.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.hestabit.sparkmatch.R
import com.hestabit.sparkmatch.ui.theme.Gray
import com.hestabit.sparkmatch.ui.theme.HotPink
import com.hestabit.sparkmatch.ui.theme.White
import com.hestabit.sparkmatch.ui.theme.modernist

@Composable
fun ProfileScreen() {

    val pagerState = rememberPagerState(initialPage = 0, pageCount = { 3 })

    Scaffold(
        topBar = {
            Row (
                modifier = Modifier.fillMaxWidth().padding(60.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ){
                Icon(
                    painter = painterResource(R.drawable.spark_match_logo),
                    tint = HotPink,
                    contentDescription = "Profile Screen Logo",
                    modifier = Modifier.size(32.dp)
                )

                Spacer(modifier = Modifier.width(10.dp))

                Text(
                    text = "Spark Match",
                    fontFamily = modernist,
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp,
                    color = HotPink,
                    modifier = Modifier
                )
            }
        },
        containerColor = White
    ) { paddingValues ->

        Column (
            modifier = Modifier.padding(paddingValues)
        ){
            Column (
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                White,
                                HotPink
                            )
                        ),
                        shape = RoundedCornerShape(
                            bottomStart = 70.dp,
                            bottomEnd = 70.dp
                        )
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ){
                Column {
                    Image(
                        painter = painterResource(R.drawable.jessica_main),
                        contentDescription = "Profile Picture",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(150.dp)
                            .clip(CircleShape)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row {
                    Text(
                        text = "Jessica, 23",
                        fontFamily = modernist,
                        fontWeight = FontWeight.Bold,
                        fontSize = 28.sp,
                        color = White,
                        modifier = Modifier
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Box(
                    modifier = Modifier
                        .height(159.dp)
                        .width(396.dp)
                        .padding(horizontal = 20.dp)
                ){
                    Column(
                        modifier = Modifier.align(Alignment.TopStart),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        FloatingActionButton(
                            onClick = {  },
                            shape = CircleShape,
                            containerColor = White,
                            elevation = FloatingActionButtonDefaults.elevation(
                                defaultElevation = 10.dp,
                                pressedElevation = 5.dp
                            ),
                            modifier = Modifier.size(54.dp)
                        )  {
                            Icon(
                                painter = painterResource(R.drawable.setting),
                                contentDescription = "Edit Profile Icon",
                                tint = HotPink,
                                modifier = Modifier.size(22.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "Settings",
                            fontFamily = modernist,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = White,
                        )
                    }

                    Column(
                        modifier = Modifier.align(Alignment.BottomCenter),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        FloatingActionButton(
                            onClick = {  },
                            shape = CircleShape,
                            containerColor = White,
                            elevation = FloatingActionButtonDefaults.elevation(
                                defaultElevation = 10.dp,
                                pressedElevation = 5.dp
                            ),
                            modifier = Modifier.size(80.dp)
                        )  {
                            Icon(
                                painter = painterResource(R.drawable.edit),
                                contentDescription = "Edit Profile Icon",
                                tint = HotPink,
                                modifier = Modifier.size(32.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "Edit Profile",
                            fontFamily = modernist,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = White,
                        )
                    }

                    Column(
                        modifier = Modifier.align(Alignment.TopEnd),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        FloatingActionButton(
                            onClick = {  },
                            shape = CircleShape,
                            containerColor = White,
                            elevation = FloatingActionButtonDefaults.elevation(
                                defaultElevation = 10.dp,
                                pressedElevation = 5.dp
                            ),
                            modifier = Modifier.size(54.dp)
                        )  {
                            Icon(
                                painter = painterResource(R.drawable.security),
                                contentDescription = "Edit Profile Icon",
                                tint = HotPink,
                                modifier = Modifier.size(22.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "Safety",
                            fontFamily = modernist,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = White,
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))
            }

            Spacer(modifier = Modifier.height(10.dp))

            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { index ->
                when (index) {
                    0 -> SparkPlatinumFeatures()
                    1 -> SparkPlatinumFeatures()
                    2 -> SparkPlatinumFeatures()
                }
            }
        }
    }
}

@Composable
fun SparkPlatinumFeatures() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ){
            Icon(
                painter = painterResource(R.drawable.spark_match_logo),
                tint = HotPink,
                contentDescription = "Profile Screen Logo",
                modifier = Modifier.size(32.dp)
            )

            Spacer(modifier = Modifier.width(10.dp))

            Text(
                text = "Spark Pride",
                fontFamily = modernist,
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp,
                color = HotPink,
                modifier = Modifier
            )
        }

        Spacer(modifier = Modifier.height(5.dp))

        Text(
            text = "Boast your Spark with pride.",
            fontFamily = modernist,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = Gray,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {},
            shape = RoundedCornerShape(50.dp),
            modifier = Modifier.wrapContentSize(),
            colors = ButtonDefaults.buttonColors(HotPink)
        ) {
            Text(
                text = "Learn More",
                fontFamily = modernist,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = White,
                textAlign = TextAlign.Center
            )
        }
    }
}
