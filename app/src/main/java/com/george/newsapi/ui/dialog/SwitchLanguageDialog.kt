package com.george.newsapi.ui.dialog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.george.newsapi.data.model.store.config.LanguageCode
import com.george.newsapi.data.model.strings.Strings

@Composable
fun SwitchLanguageDialog(
    currentSelection: LanguageCode,
    onDismiss: () -> Unit,
    onConfirm: (LanguageCode) -> Unit
) {
    var selectedLanguage by remember {
        mutableStateOf(currentSelection)
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = Strings.current.language,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column {
                LanguageCode.entries.forEach { language ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                selectedLanguage = language
                            }
                            .padding(vertical = 8.dp)
                    ) {
                        RadioButton(
                            selected = (selectedLanguage == language),
                            onClick = { selectedLanguage = language }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = language.displayName)
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                onConfirm(selectedLanguage)
            }) {
                Text(Strings.current.confirm)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(Strings.current.cancel)
            }
        },
        shape = RoundedCornerShape(16.dp)
    )
}
