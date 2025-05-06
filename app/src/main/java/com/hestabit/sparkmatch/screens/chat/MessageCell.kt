package com.hestabit.sparkmatch.screens.chat

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.hestabit.sparkmatch.R
import com.hestabit.sparkmatch.common.MessageBox
import com.hestabit.sparkmatch.data.MatchUser
import com.hestabit.sparkmatch.data.Message
import com.hestabit.sparkmatch.ui.theme.Gray
import com.hestabit.sparkmatch.ui.theme.HotPink
import com.hestabit.sparkmatch.ui.theme.OffWhite
import com.hestabit.sparkmatch.ui.theme.White
import com.hestabit.sparkmatch.ui.theme.modernist
import com.hestabit.sparkmatch.utils.Utils.convertTimestampSmart
import com.hestabit.sparkmatch.utils.Utils.createImageLoader
import com.hestabit.sparkmatch.viewmodel.ChatViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessageCell(
    profile: MatchUser,
    message: Message,
    modifier: Modifier = Modifier
) {
    val viewModel: ChatViewModel = hiltViewModel()
    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var text by remember { mutableStateOf("") }
    val context = LocalContext.current
    val imageLoader = createImageLoader(context)
    val messages by viewModel.messages.collectAsState()


    LaunchedEffect(showBottomSheet) {
        if (showBottomSheet) {
            viewModel.loadMessages(profile.chatId)
        }
    }

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            sheetState = sheetState,
            containerColor = White,
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            scrimColor = Color.Black.copy(alpha = 0.6f)
        ) {
            Scaffold(
                modifier = modifier.padding(40.dp),
                containerColor = White,
                topBar = {
                    Row(
                        modifier = modifier.fillMaxWidth().padding(bottom = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = modifier.size(64.dp)
                        ) {
                            AsyncImage(
                                model = ImageRequest.Builder(context)
                                    .data(profile.profileImageUrl)
                                    .crossfade(true)
                                    .build(),
                                contentDescription = null,
                                imageLoader = (imageLoader),
                                contentScale = ContentScale.Crop,
                                modifier = modifier
                                    .size(58.dp)
                                    .clip(CircleShape)
                            )
                        }

                        Column(
                            modifier = modifier.weight(1f).padding(start = 12.dp),
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = profile.firstName,
                                fontFamily = modernist,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
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
                                contentDescription = "",
                                tint = Gray,
                                modifier = modifier.size(28.dp)
                            )
                        }
                    }
                },
                bottomBar = {
                    Row(
                        modifier = modifier.fillMaxWidth().padding(top = 2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = text,
                            onValueChange = { text = it },
                            placeholder = { Text("Your message", color = Gray) },
                            shape = RoundedCornerShape(16.dp),
                            singleLine = true,
                            textStyle = TextStyle(fontSize = 14.sp),
                            modifier = modifier
                                .weight(1f)
                                .padding(end = 8.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedBorderColor = OffWhite,
                                focusedBorderColor = OffWhite,
                                cursorColor = HotPink
                            ),
                            trailingIcon = {
                                IconButton(onClick = {
                                    viewModel.sendMessage(profile.chatId, message.senderId, text)
                                }) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.profile_send),
                                        contentDescription = "Send",
                                        tint = HotPink,
                                        modifier = modifier.size(20.dp)
                                    )
                                }
                            }
                        )

                        OutlinedButton(
                            onClick = {  },
                            shape = RoundedCornerShape(16.dp),
                            border = BorderStroke(1.dp, OffWhite),
                            contentPadding = PaddingValues(16.dp),
                            modifier = modifier.size(56.dp)
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.voice),
                                contentDescription = "Voice Note",
                                tint = HotPink,
                                modifier = modifier.size(20.dp)
                            )
                        }
                    }
                }
            ) { paddingValues ->
                LazyColumn(
                    modifier = Modifier
                        .padding(paddingValues)
                        .fillMaxWidth()
                ) {
                    items(messages.size) { msg ->
                        MessageBox(
                            message = "msg.text",
                            time = "convertTimestampSmart(msg.timestamp)",
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


        val imageModifier : Modifier = Modifier.size(65.dp).padding(4.dp).clip(CircleShape)

        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(profile.profileImageUrl)
                .crossfade(true)
                .build(),
            contentDescription = null,
            imageLoader = (imageLoader),
            contentScale = ContentScale.Crop,
            modifier = imageModifier,
        )

        Column(
            modifier = Modifier
                .padding(vertical = 7.dp)
                .weight(1f),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                profile.firstName,
                fontFamily = modernist,
                fontWeight = FontWeight.W700,
                fontSize = 14.sp
            )

            Text(
                text = profile.message?.text?.takeIf { it.isNotEmpty() } ?: "Start a conversation",
                fontFamily = modernist,
                fontWeight = FontWeight.W400,
                fontSize = 14.sp
            )
        }

        Column(
            modifier = Modifier.padding(vertical = 7.dp),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = convertTimestampSmart(message.timestamp),
                fontFamily = modernist,
                fontWeight = FontWeight.W700,
                fontSize = 12.sp,
                color = Gray
            )

            if(message.unreadText != 0){
                Box(
                    modifier = Modifier
                        .size(25.dp)
                        .clip(CircleShape)
                        .background(HotPink),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = message.unreadText.toString(),
                        fontFamily = modernist,
                        fontWeight = FontWeight.W700,
                        fontSize = 12.sp,
                        color = White
                    )
                }
            }
        }
    }
}