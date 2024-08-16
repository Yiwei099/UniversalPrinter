package com.eiviayw.libdraw.ui

import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpOffset

@Composable
fun <E>ParamPopupMenu(
    expanded: Boolean = false,
    offset: DpOffset,
    data:List<E>,
    getLabelText:(E) -> String,
    onConfirm: (E) -> Unit = {},
) {
    var rememberExpanded by remember { mutableStateOf(expanded) }

    DropdownMenu(expanded = expanded,
        offset = offset,
        onDismissRequest = {
            rememberExpanded = false
        }) {
        data.forEachIndexed { _, item ->
            DropdownMenuItem(onClick = {
                onConfirm.invoke(item)
            }, text = {
                Text(
                    text = getLabelText(item),
                    textAlign = TextAlign.End
                )
            })
        }
    }
}