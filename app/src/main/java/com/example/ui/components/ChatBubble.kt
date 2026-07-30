package com.example.ui.components

import android.text.format.DateFormat
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.ChatMessage
import com.example.model.MessageSender
import com.example.ui.theme.*
import java.util.Date

@Composable
fun ChatBubble(
    message: ChatMessage,
    modifier: Modifier = Modifier
) {
    val isUser = message.sender == MessageSender.USER
    val timeFormatted = DateFormat.format("hh:mm a", Date(message.timestamp)).toString()

    AnimatedVisibility(
        visible = true,
        enter = fadeIn() + slideInVertically(initialOffsetY = { 20 })
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp, horizontal = 12.dp),
            horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start,
            verticalAlignment = Alignment.Top
        ) {
            if (!isUser) {
                // AI Avatar Icon Badge
                Image(
                    painter = painterResource(id = R.drawable.img_user_logo_1785436243517),
                    contentDescription = "Assets AI Avatar",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .border(1.dp, GlassBorderCyan, CircleShape)
                )
                Spacer(modifier = Modifier.width(8.dp))
            }

            Column(
                horizontalAlignment = if (isUser) Alignment.End else Alignment.Start,
                modifier = Modifier.widthIn(max = 310.dp)
            ) {
                // Sender label
                Text(
                    text = if (isUser) "You" else "Assets AI",
                    color = if (isUser) PurpleBright else NeonCyan,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(bottom = 3.dp, start = 4.dp, end = 4.dp)
                )

                // Message Box
                Box(
                    modifier = Modifier
                        .clip(
                            RoundedCornerShape(
                                topStart = 20.dp,
                                topEnd = 20.dp,
                                bottomStart = if (isUser) 20.dp else 4.dp,
                                bottomEnd = if (isUser) 4.dp else 20.dp
                            )
                        )
                        .background(
                            if (isUser) {
                                Brush.linearGradient(
                                    colors = listOf(UserBubbleBg, Color(0xFF381B54))
                                )
                            } else {
                                Brush.linearGradient(
                                    colors = listOf(AiBubbleBg, Color(0xFF10192B))
                                )
                            }
                        )
                        .border(
                            width = 1.dp,
                            color = if (isUser) GlassBorderPurple else GlassBorderCyan,
                            shape = RoundedCornerShape(
                                topStart = 20.dp,
                                topEnd = 20.dp,
                                bottomStart = if (isUser) 20.dp else 4.dp,
                                bottomEnd = if (isUser) 4.dp else 20.dp
                            )
                        )
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                        .testTag(if (isUser) "user_message_bubble" else "ai_message_bubble")
                ) {
                    SelectionContainer {
                        Text(
                            text = message.text,
                            color = TextPrimary,
                            fontSize = 14.5.sp,
                            lineHeight = 21.sp,
                            fontWeight = FontWeight.Normal
                        )
                    }
                }

                // Time label
                Text(
                    text = timeFormatted,
                    color = TextMuted,
                    fontSize = 10.sp,
                    modifier = Modifier.padding(top = 4.dp, start = 4.dp, end = 4.dp)
                )
            }

            if (isUser) {
                Spacer(modifier = Modifier.width(8.dp))
                // User Avatar
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                colors = listOf(NeonPurple.copy(alpha = 0.3f), NeonMagenta.copy(alpha = 0.3f))
                            )
                        )
                        .border(1.dp, GlassBorderPurple, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "User Avatar",
                        tint = PurpleBright,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}
