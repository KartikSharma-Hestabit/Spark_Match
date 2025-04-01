package com.hestabit.sparkmatch.screens.chat

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hestabit.sparkmatch.R
import com.hestabit.sparkmatch.common.MessageBox
import com.hestabit.sparkmatch.data.ChatMessage
import com.hestabit.sparkmatch.ui.theme.Gray
import com.hestabit.sparkmatch.ui.theme.HotPink
import com.hestabit.sparkmatch.ui.theme.OffWhite
import com.hestabit.sparkmatch.ui.theme.White
import com.hestabit.sparkmatch.ui.theme.modernist

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessageCell(chatMessage: ChatMessage, modifier: Modifier = Modifier) {
    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var text by remember { mutableStateOf("") }

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            sheetState = sheetState,
            containerColor = White,
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            scrimColor = Color.Black.copy(alpha = 0.6f)
        ) {
            Scaffold(
                modifier = Modifier.padding(40.dp),
                containerColor = White,
                topBar = {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.size(64.dp)
                        ) {
                            Canvas(modifier = Modifier.matchParentSize()) {
                                val gradient = Brush.linearGradient(
                                    colors = listOf(Color(0xFFFF8A00), Color(0xFFD500F9))
                                )
                                drawCircle(brush = gradient, radius = size.minDimension / 2)
                            }
                            Image(
                                painter = painterResource(R.drawable.jessica_main),
                                contentDescription = "Profile Picture",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(58.dp)
                                    .clip(CircleShape)
                            )
                        }

                        Column(
                            modifier = Modifier.weight(1f).padding(start = 12.dp),
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "Grace",
                                fontFamily = modernist,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                            Row(
                                modifier = Modifier.wrapContentSize(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .background(HotPink, shape = CircleShape)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Online",
                                    fontFamily = modernist,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Normal,
                                    color = Color.Gray
                                )
                            }
                        }

                        // Three-dot Menu Button
                        OutlinedButton(
                            onClick = {  },
                            shape = RoundedCornerShape(16.dp),
                            border = BorderStroke(1.dp, OffWhite),
                            contentPadding = PaddingValues(16.dp)
                        )  {
                            Icon(
                                painter = painterResource(R.drawable.more),
                                contentDescription = "Facebook Icon",
                                tint = Gray,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }
                },
                bottomBar = {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(top = 2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = text,
                            onValueChange = { text = it },
                            placeholder = { Text("Your message", color = Gray) },
                            shape = RoundedCornerShape(16.dp),
                            singleLine = true,
                            textStyle = TextStyle(fontSize = 14.sp),
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 8.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedBorderColor = OffWhite,
                                focusedBorderColor = OffWhite,
                                cursorColor = HotPink
                            ),
                            trailingIcon = {
                                IconButton(onClick = {  }) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.profile_send),
                                        contentDescription = "Send",
                                        tint = HotPink,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        )

                        OutlinedButton(
                            onClick = {  },
                            shape = RoundedCornerShape(16.dp),
                            border = BorderStroke(1.dp, OffWhite),
                            contentPadding = PaddingValues(16.dp),
                            modifier = Modifier.size(56.dp)
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.voice),
                                contentDescription = "Voice Note",
                                tint = HotPink,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            ) { paddingValues ->
                LazyColumn (
                    Modifier.padding(paddingValues)
                ){
                    items(4) {
                        MessageBox(
                            message = "Hi Jake, how are you? I saw on the app that we’ve crossed paths several times this week \uD83D\uDE04",
                            time = "2:55 PM",
                            isSender = false
                        )

                        MessageBox(
                            message = "Haha truly! Nice to meet you Grace! What about a cup of coffee today evening? ☕\uFE0F ",
                            time = "2:55 PM",
                            isSender = true
                        )
                    }
                }
            }
        }
    }

    Row(
        modifier = modifier.clickable{
            showBottomSheet = true
        },
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {


        if (chatMessage.story){
            Image(
                painter = painterResource(chatMessage.senderImage),
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(65.dp)
                    .border(
                        2.dp,
                        brush = Brush.linearGradient(
                            colors = listOf(Color(0xff8A2387), Color(0xffE94057), Color(0xffF27121))
                        ),
                        shape = CircleShape
                    )
                    .padding(4.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        } else {
            Image(
                painter = painterResource(chatMessage.senderImage),
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(65.dp)
                    .padding(4.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        }

        Column(
            modifier = Modifier
                .padding(vertical = 7.dp)
                .weight(1f),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(chatMessage.senderName, fontFamily = modernist, fontWeight = FontWeight.W700, fontSize = 14.sp)
            Text(chatMessage.lastMessage, fontFamily = modernist, fontWeight = FontWeight.W400, fontSize = 14.sp)
        }

        Column(
            modifier = Modifier.padding(vertical = 7.dp),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.End
        ) {
            Text(chatMessage.timestamp, fontFamily = modernist, fontWeight = FontWeight.W700, fontSize = 12.sp, color = Gray)

            if (chatMessage.unreadCount > 0) {
                Box(
                    modifier = Modifier
                        .size(25.dp)
                        .clip(CircleShape)
                        .background(HotPink),
                    contentAlignment = Alignment.Center
                ) {
                    Text(chatMessage.unreadCount.toString(), fontFamily = modernist, fontWeight = FontWeight.W700, fontSize = 12.sp, color = White)
                }
            }
        }
    }
}