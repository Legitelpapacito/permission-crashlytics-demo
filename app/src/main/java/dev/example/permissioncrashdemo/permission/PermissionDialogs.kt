package dev.example.permissioncrashdemo.permission

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import dev.example.permissioncrashdemo.R

@Composable
fun PermissionDialogs(
    decision: PermissionDecision?,
    rationale: String,
    onRequest: () -> Unit,
    onOpenSettings: () -> Unit,
    onDismiss: () -> Unit,
) {
    if (decision != PermissionDecision.Rationale && decision != PermissionDecision.Blocked) return
    val blocked = decision == PermissionDecision.Blocked
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(stringResource(if (blocked) R.string.settings_title else R.string.permission_title))
        },
        text = {
            Text(if (blocked) stringResource(R.string.settings_description) else rationale)
        },
        confirmButton = {
            TextButton(onClick = if (blocked) onOpenSettings else onRequest) {
                Text(stringResource(if (blocked) R.string.open_settings else R.string.continue_action))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(stringResource(R.string.cancel_action)) }
        },
    )
}
