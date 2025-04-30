package com.hestabit.sparkmatch.screens.chat

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExpandedFullScreenSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SearchBarValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSearchBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hestabit.sparkmatch.R
import com.hestabit.sparkmatch.data.ChatMessage
import com.hestabit.sparkmatch.data.Story
import com.hestabit.sparkmatch.ui.theme.White
import com.hestabit.sparkmatch.ui.theme.modernist
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.S)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessageScreen(onNavigate: (String) -> Unit) {

    val sampleChats = listOf(
        ChatMessage("Emelie", R.drawable.emelie, "Sticker", "23 min", 1, true, true),
        ChatMessage("Abigail", R.drawable.abigail, "Typing...", "27min", 1, true, false),
        ChatMessage("Elizabeth", R.drawable.elizabeth, "Ok, see you then", "33 min", 0, true, true),
        ChatMessage("Penelope", R.drawable.penelope, "You: Hey! What’s up, long time..", "50 min", 0, false, false),
        ChatMessage("Chloe", R.drawable.chloe, "You: Hello how are you?", "55 min", 0, false, false),
        ChatMessage("Grace", R.drawable.img_4, "You: Great I will write later..", "1 hour", 0, false, true),
        ChatMessage("Emelie", R.drawable.emelie, "Sticker", "23 min", 1, true, true),
        ChatMessage("Abigail", R.drawable.abigail, "Typing...", "27min", 1, true, false),
        ChatMessage("Elizabeth", R.drawable.elizabeth, "Ok, see you then", "33 min", 0, true, true),
        ChatMessage("Penelope", R.drawable.penelope, "You: Hey! What’s up, long time..", "50 min", 0, false, false),
        ChatMessage("Chloe", R.drawable.chloe, "You: Hello how are you?", "55 min", 0, false, false),
        ChatMessage("Grace", R.drawable.img_4, "You: Great I will write later..", "1 hour", 0, false, true),
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

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(vertical = 10.dp, horizontal = 30.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            val searchBarState = rememberSearchBarState()
            val textFieldState = rememberTextFieldState()
            val scope = rememberCoroutineScope()

            val inputField = @Composable {
                SearchBarDefaults.InputField(
                    modifier = Modifier.fillMaxWidth(),
                    colors = SearchBarDefaults.inputFieldColors(
                        unfocusedContainerColor = Color.White,
                        focusedContainerColor = White
                    ),
                    shape = RoundedCornerShape(15.dp),
                    searchBarState = searchBarState,
                    textFieldState = textFieldState,
                    onSearch = { scope.launch { searchBarState.animateToCollapsed() } },
                    placeholder = {
                        Text(
                            "Search",
                            fontFamily = modernist,
                            fontWeight = FontWeight.W400,
                            fontSize = 14.sp
                        )
                    },
                    leadingIcon = {
                        if (searchBarState.currentValue == SearchBarValue.Expanded) {
                            IconButton(
                                onClick = { scope.launch { searchBarState.animateToCollapsed() } }
                            ) {
                                Icon(
                                    Icons.AutoMirrored.Default.ArrowBack,
                                    contentDescription = "Back"
                                )
                            }
                        } else {
                            Icon(painterResource(R.drawable.search), contentDescription = null, modifier = Modifier.size(20.dp))
                        }
                    },
                )
            }

            SearchBar(
                state = searchBarState,
                inputField = inputField,
                shape = RoundedCornerShape(15.dp),
                colors = SearchBarDefaults.colors(containerColor = Color.Unspecified),
                modifier = Modifier
                    .border(
                        width = 1.dp,
                        color = Color.LightGray,
                        shape = RoundedCornerShape(15.dp)
                    )

            )
            ExpandedFullScreenSearchBar(
                state = searchBarState,
                inputField = inputField,
                colors = SearchBarDefaults.colors(containerColor = White),
            ) {

            }
        }

        item {
            Text(
                "Activities",
                fontFamily = modernist,
                fontWeight = FontWeight.W700,
                fontSize = 18.sp,
            )

            Spacer(modifier = Modifier.height(10.dp))

            StoryList(stories = sampleStories, onNavigate = onNavigate)
        }

        item {
            Text(
                "Messages",
                fontWeight = FontWeight.W700,
                fontFamily = modernist,
                fontSize = 18.sp,
            )
        }

        items(12) {
            Column(horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.spacedBy(8.dp)) {
                MessageCell(modifier = Modifier.fillMaxWidth(), chatMessage = sampleChats[it])
                HorizontalDivider(modifier = Modifier.weight(1f), thickness = 1.dp, color = Color.LightGray)
            }
        }
    }
}