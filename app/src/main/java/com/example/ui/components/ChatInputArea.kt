package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun ChatInputArea(
    isThinking: Boolean,
    onSendMessage: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var text by rememberSaveable { mutableStateOf("") }
    val canSend = text.isNotBlank() && !isThinking

    val sendButtonBgColor by animateColorAsState(
        targetValue = if (canSend) NeonCyan else SurfaceDark,
        animationSpec = tween(durationMillis = 200),
        label = "sendButtonBg"
    )

    val sendIconTint by animateColorAsState(
        targetValue = if (canSend) DarkBackground else TextMuted,
        animationSpec = tween(durationMillis = 200),
        label = "sendIconTint"
    )

    fun handleSend() {
        if (canSend) {
            val messageToSend = text
            text = ""
            onSendMessage(messageToSend)
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(28.dp))
                .background(SurfaceGlass)
                .border(
                    width = 1.dp,
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            if (canSend) GlassBorderCyan else GlassBorder,
                            if (canSend) GlassBorderPurple else GlassBorder
                        )
                    ),
                    shape = RoundedCornerShape(28.dp)
                )
                .padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Text Field
            TextField(
                value = text,
                onValueChange = { text = it },
                placeholder = {
                    Text(
                        text = if (isThinking) "Assets AI is responding..." else "Type your message or plan...",
                        color = TextMuted,
                        fontSize = 14.5.sp
                    )
                },
                textStyle = TextStyle(
                    color = TextPrimary,
                    fontSize = 15.sp
                ),
                singleLine = false,
                maxLines = 4,
                enabled = !isThinking,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    imeAction = ImeAction.Send
                ),
                keyboardActions = KeyboardActions(
                    onSend = { handleSend() }
                ),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    cursorColor = NeonCyan
                ),
                modifier = Modifier
                    .weight(1f)
                    .testTag("input_text_field")
            )

            Spacer(modifier = Modifier.width(6.dp))

            // Glowing Send Button
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .padding(2.dp)
                    .clip(CircleShape)
                    .background(
                        if (canSend) {
                            Brush.linearGradient(
                                colors = listOf(NeonCyan, CyanAccent)
                            )
                        } else {
                            Brush.linearGradient(
                                colors = listOf(SurfaceDark, SurfaceDark)
                            )
                        }
                    )
                    .border(
                        width = 1.dp,
                        color = if (canSend) GlassBorderCyan else GlassBorder,
                        shape = CircleShape
                    )
                    .then(
                        if (canSend) {
                            Modifier.shadow(
                                elevation = 8.dp,
                                shape = CircleShape,
                                ambientColor = NeonCyan,
                                spotColor = NeonCyan
                            )
                        } else Modifier
                    ),
                contentAlignment = Alignment.Center
            ) {
                IconButton(
                    onClick = { handleSend() },
                    enabled = canSend,
                    modifier = Modifier
                        .fillMaxSize()
                        .testTag("send_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Send Message",
                        tint = sendIconTint,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}
