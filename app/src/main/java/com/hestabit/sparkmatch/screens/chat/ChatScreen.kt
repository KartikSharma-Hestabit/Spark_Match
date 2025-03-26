package com.hestabit.sparkmatch.screens.chat

import android.app.appsearch.SearchResults
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExpandedFullScreenSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SearchBarValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
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
import com.hestabit.sparkmatch.ui.theme.Gray
import com.hestabit.sparkmatch.ui.theme.White
import com.hestabit.sparkmatch.ui.theme.modernist
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.S)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(onNavigate: (String) -> Unit) {

    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {

        item {

            val searchBarState = rememberSearchBarState()
            val textFieldState = rememberTextFieldState()
            val scope = rememberCoroutineScope()

            val inputField =
                @Composable {
                    SearchBarDefaults.InputField(
                        modifier = Modifier.padding(horizontal = 5.dp),
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
                                Icon(painterResource(R.drawable.search), contentDescription = null)
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
                    .padding(top = 20.dp)
                    .padding(horizontal = 40.dp)
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
//                SearchResults(
//                    onResultClick = { result ->
//                        textFieldState.setTextAndPlaceCursorAtEnd(result)
//                        scope.launch { searchBarState.animateToCollapsed() }
//                    }
//                )
            }
        }

        item {

            Text(
                "Activities",
                fontFamily = modernist,
                fontWeight = FontWeight.W700,
                fontSize = 18.sp,
                modifier = Modifier.padding(start = 40.dp)
            )

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(15.dp),
                modifier = Modifier.padding(top = 10.dp)
            ) {

                item { Spacer(modifier = Modifier.width(25.dp)) }

                items(10) {
                    StoryCell(onNavigate = onNavigate)
                }

                item { Spacer(modifier = Modifier.width(25.dp)) }

            }

        }

        item {
            Text(
                "Messages",
                fontWeight = FontWeight.W700,
                fontFamily = modernist,
                fontSize = 18.sp,
                modifier = Modifier.padding(horizontal = 40.dp)
            )
        }

        items(10) {
            Column(horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.spacedBy(8.dp)) {
                MessageCell(modifier = Modifier.fillMaxWidth().padding(horizontal = 40.dp))

                Row(modifier = Modifier.padding(horizontal = 40.dp)) {
                    HorizontalDivider(modifier = Modifier.weight(0.23f), thickness = 0.dp, color = White)
                    HorizontalDivider(modifier = Modifier.weight(1f), thickness = 1.dp, color = Color.LightGray)
                }
            }

        }
    }
}