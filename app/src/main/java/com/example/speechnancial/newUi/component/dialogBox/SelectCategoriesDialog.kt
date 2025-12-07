package com.example.speechnancial.newUi.component.dialogBox

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.example.speechnancial.ui.theme.SpeechnancialTheme

@Composable
fun SelectCategoriesDialog() {
}


@PreviewLightDark
@Composable
fun SelectCategoriesDialogPreview(
//    @PreviewParameter(InputTransactionStatePreviewParameterProvider::class) state: InputTransactionStatePreviewParameterProvider
) {
    SpeechnancialTheme {
        Surface {
            SelectCategoriesDialog()
        }
    }
}