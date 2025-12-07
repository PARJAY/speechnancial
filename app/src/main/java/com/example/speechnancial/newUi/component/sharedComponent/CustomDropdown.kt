package com.example.speechnancial.newUi.component.sharedComponent

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.example.speechnancial.data.firebase.model.EnumTransactionType
import com.example.speechnancial.ui.theme.SpeechnancialTheme

@Composable
fun <T> CustomDropdown(
    selectedItem: T,
    items: List<T>,
    onItemSelected: (T) -> Unit,
    itemToString: (T) -> String = { it.toString() }
) {
    var expanded by remember { mutableStateOf(false) }

    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    shape = RoundedCornerShape(4.dp),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                )
                .padding(start = 16.dp)
                .clickable { expanded = true }
        ) {
            Text(
                text = itemToString(selectedItem),
                modifier = Modifier.weight(1f)
            )
            IconButton(
                onClick = { expanded = !expanded },
            ) {
                Icon(
                    Icons.Filled.ArrowDropDown,
                    contentDescription = "Dropdown"
                )
            }
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    onClick = {
                        onItemSelected(item)
                        expanded = false
                    },
                    text = { Text(text = itemToString(item)) }
                )
            }
        }
    }
}

// Contoh Penggunaan
@Composable
fun ExampleDropdown() {
    var selectedType by remember { mutableStateOf(EnumTransactionType.INCOME) }

    CustomDropdown(
        selectedItem = selectedType,
        items = EnumTransactionType.entries.filter { it == EnumTransactionType.INCOME || it == EnumTransactionType.OUTCOME },
        onItemSelected = { selectedType = it }
    )
}

@PreviewLightDark
@Composable
fun CustomDropdownPreview() {
    SpeechnancialTheme {
        Surface {
            ExampleDropdown()
        }
    }
}