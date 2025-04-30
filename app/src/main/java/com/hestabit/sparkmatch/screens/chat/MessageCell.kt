package com.hestabit.sparkmatch.screens.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun MessageCell(
    senderName: String,
    senderId: String,
    senderImage: Int,
    lastMessage: String,
    timestamp: String,
    unreadCount: Int,
    isOnline: Boolean,
    story: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(Color.Gray, shape = CircleShape),
            contentAlignment = Alignment.Center  // Properly center the icon
        ) {
            Icon(imageVector = Icons.Default.Person, contentDescription = null, tint = Color.White)

            // Online status indicator
            if (isOnline) {
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .background(Color.Green, CircleShape)
                        .align(Alignment.BottomEnd)
                )
            }

            // Story indicator
            if (story) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .border(2.dp, Color.Blue, CircleShape)
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(senderName, style = MaterialTheme.typography.bodyLarge)
                Text(timestamp, style = MaterialTheme.typography.bodySmall)
            }
            Text(lastMessage, style = MaterialTheme.typography.bodyMedium, maxLines = 1)
        }

        if (unreadCount > 0) {
            Box(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .size(20.dp)
                    .background(Color.Red, shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text("$unreadCount", style = MaterialTheme.typography.labelSmall, color = Color.White)
            }
        }
    }
}