package com.hestabit.sparkmatch.screens.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.hestabit.sparkmatch.R
import com.hestabit.sparkmatch.common.BackButton
import com.hestabit.sparkmatch.common.InterestChip
import com.hestabit.sparkmatch.ui.theme.HotPink
import com.hestabit.sparkmatch.ui.theme.OffWhite
import com.hestabit.sparkmatch.ui.theme.White
import com.hestabit.sparkmatch.ui.theme.modernist

@Composable
fun Profile(navController: NavController) {
    val interests = listOf("Travelling", "Books", "Music", "Dancing", "Modeling")
    val selectedInterests = remember { mutableStateListOf("Travelling", "Books") }

    Scaffold(
        floatingActionButton = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(40.dp)
            ) {
                BackButton(navController, OffWhite)
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(paddingValues)
        ) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(415.dp)
                        .zIndex(0f)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.jessica_main),
                        contentDescription = "Profile Image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
            item {
                Row(
                    modifier = Modifier.fillMaxWidth().offset(y=(-60).dp).zIndex(2f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    FloatingActionButton(
                        onClick = { /* Dislike action */ },
                        modifier = Modifier.size(72.dp),
                        shape = CircleShape,
                        containerColor = Color.White,
                        elevation = FloatingActionButtonDefaults.elevation(4.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.profile_close),
                            contentDescription = "Dislike",
                            tint = Color(0xFFF27121),
                            modifier = Modifier.size(32.dp)
                        )
                    }
                    FloatingActionButton(
                        onClick = { /* Like action */ },
                        modifier = Modifier.size(96.dp),
                        shape = CircleShape,
                        containerColor = HotPink,
                        elevation = FloatingActionButtonDefaults.elevation(6.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.profile_like),
                            contentDescription = "Like",
                            tint = Color.White,
                            modifier = Modifier.size(48.dp)
                        )
                    }
                    FloatingActionButton(
                        onClick = { /* Super like action */ },
                        modifier = Modifier.size(72.dp),
                        shape = CircleShape,
                        containerColor = Color.White,
                        elevation = FloatingActionButtonDefaults.elevation(4.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.profile_superlike),
                            contentDescription = "Super Like",
                            tint = Color(0xFF9575CD),
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
            }
            item {
                Card (
                    colors = CardDefaults.cardColors(White),
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(4.dp),
                    modifier = Modifier.fillMaxWidth().offset(y=(-110).dp)
                ){
                    Column(modifier = Modifier.padding(start = 40.dp, top = 80.dp, end = 40.dp, bottom = 40.dp)) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = "Jessica Parker, 23",
                                        fontFamily = modernist,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp
                                    )
                                    Text(
                                        text = "Professional model",
                                        fontFamily = modernist,
                                        fontWeight = FontWeight.Normal,
                                        fontSize = 14.sp
                                    )
                                }
                                OutlinedButton(
                                    onClick = { },
                                    shape = RoundedCornerShape(15.dp),
                                    border = BorderStroke(1.dp, OffWhite),
                                    contentPadding = PaddingValues(16.dp),
                                ) {
                                    Icon(
                                        painter = painterResource(R.drawable.profile_send),
                                        contentDescription = "Send message Icon",
                                        tint = HotPink,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.padding(15.dp))
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = "Location",
                                        fontFamily = modernist,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp
                                    )
                                    Text(
                                        text = "Chicago, IL United States",
                                        fontFamily = modernist,
                                        fontWeight = FontWeight.Normal,
                                        fontSize = 14.sp
                                    )
                                }
                                Button(
                                    onClick = {},
                                    shape = RoundedCornerShape(7.dp),
                                    colors = ButtonDefaults.buttonColors(Color(0x1AE94057)),
                                    contentPadding = PaddingValues(10.dp),
                                    modifier = Modifier.clickable(false, onClickLabel = null, role = null, onClick = {})
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(2.dp)
                                    ) {
                                        Icon(
                                            painter = painterResource(R.drawable.profile_location),
                                            contentDescription = "Location Icon",
                                            tint = HotPink,
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Text(
                                            text = "1 km",
                                            fontFamily = modernist,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp,
                                            color = HotPink
                                        )
                                    }
                                }
                            }
                        }
                        Spacer(modifier = Modifier.padding(15.dp))
                        Column {
                            Text(
                                text = "About",
                                fontFamily = modernist,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Text(
                                text = "My name is Jessica Parker and I enjoy meeting new people and finding ways to help them have an uplifting experience. I enjoy reading..",
                                fontFamily = modernist,
                                fontWeight = FontWeight.Normal,
                                fontSize = 14.sp
                            )
                            Text(
                                text = "Read more",
                                fontFamily = modernist,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = HotPink,
                                modifier = Modifier.clickable { }
                            )
                        }
                        Spacer(modifier = Modifier.padding(15.dp))
                        Column {
                            Text(
                                text = "Interests",
                                fontFamily = modernist,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Spacer(modifier = Modifier.padding(5.dp))
                            FlowRow(
                                verticalArrangement = Arrangement.spacedBy(10.dp),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                interests.forEach { interest ->
                                    InterestChip(
                                        text = interest,
                                        isSelected = selectedInterests.contains(interest)
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.padding(15.dp))
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Gallery",
                                    fontFamily = modernist,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )
                                Text(
                                    text = "See all",
                                    fontFamily = modernist,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = HotPink,
                                    modifier = Modifier.clickable {
                                        navController.navigate("gallery")
                                    }
                                )
                            }
                            Spacer(modifier = Modifier.padding(5.dp))
                            LazyVerticalStaggeredGrid(
                                columns = StaggeredGridCells.Fixed(3),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(316.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalItemSpacing = 8.dp,
                                userScrollEnabled = false
                            ) {
                                items(listOf(R.drawable.jessica_1, R.drawable.jessica_2, R.drawable.jessica_3, R.drawable.jessica_4, R.drawable.jessica_5)) { image ->
                                    Card(
                                        shape = RoundedCornerShape(10.dp),
                                        elevation = CardDefaults.cardElevation(4.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Image(
                                            painter = painterResource(id = image),
                                            contentDescription = "Gallery Image",
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier.height(150.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
            item {
                Row(
                    modifier = Modifier.fillMaxWidth().offset(y=(-60).dp).zIndex(2f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    FloatingActionButton(
                        onClick = { /* Dislike action */ },
                        modifier = Modifier.size(72.dp),
                        shape = CircleShape,
                        containerColor = Color.White,
                        elevation = FloatingActionButtonDefaults.elevation(4.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.profile_close),
                            contentDescription = "Dislike",
                            tint = Color(0xFFF27121),
                            modifier = Modifier.size(32.dp)
                        )
                    }
                    FloatingActionButton(
                        onClick = { /* Like action */ },
                        modifier = Modifier.size(96.dp),
                        shape = CircleShape,
                        containerColor = HotPink,
                        elevation = FloatingActionButtonDefaults.elevation(6.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.profile_like),
                            contentDescription = "Like",
                            tint = Color.White,
                            modifier = Modifier.size(48.dp)
                        )
                    }
                    FloatingActionButton(
                        onClick = { /* Super like action */ },
                        modifier = Modifier.size(72.dp),
                        shape = CircleShape,
                        containerColor = Color.White,
                        elevation = FloatingActionButtonDefaults.elevation(4.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.profile_superlike),
                            contentDescription = "Super Like",
                            tint = Color(0xFF9575CD),
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
            }
        }
    }
}