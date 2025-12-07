package com.example.speechnancial.newUi.screen.categoryListScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.example.speechnancial.data.firebase.model.Category
import com.example.speechnancial.data.firebase.viewmodel.CategoryScreenEvent
import com.example.speechnancial.data.firebase.viewmodel.CategoryScreenUiState
import com.example.speechnancial.newUi.component.CategoryItem
import com.example.speechnancial.newUi.component.dialogBox.DialogCategoryCrud
import com.example.speechnancial.newUi.component.sharedComponent.CompactBottomFAB
import com.example.speechnancial.newUi.component.sharedComponent.Title
import com.example.speechnancial.ui.theme.SpeechnancialTheme

@Composable
fun CategoryListScreen(
    categoryScreenUiState: CategoryScreenUiState,
    onEvent: (CategoryScreenEvent) -> Unit,
) {
    val context = LocalContext.current

    DialogCategoryCrud(
        categoryScreenUiState.showDialogCategoryCrud,
        onDismiss = { onEvent(CategoryScreenEvent.CloseDialog) },
        selectedCategory = categoryScreenUiState.selectedCategory,
        onSave = { onEvent(CategoryScreenEvent.SaveCategory(it, context)) },
        onUpdate = { onEvent(CategoryScreenEvent.UpdateCategory(it, context)) },
        onDelete = { onEvent(CategoryScreenEvent.HardDeleteCategory(it, context)) }
    )

    LazyColumn (Modifier.padding(top = 16.dp)) {
        item {
            Title("Daftar Kategori")
            Spacer(Modifier.height(4.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                FilterChip(
                    selected = categoryScreenUiState.isIncomeFilterEnabled,
                    onClick = { onEvent(CategoryScreenEvent.IncomeFilterChanged(!categoryScreenUiState.isIncomeFilterEnabled)) },
                    label = { Text("Income") }
                )
                Spacer(modifier = Modifier.width(8.dp))
                FilterChip(
                    selected = categoryScreenUiState.isOutcomeFilterEnabled,
                    onClick = { onEvent(CategoryScreenEvent.OutcomeFilterChanged(!categoryScreenUiState.isOutcomeFilterEnabled)) },
                    label = { Text("Outcome") }
                )
            }
            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = categoryScreenUiState.searchQuery,
                onValueChange = { query ->
                    onEvent(CategoryScreenEvent.OnSearchQueryChange(query))
                },
                label = { Text("Cari kategori...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                singleLine = true,
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search Icon") }
            )
            Spacer(modifier = Modifier.height(8.dp))
            Spacer(modifier = Modifier.height(8.dp))
        }

        items(categoryScreenUiState.categories) {
            if (!it.isDeleted)
                CategoryItem(it.name, it.enumTransactionType) {
                    onEvent(CategoryScreenEvent.SetSelectedCategory(it))
                    onEvent(CategoryScreenEvent.OpenDialog)
                }
        }
    }

    CompactBottomFAB(
        icons = Icons.Filled.Add,
        onClick = {
            onEvent(CategoryScreenEvent.OpenDialog)
        },
        contentDescription = "Add Category"
    )
}


@PreviewLightDark
@Composable
fun CategoryListScreenPreview() {
    SpeechnancialTheme {
        Surface {
            CategoryListScreen(
                categoryScreenUiState = CategoryScreenUiState(
                    listOf(
                        Category(name = "makanan"),
                        Category(name = "minuman"),
                        Category(name = "utilitas"),
                        Category(name = "perawatan kendaraan"),
                        Category(name = "kesehatan"),
                        Category(name = "kesehatan"),
                    )
                ),
                onEvent = {

                }
            )
        }
    }
}