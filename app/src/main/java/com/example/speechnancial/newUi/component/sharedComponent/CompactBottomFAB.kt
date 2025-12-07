package com.example.speechnancial.newUi.component.sharedComponent

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun CompactBottomFAB(
    icons: ImageVector,
    onClick: () -> Unit,
    contentDescription: String
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        contentAlignment = Alignment.BottomEnd,
    ) {
        FloatingActionButton(
            onClick = { onClick() },
            containerColor = MaterialTheme.colorScheme.tertiary,
            shape = CircleShape,
            modifier = Modifier
                .padding(vertical = 8.dp)
                .border(
                    width = 2.dp,
                    MaterialTheme.colorScheme.tertiary,
                    shape = CircleShape
                )
        ) {
            Icon(
                icons,
                tint = MaterialTheme.colorScheme.primary,
                contentDescription = contentDescription,
            )
        }
    }
}