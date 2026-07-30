package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.*

@Composable
fun Header(
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "glow_transition")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0.85f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glowAlpha"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        // Subtle background neon glow backdrop
        Box(
            modifier = Modifier
                .width(200.dp)
                .height(40.dp)
                .blur(24.dp)
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            NeonCyan.copy(alpha = glowAlpha * 0.4f),
                            NeonPurple.copy(alpha = glowAlpha * 0.4f)
                        )
                    ),
                    shape = CircleShape
                )
        )

        // Glassmorphic header card
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(24.dp))
                .background(SurfaceGlass)
                .border(
                    width = 1.dp,
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            GlassBorderCyan.copy(alpha = glowAlpha),
                            GlassBorderPurple.copy(alpha = glowAlpha)
                        )
                    ),
                    shape = RoundedCornerShape(24.dp)
                )
                .padding(horizontal = 20.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            // User Round Emblem Logo
            Image(
                painter = painterResource(id = R.drawable.img_user_logo_1785436243517),
                contentDescription = "Assets AI Logo",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .border(1.dp, GlassBorderCyan, CircleShape)
            )

            Spacer(modifier = Modifier.width(10.dp))

            // Text Header with Neon Accent
            Text(
                text = "Assets AI",
                color = TextPrimary,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.2.sp,
                modifier = Modifier.testTag("header_title")
            )

            Spacer(modifier = Modifier.width(10.dp))

            // Active indicator dot
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(NeonCyan)
                    .shadow(elevation = 6.dp, shape = CircleShape, ambientColor = NeonCyan, spotColor = NeonCyan)
            )
        }
    }
}
