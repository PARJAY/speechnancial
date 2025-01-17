package com.example.speechnancial.ui.component.inputTransactionScreen

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun CompactFAB(
    painterResources: Painter,
    onClick: () -> Unit,
    isActive: Boolean,
    contentDescription: String
) {
    FloatingActionButton(
        onClick = { onClick() },
        containerColor =
        if (isActive) MaterialTheme.colorScheme.tertiary
        else MaterialTheme.colorScheme.background,

        contentColor =
        if (isActive) MaterialTheme.colorScheme.background
        else MaterialTheme.colorScheme.primary,

        shape = CircleShape,
        modifier = Modifier
            .padding(vertical = 16.dp)
            .border(
                width = 2.dp,
                MaterialTheme.colorScheme.tertiary,
                shape = CircleShape
            )
    ) {
        Icon(
            painterResources,
            tint = MaterialTheme.colorScheme.primary,
            contentDescription = contentDescription,
        )
    }
}

@Composable
fun CompactFAB(
    icons: ImageVector,
    onClick: () -> Unit,
    isActive: Boolean,
    contentDescription: String
) {
    FloatingActionButton(
        onClick = { onClick() },
        containerColor =
        if (isActive) MaterialTheme.colorScheme.tertiary
        else MaterialTheme.colorScheme.background,

        contentColor =
        if (isActive) MaterialTheme.colorScheme.background
        else MaterialTheme.colorScheme.primary,

        shape = CircleShape,
        modifier = Modifier
            .padding(vertical = 16.dp)
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